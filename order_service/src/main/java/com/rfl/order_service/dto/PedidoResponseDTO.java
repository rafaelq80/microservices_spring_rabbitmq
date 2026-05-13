package com.rfl.order_service.dto;

import java.time.LocalDateTime;

public record PedidoResponseDTO(
        Long id,
        String produto,
        Integer quantidade,
        String status,
        String criadoPor,
        String atualizadoPor,
        LocalDateTime criadoEm
) {}