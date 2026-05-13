package com.rfl.product_service.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Jwt jwt,
        Rabbitmq rabbitmq
) {

    public record Jwt(
            @NotBlank String secret,
            @NotBlank String expiration
    ) {}

    public record Rabbitmq(
            @NotBlank String exchange,
            @NotBlank String routingKey,
            @NotBlank String queue,
            boolean durable
    ) {}
}