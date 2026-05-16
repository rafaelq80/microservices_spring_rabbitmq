package com.rfl.order_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.rfl.order_service.dto.ErroResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Recurso de negócio não encontrado
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

    // 404 - Endpoint inexistente (Spring MVC — versões anteriores ao Spring 6)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErroResponseDTO> tratarEndpointNaoEncontrado(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint não encontrado",
                "Nenhum handler encontrado para " + ex.getHttpMethod() + " " + ex.getRequestURL(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 404 - Endpoint inexistente (Spring 6+ / Spring Boot 3+)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponseDTO> tratarRecursoNaoEncontradoSpring6(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint não encontrado",
                "Nenhum recurso encontrado para " + request.getMethod() + " " + request.getRequestURI(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 405 - Método HTTP não permitido (ex: POST em endpoint que só aceita GET)
 // Handler corrigido — nome da exception estava errado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponseDTO> tratarMetodoNaoPermitido(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Método não permitido",
                "O método " + ex.getMethod() + " não é suportado para esta rota",
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erro);
    }
    
    // 400 - Erro de validação (@Valid em @RequestBody)
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

    // 400 - Violação de constraint (@RequestParam, @PathVariable com @Validated)
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

    // 400 - @RequestParam obrigatório ausente
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponseDTO> tratarParametroAusente(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetro ausente",
                "O parâmetro obrigatório '" + ex.getParameterName() + "' não foi informado",
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.badRequest().body(erro);
    }

    // 400 - Tipo incompatível em @PathVariable ou @RequestParam (ex: String onde se espera Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDTO> tratarTipoInvalido(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String mensagem = String.format(
                "O parâmetro '%s' recebeu o valor '%s', que é incompatível com o tipo esperado '%s'",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido"
        );

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Tipo de parâmetro inválido",
                mensagem,
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.badRequest().body(erro);
    }

    // 400 - JSON mal formatado ou tipo incompatível no body
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

 // 422 - Recurso Duplicado
    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErroResponseDTO> tratarRegraDeNegocio(
            RecursoDuplicadoException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
        		HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Recurso Duplicado",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }
    
    // 422 - Recurso inativo
    @ExceptionHandler(RecursoInativoException.class)
    public ResponseEntity<ErroResponseDTO> tratarRecursoInativo(
            RecursoInativoException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Recurso inativo",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erro);
    }
    
    // 500 - Erro inesperado (fallback — nunca deve aparecer em produção com frequência)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(
            Exception ex,
            HttpServletRequest request) {

        log.error("Erro inesperado em {} {}", request.getMethod(), request.getRequestURI(), ex);

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);
    }
}