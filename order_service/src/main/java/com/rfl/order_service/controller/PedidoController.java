package com.rfl.order_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rfl.order_service.dto.PedidoCreateDTO;
import com.rfl.order_service.dto.PedidoResponseDTO;
import com.rfl.order_service.dto.PedidoUpdateDTO;
import com.rfl.order_service.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
		this.service = service;
	}

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarPedidos());
    }
    
    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // FIND ALL BY CRIADOR
    @GetMapping("/criador/{criador}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCriador(
            @PathVariable String criador) {

        return ResponseEntity.ok(service.listarPorCriador(criador));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(
            @Valid @RequestBody PedidoCreateDTO pedidoDTO,
            Authentication authentication) {

        String usuario = authentication.getName();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criarPedido(pedidoDTO, usuario));
    }
    
    // UPDATE
    @PutMapping
    public ResponseEntity<PedidoResponseDTO> atualizar(
            @Valid @RequestBody PedidoUpdateDTO pedidoDTO) {

        return ResponseEntity.ok(service.atualizarPedido(pedidoDTO));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }
}