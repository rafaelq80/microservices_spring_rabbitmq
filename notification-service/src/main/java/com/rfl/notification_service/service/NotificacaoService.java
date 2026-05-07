package com.rfl.notification_service.service;

import com.rfl.notification_service.dto.NotificacaoResponseDTO;
import com.rfl.notification_service.enums.StatusNotificacao;
import com.rfl.notification_service.enums.TipoNotificacao;
import com.rfl.notification_service.event.PedidoAtualizadoEvent;
import com.rfl.notification_service.event.PedidoCriadoEvent;
import com.rfl.notification_service.exception.RecursoNaoEncontradoException;
import com.rfl.notification_service.model.Notificacao;
import com.rfl.notification_service.repository.NotificacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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

        Notificacao notificacao = new Notificacao();
        notificacao.setPedidoId(evento.pedidoId());
        notificacao.setDestinatario(evento.criadoPor());
        notificacao.setMensagem(
                "Pedido #" + evento.pedidoId() + " criado com sucesso. " +
                "Produto: " + evento.produto() + ", Quantidade: " + evento.quantidade()
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
        notificacao.setDestinatario("sistema");
        notificacao.setMensagem(
                "Pedido #" + evento.pedidoId() + " atualizado. " +
                "Produto: " + evento.produto() +
                ", Quantidade: " + evento.quantidade() +
                ", Status: " + evento.status()
        );
        notificacao.setTipo(TipoNotificacao.PEDIDO_ATUALIZADO);
        notificacao.setStatus(StatusNotificacao.PROCESSADA);

        repository.save(notificacao);
        log.info("[Notificacao] Registrada para pedidoId={}", evento.pedidoId());
    }

    // ── Consultas ─────────────────────────────────────────────────────────────

    public List<NotificacaoResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(NotificacaoResponseDTO::from)
                .toList();
    }

    public NotificacaoResponseDTO buscarPorId(Long id) {
        Notificacao notificacao = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Notificação não encontrada")
                );
        return NotificacaoResponseDTO.from(notificacao);
    }

    public List<NotificacaoResponseDTO> listarPorDestinatario(String destinatario) {
        return repository.findAllByDestinatario(destinatario)
                .stream()
                .map(NotificacaoResponseDTO::from)
                .toList();
    }

}