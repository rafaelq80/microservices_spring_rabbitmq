package com.rfl.notification_service.mapper;

import org.springframework.stereotype.Component;

import com.rfl.notification_service.dto.NotificacaoResponseDTO;
import com.rfl.notification_service.model.Notificacao;

@Component
public class NotificacaoMapper {

    public NotificacaoResponseDTO toNotificacaoResponseDTO(Notificacao notificacao) {
        return NotificacaoResponseDTO.from(notificacao);
    }
}