package com.rfl.product_service.dto;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao,
        boolean ativo
) {}