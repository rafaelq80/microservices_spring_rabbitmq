package com.rfl.notification_service.event;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PedidoAtualizadoEvent(
		@JsonProperty("pedidoId") Long pedidoId,
		@JsonProperty("criadoPor") String criadoPor,
		@JsonProperty("novoStatus") String novoStatus,
		@JsonProperty("valorTotal") BigDecimal valorTotal,
		@JsonProperty("itens") List<ItemEvent> itens
) {}