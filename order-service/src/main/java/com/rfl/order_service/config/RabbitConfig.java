package com.rfl.order_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PRODUCTS_QUEUE = "products.queue";

    private final AppProperties properties;

    public RabbitConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                  JacksonJsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(messageConverter);
        return template;
    }

    // ── Exchange e fila de pedidos (producer) ─────────────────────────────────

    @Bean
    TopicExchange ordersExchange() {
        return new TopicExchange(
                properties.rabbitmq().exchange(),
                properties.rabbitmq().durable(),
                false
        );
    }

    @Bean
    Queue ordersQueue() {
        return new Queue(
                properties.rabbitmq().queue(),
                properties.rabbitmq().durable()
        );
    }

    @Bean
    Binding ordersBinding(Queue ordersQueue, TopicExchange ordersExchange) {
        return BindingBuilder
                .bind(ordersQueue)
                .to(ordersExchange)
                .with(properties.rabbitmq().routingKey());
    }

    // ── Fila de produtos (consumer) ───────────────────────────────────────────
    // A exchange e o binding já foram declarados pelo Product Service.
    // O Order Service só precisa garantir que a fila existe para consumir.

    @Bean
    Queue productsQueue() {
        return new Queue(PRODUCTS_QUEUE, true);
    }

    // ── Listener factory com converter ────────────────────────────────────────

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, JacksonJsonMessageConverter messageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}