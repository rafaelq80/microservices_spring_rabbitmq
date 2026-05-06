package com.rfl.order_service.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Jwt jwt,
        Rabbitmq rabbitmq
) {

    @ConstructorBinding
    public AppProperties {}  // canonical constructor explícito

    public record Jwt(
            @NotBlank String secret
    ) {}

    public record Rabbitmq(
            @NotBlank String exchange,
            @NotBlank String routingKey,
            @NotBlank String queue,
            boolean durable
    ) {}
}