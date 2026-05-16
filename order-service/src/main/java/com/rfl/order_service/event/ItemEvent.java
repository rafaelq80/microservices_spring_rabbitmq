package com.rfl.order_service.event;

import java.math.BigDecimal;

public record ItemEvent(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
