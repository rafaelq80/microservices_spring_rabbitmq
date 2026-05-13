package com.rfl.product_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rfl.product_service.dto.ErroResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Recurso não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 400 - Erro de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> erros = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Campos inválidos",
                request.getRequestURI(),
                LocalDateTime.now(),
                erros
        );

        return ResponseEntity.badRequest().body(erro);
    }

    // 400 - Violação de constraint (@RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponseDTO> tratarConstraint(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, String> erros = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String campo = violation.getPropertyPath().toString();
            String mensagem = violation.getMessage();
            erros.put(campo, mensagem);
        });

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Parâmetros inválidos",
                request.getRequestURI(),
                LocalDateTime.now(),
                erros
        );

        return ResponseEntity.badRequest().body(erro);
    }

    // 400 - JSON mal formatado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> tratarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "JSON inválido",
                "O corpo da requisição está mal formatado ou possui tipos incompatíveis",
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.badRequest().body(erro);
    }

    // 409 - Conflito (ex: regra de negócio)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> tratarConflito(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflito de regra de negócio",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    // 500 - Erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(
            Exception ex,
            HttpServletRequest request) {

        log.error("Erro inesperado", ex);

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado",
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);
    }
}