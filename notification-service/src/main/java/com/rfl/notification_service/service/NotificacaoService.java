package com.rfl.notification_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rfl.notification_service.enums.StatusNotificacao;
import com.rfl.notification_service.enums.TipoNotificacao;
import com.rfl.notification_service.event.PedidoAtualizadoEvent;
import com.rfl.notification_service.event.PedidoCriadoEvent;
import com.rfl.notification_service.exception.RecursoNaoEncontradoException;
import com.rfl.notification_service.model.Notificacao;
import com.rfl.notification_service.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoService.class);

    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    // ── Chamados pelo consumer ────────────────────────────────────────────────

    public void processarPedidoCriado(PedidoCriadoEvent evento) {
        log.info("[Notificacao] Pedido criado recebido: pedidoId={}", evento.pedidoId());

        int totalItens = evento.itens().stream()
                .mapToInt(itens -> itens.quantidade())
                .sum();

        Notificacao notificacao = new Notificacao();
        notificacao.setPedidoId(evento.pedidoId());
        notificacao.setDestinatario(evento.criadoPor());
        notificacao.setMensagem(
                "Pedido #" + evento.pedidoId() + " criado com sucesso. " +
                totalItens + " item(ns), total: R$ " + evento.valorTotal()
        );
        notificacao.setTipo(TipoNotificacao.PEDIDO_CRIADO);
        notificacao.setStatus(StatusNotificacao.PROCESSADA);

        repository.save(notificacao);
        log.info("[Notificacao] Registrada para pedidoId={}", evento.pedidoId());
    }

    public void processarPedidoAtualizado(PedidoAtualizadoEvent evento) {
        log.info("[Notificacao] Pedido atualizado recebido: pedidoId={}", evento.pedidoId());

        Notificacao notificacao = new Notificacao();
        notificacao.setPedidoId(evento.pedidoId());
        notificacao.setDestinatario(evento.criadoPor());
        notificacao.setMensagem(
                "Pedido #" + evento.pedidoId() + " atualizado. " +
                "Status: " + evento.novoStatus() + ", total: R$ " + evento.valorTotal()
        );
        notificacao.setTipo(TipoNotificacao.PEDIDO_ATUALIZADO);
        notificacao.setStatus(StatusNotificacao.PROCESSADA);

        repository.save(notificacao);
        log.info("[Notificacao] Registrada para pedidoId={}", evento.pedidoId());
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

    public List<Notificacao> listar() {
        return repository.findAll();
    }

    public Notificacao buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificação não encontrada"));
    }

    public List<Notificacao> listarPorDestinatario(String destinatario) {
        return repository.findAllByDestinatario(destinatario);
    }
}