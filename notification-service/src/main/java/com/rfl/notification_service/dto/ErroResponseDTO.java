package com.rfl.notification_service.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErroResponseDTO(
        int status,
        String erro,
        String mensagem,
        String caminho,
        LocalDateTime timestamp,
        Map<String, String> campos
) {}