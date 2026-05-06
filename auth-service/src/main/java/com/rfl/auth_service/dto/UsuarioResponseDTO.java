package com.rfl.auth_service.dto;

import java.time.LocalDateTime;

import com.rfl.auth_service.enums.Role;

public record UsuarioResponseDTO(

        Long id,
        String nome,
        String usuario,
        String foto,
        Role role,
        boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {}
