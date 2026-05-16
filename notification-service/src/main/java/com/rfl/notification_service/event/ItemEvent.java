package com.rfl.notification_service.event;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemEvent(
		@JsonProperty("produtoId") Long produtoId,
		@JsonProperty("nomeProduto") String nomeProduto,
		@JsonProperty("quantidade") Integer quantidade,
		@JsonProperty("precoUnitario") BigDecimal precoUnitario,
		@JsonProperty("subtotal") BigDecimal subtotal
) {}