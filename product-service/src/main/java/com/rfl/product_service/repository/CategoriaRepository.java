package com.rfl.product_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rfl.product_service.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	boolean existsByNome(String nome);

	List<Categoria> findAllByAtivo(boolean ativo);
}
