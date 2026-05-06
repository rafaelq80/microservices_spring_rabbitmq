package com.rfl.order_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rfl.order_service.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	List<Pedido> findAllByCriadoPor(String criadoPor);
	
}