package com.rfl.order_service.mapper;

import com.rfl.order_service.dto.PedidoResponseDTO;
import com.rfl.order_service.model.Pedido;

import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        return PedidoResponseDTO.from(pedido);
    }
}