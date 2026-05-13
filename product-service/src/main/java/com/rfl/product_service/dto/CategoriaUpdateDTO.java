package com.rfl.product_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaUpdateDTO(
        @NotNull 
        Long id,
        
        @Size(max = 100) 
        String nome,
        
        @Size(max = 255) 
        String descricao,
        
        Boolean ativo
) {}