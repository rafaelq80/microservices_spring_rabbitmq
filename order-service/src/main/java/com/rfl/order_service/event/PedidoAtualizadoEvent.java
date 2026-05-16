package com.rfl.order_service.event;

import java.math.BigDecimal;
import java.util.List;

public record PedidoAtualizadoEvent(
        Long pedidoId,
        String criadoPor,
        String novoStatus,
        BigDecimal valorTotal,
        List<ItemEvent> itens
) {}