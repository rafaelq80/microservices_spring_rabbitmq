package com.rfl.product_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoCreateDTO(
		
        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 150) 
        String nome,

        @Size(max = 500) 
        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin("0.01") 
        BigDecimal preco,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId
) {}