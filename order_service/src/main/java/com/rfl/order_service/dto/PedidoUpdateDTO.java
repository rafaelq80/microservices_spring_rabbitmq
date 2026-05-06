package com.rfl.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PedidoUpdateDTO(

        @NotNull(message = "O id é obrigatório")
        Long id,

        @NotBlank(message = "O produto é obrigatório")
        String produto,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @NotBlank(message = "O status é obrigatório")
        String status
) {}