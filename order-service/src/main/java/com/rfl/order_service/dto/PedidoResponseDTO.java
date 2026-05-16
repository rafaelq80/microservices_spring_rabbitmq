package com.rfl.order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.rfl.order_service.enums.StatusPedido;
import com.rfl.order_service.model.Pedido;

public record PedidoResponseDTO(
        Long id,
        String criadoPor,
        StatusPedido status,
        BigDecimal valorTotal,
        List<PedidoItemResponseDTO> itens,
        LocalDateTime criadoEm
) {
    public static PedidoResponseDTO from(Pedido pedido) {
        return new PedidoResponseDTO(
        		pedido.getId(), 
        		pedido.getCriadoPor(), 
        		pedido.getStatus(), 
        		pedido.getValorTotal(),
        		pedido.getItens().stream().map(PedidoItemResponseDTO::from).toList(),
        		pedido.getCriadoEm()
        );
    }
}