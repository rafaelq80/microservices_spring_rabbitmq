package com.rfl.product_service.service;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rfl.product_service.config.AppProperties;
import com.rfl.product_service.dto.ProdutoCreateDTO;
import com.rfl.product_service.dto.ProdutoUpdateDTO;
import com.rfl.product_service.enums.TipoEventoProduto;
import com.rfl.product_service.event.ProdutoEvent;
import com.rfl.product_service.exception.RecursoNaoEncontradoException;
import com.rfl.product_service.model.Categoria;
import com.rfl.product_service.model.Produto;
import com.rfl.product_service.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;
    private final AmqpTemplate amqpTemplate;
    private final AppProperties appProperties;

    ProdutoService(ProdutoRepository produtoRepository,
                   CategoriaService categoriaService,
                   AmqpTemplate amqpTemplate,
                   AppProperties appProperties) {
    	
        this.produtoRepository = produtoRepository;
        this.categoriaService = categoriaService;
        this.amqpTemplate     = amqpTemplate;
        this.appProperties    = appProperties;
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

        Produto produto = new Produto();
        produto.setNome(produtoDTO.nome());
        produto.setDescricao(produtoDTO.descricao());
        produto.setPreco(produtoDTO.preco());
        produto.setCategoria(categoria);

        Produto salvo = produtoRepository.save(produto);
        publicarEvento(salvo, TipoEventoProduto.CRIADO);
        return produto;
    }

    @Transactional
    public Produto atualizar(ProdutoUpdateDTO produtoDTO) {
        
    	Produto produto = this.buscarPorId(produtoDTO.id());

        if (produtoDTO.nome() != null)        
        	produto.setNome(produtoDTO.nome());
        
        if (produtoDTO.descricao() != null)   
        	produto.setDescricao(produtoDTO.descricao());
        
        if (produtoDTO.preco() != null)       
        	produto.setPreco(produtoDTO.preco());
        
        if (produtoDTO.categoriaId() != null) 
        	produto.setCategoria(categoriaService.buscarPorId(produtoDTO.categoriaId())
        );
        
        if (produtoDTO.ativo() != null)       
        	produto.setAtivo(produtoDTO.ativo());

        Produto produtoAtualizado = produtoRepository.save(produto);

       TipoEventoProduto tipo = !produtoAtualizado.isAtivo()
                ? TipoEventoProduto.DESATIVADO
                : TipoEventoProduto.ATUALIZADO;

        publicarEvento(produtoAtualizado, tipo);
        
        return produto;
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void publicarEvento(Produto produto, TipoEventoProduto tipo) {
        var evento = new ProdutoEvent(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.isAtivo(),
                tipo
        );
        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                evento
        );
    }
}