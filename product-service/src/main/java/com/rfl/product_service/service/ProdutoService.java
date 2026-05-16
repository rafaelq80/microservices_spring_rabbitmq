package com.rfl.product_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rfl.product_service.dto.ProdutoCreateDTO;
import com.rfl.product_service.dto.ProdutoUpdateDTO;
import com.rfl.product_service.enums.TipoEventoProduto;
import com.rfl.product_service.exception.RecursoNaoEncontradoException;
import com.rfl.product_service.message.ProdutoEventPublisher;
import com.rfl.product_service.model.Categoria;
import com.rfl.product_service.model.Produto;
import com.rfl.product_service.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;
    private final ProdutoEventPublisher produtoEventPublisher;

    ProdutoService(ProdutoRepository produtoRepository,
                   CategoriaService categoriaService,
                   ProdutoEventPublisher produtoEventPublisher) {
    	
        this.produtoRepository = produtoRepository;
        this.categoriaService = categoriaService;
        this.produtoEventPublisher = produtoEventPublisher;
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Produto não encontrado")
                );
    }
    
    public List<Produto> listarAtivos() {
        return produtoRepository.findAllByAtivo(true);
    }
    
    public List<Produto> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findAllByCategoriaId(categoriaId);
    }

    @Transactional
    public Produto criar(ProdutoCreateDTO produtoDTO) {
    	
    	Categoria categoria = categoriaService.buscarPorId(produtoDTO.categoriaId());

        Produto novoProduto = new Produto();
        novoProduto.setNome(produtoDTO.nome());
        novoProduto.setDescricao(produtoDTO.descricao());
        novoProduto.setPreco(produtoDTO.preco());
        novoProduto.setCategoria(categoria);

        Produto produto = produtoRepository.save(novoProduto);
        produtoEventPublisher.publicar(produto, TipoEventoProduto.CRIADO);
        return produto;
    }

    @Transactional
    public Produto atualizar(ProdutoUpdateDTO produtoDTO) {
        
    	Produto produtoUpdate = this.buscarPorId(produtoDTO.id());

        if (produtoDTO.nome() != null)        
        	produtoUpdate.setNome(produtoDTO.nome());
        
        if (produtoDTO.descricao() != null)   
        	produtoUpdate.setDescricao(produtoDTO.descricao());
        
        if (produtoDTO.preco() != null)       
        	produtoUpdate.setPreco(produtoDTO.preco());
        
        if (produtoDTO.categoriaId() != null) 
        	produtoUpdate.setCategoria(categoriaService.buscarPorId(produtoDTO.categoriaId())
        );
        
        if (produtoDTO.ativo() != null)       
        	produtoUpdate.setAtivo(produtoDTO.ativo());

        Produto produto = produtoRepository.save(produtoUpdate);

       TipoEventoProduto tipo = !produto.isAtivo()
                ? TipoEventoProduto.DESATIVADO
                : TipoEventoProduto.ATUALIZADO;

       produtoEventPublisher.publicar(produto, tipo);
        
        return produto;
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

}