package com.rfl.product_service.message;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import com.rfl.product_service.config.AppProperties;
import com.rfl.product_service.enums.TipoEventoProduto;
import com.rfl.product_service.event.ProdutoEvent;
import com.rfl.product_service.model.Produto;

@Component
public class ProdutoEventPublisher {

    private final AmqpTemplate amqpTemplate;
    private final AppProperties appProperties;

    ProdutoEventPublisher(AmqpTemplate amqpTemplate, AppProperties appProperties) {
        this.amqpTemplate  = amqpTemplate;
        this.appProperties = appProperties;
    }

    public void publicar(Produto produto, TipoEventoProduto tipo) {
        var evento = new ProdutoEvent(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.isAtivo(),
                tipo
        );
        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                evento
        );
    }
}