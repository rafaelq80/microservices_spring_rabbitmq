package com.rfl.product_service.mapper;

import org.springframework.stereotype.Component;

import com.rfl.product_service.dto.CategoriaResponseDTO;
import com.rfl.product_service.model.Categoria;

@Component
public class CategoriaMapper {

    public CategoriaResponseDTO toCategoriaResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.isAtivo()
        );
    }

}