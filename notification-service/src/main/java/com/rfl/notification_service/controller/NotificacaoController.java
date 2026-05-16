package com.rfl.notification_service.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rfl.notification_service.dto.NotificacaoResponseDTO;
import com.rfl.notification_service.mapper.NotificacaoMapper;
import com.rfl.notification_service.service.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final NotificacaoMapper notificacaoMapper;

    NotificacaoController(NotificacaoService notificacaoService,
                          NotificacaoMapper notificacaoMapper) {
        this.notificacaoService = notificacaoService;
        this.notificacaoMapper  = notificacaoMapper;
    }

    @GetMapping
    public List<NotificacaoResponseDTO> listar() {
        return notificacaoService.listar()
                .stream()
                .map(notificacaoMapper::toNotificacaoResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public NotificacaoResponseDTO buscarPorId(@PathVariable Long id) {
        return notificacaoMapper.toNotificacaoResponseDTO(notificacaoService.buscarPorId(id));
    }

    @GetMapping("/criador")
    public List<NotificacaoResponseDTO> listarMinhas(Authentication auth) {
        return notificacaoService.listarPorDestinatario(auth.getName())
                .stream()
                .map(notificacaoMapper::toNotificacaoResponseDTO)
                .toList();
    }
}