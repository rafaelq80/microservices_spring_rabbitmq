package com.rfl.order_service.model;

import java.math.BigDecimal;

import com.rfl.order_service.enums.StatusItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_pedido_itens")
public class PedidoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "O pedido é obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pedido_id", nullable = false)
	private Pedido pedido;

	@NotNull(message = "O produto é obrigatório")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "produto_id", nullable = false)
	private ProdutoCache produto;

	@NotNull(message = "A quantidade é obrigatória")
	@Min(value = 1, message = "A quantidade deve ser maior que zero")
	@Column(nullable = false)
	private Integer quantidade;

	// Snapshot do preço no momento da compra
	@NotNull
	@Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal precoUnitario;

	// Calculado automaticamente: precoUnitario × quantidade
	@NotNull
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal subtotal;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusItem status = StatusItem.ATIVO;

	// ── Lógica de negócio ─────────────────────────────────────────────────────

	public void calcularSubtotal() {
		if (this.precoUnitario != null && this.quantidade != null) {
			this.subtotal = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
		}
	}

	// ── Getters e Setters ─────────────────────────────────────────────────────

	public Long getId() {
		return id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public ProdutoCache getProdutoCache() {
		return produto;
	}

	public void setProdutoCache(ProdutoCache produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
		calcularSubtotal();
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
		calcularSubtotal();
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public StatusItem getStatus() {
		return status;
	}

	public void setStatus(StatusItem status) {
		this.status = status;
	}
}