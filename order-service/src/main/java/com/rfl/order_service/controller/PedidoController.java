package com.rfl.order_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rfl.order_service.dto.PedidoCreateDTO;
import com.rfl.order_service.dto.PedidoResponseDTO;
import com.rfl.order_service.dto.PedidoUpdateStatusDTO;
import com.rfl.order_service.mapper.PedidoMapper;
import com.rfl.order_service.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    PedidoController(PedidoService pedidoService,
                     PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper  = pedidoMapper;
    }

    @GetMapping
    public List<PedidoResponseDTO> listar() {
        return pedidoService.listar()
                .stream()
                .map(pedidoMapper::toPedidoResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return pedidoMapper.toPedidoResponseDTO(pedidoService.buscarPorId(id));
    }

    @GetMapping("/criador")
    public List<PedidoResponseDTO> listarPorCriador(Authentication auth) {
        return pedidoService.listarPorCriador(auth.getName())
                .stream()
                .map(pedidoMapper::toPedidoResponseDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criar(
            @Valid @RequestBody PedidoCreateDTO dto,
            Authentication auth) {
        return pedidoMapper.toPedidoResponseDTO(pedidoService.criar(dto, auth.getName()));
    }

    @PatchMapping("/status")
    public PedidoResponseDTO atualizarStatus(
            @Valid @RequestBody PedidoUpdateStatusDTO dto) {
        return pedidoMapper.toPedidoResponseDTO(pedidoService.atualizar(dto));
    }

    @PatchMapping
    public PedidoResponseDTO atualizar(@Valid @RequestBody PedidoUpdateStatusDTO dto) {
        return pedidoMapper.toPedidoResponseDTO(pedidoService.atualizar(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        pedidoService.deletarPedido(id);
    }
}