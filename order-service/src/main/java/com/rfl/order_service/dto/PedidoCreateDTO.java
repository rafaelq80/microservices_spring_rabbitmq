package com.rfl.order_service.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoCreateDTO(
        @NotNull @Size(min = 1) List<@Valid ItemDTO> itens
) {
    public record ItemDTO(
            @NotNull Long produtoId,
            @NotNull @Min(1) Integer quantidade
    ) {}
}