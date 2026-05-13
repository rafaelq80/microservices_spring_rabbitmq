package com.rfl.product_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_categorias")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome da categoria é obrigatório")
	@Size(max = 100)
	@Column(nullable = false, length = 100, unique = true)
	private String nome;

	@Size(max = 255)
	@Column(length = 255)
	private String descricao;

	@Column(nullable = false)
	private boolean ativo = true;

	@OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
	private List<Produto> produtos;

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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public LocalDateTime getAtualizadoEm() {
		return atualizadoEm;
	}
}