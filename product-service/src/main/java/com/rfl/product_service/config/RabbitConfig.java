package com.rfl.product_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private final AppProperties properties;

    public RabbitConfig(AppProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(
                properties.rabbitmq().exchange(),
                properties.rabbitmq().durable(),
                false
        );
    }

    @Bean
    Queue queue() {
        return new Queue(
                properties.rabbitmq().queue(),
                properties.rabbitmq().durable()
        );
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(properties.rabbitmq().routingKey());
    }
}