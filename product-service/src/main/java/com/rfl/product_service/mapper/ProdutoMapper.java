package com.rfl.product_service.mapper;

import org.springframework.stereotype.Component;

import com.rfl.product_service.dto.ProdutoCreateDTO;
import com.rfl.product_service.dto.ProdutoResponseDTO;
import com.rfl.product_service.dto.ProdutoUpdateDTO;
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

    public Produto toProdutoEntity(ProdutoCreateDTO produtoDTO) {
        Produto produto = new Produto();
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        return produto;
    }

    public void updateToProdutoEntity(ProdutoUpdateDTO produtoDTO, Produto produto) {
        if (produtoDTO.nome() != null) {
            produto.setNome(produtoDTO.nome());
        }

        if (produtoDTO.descricao() != null) {
            produto.setDescricao(produtoDTO.descricao());
        }

        if (produtoDTO.preco() != null) {
            produto.setPreco(produtoDTO.preco());
        }

        if (produtoDTO.ativo() != null) {
            produto.setAtivo(produtoDTO.ativo());
        }
    }
}