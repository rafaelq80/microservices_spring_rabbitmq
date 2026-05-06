package com.rfl.order_service.event;

public record PedidoAtualizadoEvent(
    Long id,
    String produto,
    Integer quantidade,
    String status
) {}