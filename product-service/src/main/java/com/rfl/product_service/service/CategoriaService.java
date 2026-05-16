package com.rfl.product_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rfl.product_service.dto.CategoriaCreateDTO;
import com.rfl.product_service.dto.CategoriaUpdateDTO;
import com.rfl.product_service.exception.RecursoNaoEncontradoException;
import com.rfl.product_service.exception.RecursoDuplicadoException;
import com.rfl.product_service.model.Categoria;
import com.rfl.product_service.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Categoria não encontrada")
                );
    }

    public Categoria criar(CategoriaCreateDTO dto) {

        if (categoriaRepository.existsByNome(dto.nome())) {
            throw new RecursoDuplicadoException("Categoria '" + dto.nome() + "' já existe");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());

        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(CategoriaUpdateDTO dto) {

        Categoria categoria = categoriaRepository.findById(dto.id())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Categoria não encontrada")
                );

        if (dto.nome() != null) {
            boolean nomeAlterado = !dto.nome().equalsIgnoreCase(categoria.getNome());

            if (nomeAlterado && categoriaRepository.existsByNome(dto.nome())) {
                throw new IllegalArgumentException("Categoria '" + dto.nome() + "' já existe");
            }
        }

        if (dto.nome() != null) categoria.setNome(dto.nome());
        if (dto.descricao() != null) categoria.setDescricao(dto.descricao());
        if (dto.ativo() != null) categoria.setAtivo(dto.ativo());

        return categoriaRepository.save(categoria);
    }

    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}