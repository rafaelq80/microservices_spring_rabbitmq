package com.rfl.auth_service.dto;

import com.rfl.auth_service.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreateDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "O usuário é obrigatório")
        @Email(message = "O usuário deve ser um e-mail válido")
        String usuario,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,
        
        @Size(max = 500, message = "O link da foto deve ter no máximo 500 caracteres")
        String foto,

        @NotNull(message = "O role é obrigatório")
        Role role

) {}