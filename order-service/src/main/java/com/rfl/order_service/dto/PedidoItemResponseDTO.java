package com.rfl.order_service.dto;

import java.math.BigDecimal;

import com.rfl.order_service.enums.StatusItem;
import com.rfl.order_service.model.PedidoItem;

public record PedidoItemResponseDTO(
        Long id,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal,
        StatusItem status
) {
    public static PedidoItemResponseDTO from(PedidoItem item) {
        return new PedidoItemResponseDTO(
                item.getId(),
                item.getProdutoCache().getId(),
                item.getProdutoCache().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal(),
                item.getStatus()
        );
    }
}
