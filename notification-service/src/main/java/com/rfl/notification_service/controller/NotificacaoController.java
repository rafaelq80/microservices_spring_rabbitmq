package com.rfl.notification_service.controller;

import com.rfl.notification_service.dto.NotificacaoResponseDTO;
import com.rfl.notification_service.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // FIND ALL BY DESTINATARIO
    @GetMapping("/destinatario/{destinatario}")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarPorDestinatario(
            @PathVariable String destinatario) {
        return ResponseEntity.ok(service.listarPorDestinatario(destinatario));
    }

}