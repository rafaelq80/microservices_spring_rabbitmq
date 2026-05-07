package com.rfl.order_service.event;

public record PedidoAtualizadoEvent(
	Long pedidoId,
    String produto,
    Integer quantidade,
    String status
) {}