package com.rfl.order_service.repository;

import com.rfl.order_service.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    List<PedidoItem> findAllByPedidoId(Long pedidoId);
}