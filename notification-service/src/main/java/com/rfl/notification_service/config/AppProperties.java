package com.rfl.notification_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
		Jwt jwt,
		Rabbitmq rabbitmq
) {

	public record Jwt(
            @NotBlank String secret
    ) {}
	
    public record Rabbitmq(
            @NotBlank String queue
    ) {}
}