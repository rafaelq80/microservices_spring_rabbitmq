package com.rfl.product_service.mapper;

import org.springframework.stereotype.Component;

import com.rfl.product_service.dto.CategoriaCreateDTO;
import com.rfl.product_service.dto.CategoriaResponseDTO;
import com.rfl.product_service.dto.CategoriaUpdateDTO;
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

    public Categoria toCategoriaEntity(CategoriaCreateDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.nome());
        categoria.setDescricao(categoriaDTO.descricao());
        return categoria;
    }

    public void updateToCategoriaEntity(CategoriaUpdateDTO categoriaDTO, Categoria categoria) {
        if (categoriaDTO.nome() != null) {
            categoria.setNome(categoriaDTO.nome());
        }
        if (categoriaDTO.descricao() != null) {
            categoria.setDescricao(categoriaDTO.descricao());
        }
        if (categoriaDTO.ativo() != null) {
            categoria.setAtivo(categoriaDTO.ativo());
        }
    }
}