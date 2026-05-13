package com.rfl.product_service.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        CategoriaResponseDTO categoria,
        boolean ativo
) {}
