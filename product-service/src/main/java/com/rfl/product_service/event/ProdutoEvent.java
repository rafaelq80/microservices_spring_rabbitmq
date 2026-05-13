package com.rfl.product_service.event;

import java.math.BigDecimal;

import com.rfl.product_service.enums.TipoEventoProduto;

/**
 * Evento publicado no RabbitMQ sempre que um produto é criado,
 * atualizado ou desativado. O Order Service consome este evento
 * para manter seu cache local (tb_produto_cache) sincronizado.
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