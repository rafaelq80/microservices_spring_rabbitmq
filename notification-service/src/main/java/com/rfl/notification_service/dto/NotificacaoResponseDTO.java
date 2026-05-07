package com.rfl.notification_service.dto;

import com.rfl.notification_service.enums.StatusNotificacao;
import com.rfl.notification_service.enums.TipoNotificacao;
import com.rfl.notification_service.model.Notificacao;

import java.time.LocalDateTime;

public record NotificacaoResponseDTO(
        Long id,
        Long pedidoId,
        String destinatario,
        String mensagem,
        TipoNotificacao tipo,
        StatusNotificacao status,
        LocalDateTime criadoEm
) {

    public static NotificacaoResponseDTO from(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getPedidoId(),
                notificacao.getDestinatario(),
                notificacao.getMensagem(),
                notificacao.getTipo(),
                notificacao.getStatus(),
                notificacao.getCriadoEm()
        );
    }
}