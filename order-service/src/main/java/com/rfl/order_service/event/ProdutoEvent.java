package com.rfl.order_service.event;

import java.math.BigDecimal;

import com.rfl.order_service.enums.TipoEventoProduto;

/**
 * Espelho do ProdutoEvent publicado pelo Product Service.
 * Deve ter os mesmos campos para que o Jackson consiga deserializar.
 */
public record ProdutoEvent(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Long categoriaId,
        String nomeCategoria,
        boolean ativo,
        TipoEventoProduto tipo
) {}