package com.rfl.order_service.dto;

import java.util.List;

import com.rfl.order_service.enums.StatusItem;
import com.rfl.order_service.enums.StatusPedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoUpdateStatusDTO(
        @NotNull Long pedidoId,
        StatusPedido status,
        @Size(min = 1) List<@Valid ItemDTO> itens
) {
    public record ItemDTO(
            @NotNull Long itemId,
            @NotNull StatusItem status
    ) {}
}