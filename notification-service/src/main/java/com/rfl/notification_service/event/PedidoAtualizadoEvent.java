package com.rfl.notification_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PedidoAtualizadoEvent(
    @JsonProperty("pedidoId") Long pedidoId,
    @JsonProperty("produto") String produto,
    @JsonProperty("quantidade") Integer quantidade,
    @JsonProperty("status") String status
) {}