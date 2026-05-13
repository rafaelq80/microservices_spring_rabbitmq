package com.rfl.product_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rfl.product_service.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findAllByAtivo(boolean ativo);

	List<Produto> findAllByCategoriaId(Long categoriaId);
}
