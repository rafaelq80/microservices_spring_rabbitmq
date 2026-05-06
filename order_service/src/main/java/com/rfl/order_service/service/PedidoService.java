package com.rfl.order_service.service;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import com.rfl.order_service.config.AppProperties;
import com.rfl.order_service.dto.PedidoCreateDTO;
import com.rfl.order_service.dto.PedidoResponseDTO;
import com.rfl.order_service.dto.PedidoUpdateDTO;
import com.rfl.order_service.event.PedidoAtualizadoEvent;
import com.rfl.order_service.event.PedidoCriadoEvent;
import com.rfl.order_service.exception.RecursoNaoEncontradoException;
import com.rfl.order_service.model.Pedido;
import com.rfl.order_service.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final AmqpTemplate amqpTemplate;
    private final AppProperties appProperties;
    
    // Construtor para injeção de dependências
    public PedidoService(
            PedidoRepository repository,
            AmqpTemplate amqpTemplate,
            AppProperties appProperties) {

        this.repository = repository;
        this.amqpTemplate = amqpTemplate;
        this.appProperties = appProperties;
    }

    // CREATE
    public PedidoResponseDTO criarPedido(PedidoCreateDTO dto, String usuario) {

        Pedido pedido = new Pedido();
        pedido.setProduto(dto.produto());
        pedido.setQuantidade(dto.quantidade());
        pedido.setStatus("CRIADO");
        pedido.setCriadoPor(usuario);

        Pedido salvo = repository.save(pedido);

        // Publica evento
        var evento = new PedidoCriadoEvent(
                salvo.getId(),
                salvo.getProduto(),
                salvo.getQuantidade(),
                salvo.getCriadoPor()
        );

        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                evento
        );

        return toResponseDTO(salvo);
    }

    // FIND BY ID
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido não encontrado")
                );

        return toResponseDTO(pedido);
    }

    // FIND ALL
    public List<PedidoResponseDTO> listarPedidos() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // FIND ALL BY CRIADOR
    public List<PedidoResponseDTO> listarPorCriador(String criador) {
        return repository.findAllByCriadoPor(criador)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // UPDATE
    public PedidoResponseDTO atualizarPedido(PedidoUpdateDTO dto) {

        Pedido pedido = repository.findById(dto.id())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido não encontrado")
                );

        pedido.setProduto(dto.produto());
        pedido.setQuantidade(dto.quantidade());
        pedido.setStatus(dto.status());

        Pedido salvo = repository.save(pedido);

        var evento = new PedidoAtualizadoEvent(
                salvo.getId(),
                salvo.getProduto(),
                salvo.getQuantidade(),
                salvo.getStatus()
        );

        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                evento
        );

        return toResponseDTO(salvo);
    }

    // DELETE
    public void deletarPedido(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pedido não encontrado");
        }
        repository.deleteById(id);
    }

    // Mapper interno
    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getProduto(),
                pedido.getQuantidade(),
                pedido.getStatus(),
                pedido.getCriadoPor(),
                pedido.getCriadoEm()
        );
    }
}