package com.rfl.order_service.repository;

import com.rfl.order_service.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByCriadoPor(String criadoPor);
}