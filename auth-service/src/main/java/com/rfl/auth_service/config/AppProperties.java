package com.rfl.auth_service.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Jwt jwt
) {

    @ConstructorBinding
    public AppProperties {}  // canonical constructor explícito

    public record Jwt(
            @NotBlank String secret,
            @NotBlank String expiration
    ) {}

}