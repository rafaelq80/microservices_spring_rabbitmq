package com.rfl.order_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cache local de produtos no Order Service. Mantido sincronizado via eventos do
 * Product Service (RabbitMQ). Nunca é modificado diretamente pelo Order Service
 * — apenas pelo consumer.
 */
@Entity
@Table(name = "tb_produto_cache")
public class ProdutoCache {

	// ID espelha o ID do produto no Product Service
	@Id
	private Long id;

	@Column(nullable = false, length = 150)
	private String nome;

	@Column(length = 500)
	private String descricao;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal preco;

	@Column(name = "categoria_id", nullable = false)
	private Long categoriaId;

	@Column(name = "nome_categoria", nullable = false, length = 100)
	private String nomeCategoria;

	@Column(nullable = false)
	private boolean ativo = true;

	@Column(name = "sincronizado_em", nullable = false)
	private LocalDateTime sincronizadoEm;

	// ── Getters e Setters ─────────────────────────────────────────────────────

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Long getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getSincronizadoEm() {
		return sincronizadoEm;
	}

	public void setSincronizadoEm(LocalDateTime sincronizadoEm) {
		this.sincronizadoEm = sincronizadoEm;
	}
}