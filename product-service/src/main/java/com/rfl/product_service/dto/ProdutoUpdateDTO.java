package com.rfl.product_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoUpdateDTO(
		
        @NotNull 
        Long id,
        
        @Size(max = 150) 
        String nome,
        
        @Size(max = 500) 
        String descricao,
        
        @DecimalMin("0.01") 
        BigDecimal preco,
        Long categoriaId,
        
        Boolean ativo
) {}