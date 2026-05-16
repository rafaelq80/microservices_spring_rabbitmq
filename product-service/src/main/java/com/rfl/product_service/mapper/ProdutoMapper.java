package com.rfl.product_service.mapper;

import org.springframework.stereotype.Component;

import com.rfl.product_service.dto.ProdutoResponseDTO;
import com.rfl.product_service.model.Produto;

@Component
public class ProdutoMapper {

    private final CategoriaMapper categoriaMapper;

    public ProdutoMapper(CategoriaMapper categoriaMapper) {
        this.categoriaMapper = categoriaMapper;
    }

    public ProdutoResponseDTO toProdutoResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                categoriaMapper.toCategoriaResponseDTO(produto.getCategoria()),
                produto.isAtivo()
        );
    }
}