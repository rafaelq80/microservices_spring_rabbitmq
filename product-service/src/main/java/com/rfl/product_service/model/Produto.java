package com.rfl.product_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_produtos")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome do produto é obrigatório")
	@Size(max = 150)
	@Column(nullable = false, length = 150)
	private String nome;

	@Size(max = 500)
	@Column(length = 500)
	private String descricao;

	@NotNull(message = "O preço é obrigatório")
	@DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal preco;

	@NotNull(message = "A categoria é obrigatória")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@Column(nullable = false)
	private boolean ativo = true;

	@CreationTimestamp
	@Column(name = "criado_em", updatable = false)
	private LocalDateTime criadoEm;

	@UpdateTimestamp
	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;

	public Long getId() {
		return id;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public LocalDateTime getAtualizadoEm() {
		return atualizadoEm;
	}
}