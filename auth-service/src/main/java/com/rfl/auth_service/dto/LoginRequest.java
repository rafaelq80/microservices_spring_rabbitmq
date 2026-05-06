package com.rfl.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank(message = "Usuário não pode ser vazio")
    String usuario,

    @NotBlank(message = "Senha não pode ser vazia")
    String senha
) {}