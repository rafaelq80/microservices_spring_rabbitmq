package com.rfl.notification_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rfl.notification_service.config.RabbitConfig;
import com.rfl.notification_service.event.PedidoAtualizadoEvent;
import com.rfl.notification_service.event.PedidoCriadoEvent;
import com.rfl.notification_service.service.NotificacaoService;

@Component
public class PedidoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoEventConsumer.class);

    private final NotificacaoService notificacaoService;

    public PedidoEventConsumer(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onPedidoCriado(PedidoCriadoEvent evento) {
        log.info("[Consumer] PedidoCriadoEvent recebido: pedidoId={}", evento.pedidoId());
        notificacaoService.processarPedidoCriado(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onPedidoAtualizado(PedidoAtualizadoEvent evento) {
        log.info("[Consumer] PedidoAtualizadoEvent recebido: pedidoId={}", evento.pedidoId());
        notificacaoService.processarPedidoAtualizado(evento);
    }
}