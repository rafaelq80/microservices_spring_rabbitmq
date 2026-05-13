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

import com.rfl.product_service.dto.ProdutoCreateDTO;
import com.rfl.product_service.dto.ProdutoResponseDTO;
import com.rfl.product_service.dto.ProdutoUpdateDTO;
import com.rfl.product_service.mapper.ProdutoMapper;
import com.rfl.product_service.model.Produto;
import com.rfl.product_service.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
 
    private final ProdutoService produtoService;
    private final ProdutoMapper produtoMapper;
 
    ProdutoController(ProdutoService produtoService,
    				  ProdutoMapper produtoMapper) {
        this.produtoService = produtoService;
        this.produtoMapper = produtoMapper;
    }
 
    @GetMapping
    public List<ProdutoResponseDTO> listar() {
    	return produtoService.listar()
                .stream()
                .map(produtoMapper::toProdutoResponseDTO)
                .toList();
    }
    
    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable Long id) {
    	Produto produto = produtoService.buscarPorId(id);
        return produtoMapper.toProdutoResponseDTO(produto);
    }
    
    @GetMapping("/ativos")
    public List<ProdutoResponseDTO> listarAtivos() {
    	return produtoService.listarAtivos()
                .stream()
                .map(produtoMapper::toProdutoResponseDTO)
                .toList();
    }
  
    @GetMapping("/categoria/{categoriaId}")
    public List<ProdutoResponseDTO> listarPorCategoria(@PathVariable Long categoriaId) {
    	return produtoService.listarPorCategoria(categoriaId)
                .stream()
                .map(produtoMapper::toProdutoResponseDTO)
                .toList();
    }
 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criar(@Valid @RequestBody ProdutoCreateDTO produtoDTO) {
    	Produto produto = produtoService.criar(produtoDTO);
        return produtoMapper.toProdutoResponseDTO(produto);
    }
 
    @PutMapping
    public ProdutoResponseDTO atualizar(@Valid @RequestBody ProdutoUpdateDTO produtoDTO) {
    	Produto produto = produtoService.atualizar(produtoDTO);
        return produtoMapper.toProdutoResponseDTO(produto);
    }
 
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
    	produtoService.deletar(id);
    }
}
