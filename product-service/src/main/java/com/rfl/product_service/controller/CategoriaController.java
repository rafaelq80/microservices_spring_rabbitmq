package com.rfl.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rfl.product_service.dto.CategoriaCreateDTO;
import com.rfl.product_service.dto.CategoriaResponseDTO;
import com.rfl.product_service.dto.CategoriaUpdateDTO;
import com.rfl.product_service.mapper.CategoriaMapper;
import com.rfl.product_service.model.Categoria;
import com.rfl.product_service.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    public CategoriaController(CategoriaService categoriaService,
                                CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar()
                .stream()
                .map(categoriaMapper::toCategoriaResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscar(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return categoriaMapper.toCategoriaResponseDTO(categoria);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO criar(@RequestBody CategoriaCreateDTO dto) {
        Categoria categoria = categoriaService.criar(dto);
        return categoriaMapper.toCategoriaResponseDTO(categoria);
    }

    @PutMapping
    public CategoriaResponseDTO atualizar(@RequestBody CategoriaUpdateDTO dto) {
        Categoria categoria = categoriaService.atualizar(dto);
        return categoriaMapper.toCategoriaResponseDTO(categoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
    	categoriaService.deletar(id);
    }
}