package com.rfl.order_service.messaging;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import com.rfl.order_service.config.AppProperties;
import com.rfl.order_service.enums.StatusItem;
import com.rfl.order_service.event.ItemEvent;
import com.rfl.order_service.event.PedidoAtualizadoEvent;
import com.rfl.order_service.event.PedidoCriadoEvent;
import com.rfl.order_service.model.Pedido;

@Component
public class PedidoEventPublisher {

    private final AmqpTemplate amqpTemplate;
    private final AppProperties appProperties;

    public PedidoEventPublisher(AmqpTemplate amqpTemplate, AppProperties appProperties) {
        this.amqpTemplate  = amqpTemplate;
        this.appProperties = appProperties;
    }

    public void publicarCriado(Pedido pedido) {
        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                new PedidoCriadoEvent(
                        pedido.getId(),
                        pedido.getCriadoPor(),
                        pedido.getValorTotal(),
                        toItemEventList(pedido)
                )
        );
    }

    public void publicarAtualizado(Pedido pedido) {
        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                new PedidoAtualizadoEvent(
                        pedido.getId(),
                        pedido.getCriadoPor(),
                        pedido.getStatus().name(),
                        pedido.getValorTotal(),
                        toItemEventList(pedido)
                )
        );
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private List<ItemEvent> toItemEventList(Pedido pedido) {
        return pedido.getItens().stream()
                .filter(i -> i.getStatus() == StatusItem.ATIVO)
                .map(i -> new ItemEvent(
                        i.getProdutoCache().getId(),
                        i.getProdutoCache().getNome(),
                        i.getQuantidade(),
                        i.getPrecoUnitario(),
                        i.getSubtotal()
                )).toList();
    }
}