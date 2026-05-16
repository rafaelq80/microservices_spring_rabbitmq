package com.rfl.order_service.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.rfl.order_service.enums.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_pedidos")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O criador do pedido é obrigatório")
	@Column(name = "criado_por", nullable = false, length = 150)
	private String criadoPor;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusPedido status = StatusPedido.CRIADO;

	// Calculado automaticamente somando subtotais dos itens ativos
	@Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PedidoItem> itens = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "criado_em", updatable = false)
	private LocalDateTime criadoEm;

	// ── Lógica de negócio ─────────────────────────────────────────────────────

	public void recalcularTotal() {
		this.valorTotal = itens.stream()
				.filter(item -> item.getStatus() == com.rfl.order_service.enums.StatusItem.ATIVO)
				.map(PedidoItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public void adicionarItem(PedidoItem item) {
		item.setPedido(this);
		this.itens.add(item);
		recalcularTotal();
	}

	// ── Getters e Setters ─────────────────────────────────────────────────────

	public Long getId() {
		return id;
	}

	public String getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(String criadoPor) {
		this.criadoPor = criadoPor;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public List<PedidoItem> getItens() {
		return itens;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}
}