package com.rfl.order_service.event;

import java.math.BigDecimal;
import java.util.List;

public record PedidoCriadoEvent(
        Long pedidoId,
        String criadoPor,
        BigDecimal valorTotal,
        List<ItemEvent> itens
) {}