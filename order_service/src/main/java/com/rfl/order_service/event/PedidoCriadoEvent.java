package com.rfl.order_service.event;

public record PedidoCriadoEvent(
        Long pedidoId,
        String produto,
        Integer quantidade,
        String criadoPor
) {}