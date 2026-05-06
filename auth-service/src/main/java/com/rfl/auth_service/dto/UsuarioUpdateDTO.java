package com.rfl.auth_service.dto;

import com.rfl.auth_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(

		@NotNull(message = "O id é obrigatório")
	    Long id,
	        
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @Email(message = "O usuário deve ser um e-mail válido")
        String usuario,

        // Senha opcional na atualização — só atualiza se informada
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,
        
        @Size(max = 500, message = "O link da foto deve ter no máximo 500 caracteres")
        String foto,

        // Role opcional — só atualiza se informado (uso restrito a ADMIN)
        Role role,

        // Permite ativar/desativar o usuário (uso restrito a ADMIN)
        Boolean ativo

) {}