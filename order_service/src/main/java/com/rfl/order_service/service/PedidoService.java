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

/**
 * Serviço responsável pela lógica de negócio relacionada aos pedidos.
 *
 * <p>Gerencia as operações de criação, consulta, atualização e remoção de pedidos,
 * além de publicar eventos de domínio no RabbitMQ após cada operação de escrita,
 * permitindo que outros serviços reajam de forma assíncrona às mudanças de estado.</p>
 *
 * <p>Os eventos publicados são:</p>
 * <ul>
 *   <li>{@link PedidoCriadoEvent} — disparado ao criar um novo pedido.</li>
 *   <li>{@link PedidoAtualizadoEvent} — disparado ao atualizar um pedido existente.</li>
 * </ul>
 *
 * @see PedidoRepository
 * @see AppProperties
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AmqpTemplate amqpTemplate;
    private final AppProperties appProperties;

    /**
     * Construtor com injeção de dependências via construtor (recomendado pelo Spring).
     *
     * @param pedidoRepository repositório JPA para persistência de pedidos
     * @param amqpTemplate     template AMQP para publicação de mensagens no RabbitMQ
     * @param appProperties    propriedades da aplicação, incluindo configurações do RabbitMQ
     */
    public PedidoService(
            PedidoRepository pedidoRepository,
            AmqpTemplate amqpTemplate,
            AppProperties appProperties) {

        this.pedidoRepository = pedidoRepository;
        this.amqpTemplate = amqpTemplate;
        this.appProperties = appProperties;
    }

    /**
     * Retorna todos os pedidos cadastrados no sistema.
     *
     * @return lista de {@link PedidoResponseDTO} com todos os pedidos; lista vazia se não houver nenhum
     */
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Busca um pedido pelo seu identificador único.
     *
     * @param id identificador do pedido
     * @return {@link PedidoResponseDTO} com os dados do pedido encontrado
     * @throws RecursoNaoEncontradoException se nenhum pedido com o {@code id} informado existir
     */
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido não encontrado")
                );

        return toResponseDTO(pedido);
    }

    /**
     * Lista todos os pedidos criados por um determinado usuário.
     *
     * @param criador nome ou identificador do usuário criador dos pedidos
     * @return lista de {@link PedidoResponseDTO} dos pedidos do usuário; lista vazia se não houver nenhum
     */
    public List<PedidoResponseDTO> listarPorCriador(String criador) {
        return pedidoRepository.findAllByCriadoPor(criador)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Cria um novo pedido e publica um evento de domínio {@link PedidoCriadoEvent} no RabbitMQ.
     *
     * <p>O pedido é persistido com status inicial {@code "CRIADO"}, e tanto {@code criadoPor}
     * quanto {@code atualizadoPor} são definidos com o usuário autenticado no momento da criação.</p>
     *
     * @param dto     dados necessários para a criação do pedido
     * @param usuario identificador do usuário autenticado que está realizando a operação
     * @return {@link PedidoResponseDTO} com os dados do pedido recém-criado
     */
    public PedidoResponseDTO criarPedido(PedidoCreateDTO dto, String usuario) {

        // Monta a entidade com os dados do DTO e define os metadados de auditoria
        Pedido pedido = new Pedido();
        pedido.setProduto(dto.produto());
        pedido.setQuantidade(dto.quantidade());
        pedido.setStatus("CRIADO");
        pedido.setCriadoPor(usuario);
        pedido.setAtualizadoPor(usuario);

        Pedido salvo = pedidoRepository.save(pedido);

        // Publica evento para notificar outros serviços sobre a criação do pedido
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

    /**
     * Atualiza os dados de um pedido existente e publica um evento de domínio
     * {@link PedidoAtualizadoEvent} no RabbitMQ.
     *
     * <p>Todos os campos editáveis (produto, quantidade e status) são sobrescritos
     * com os valores fornecidos no DTO. O campo {@code atualizadoPor} é atualizado
     * com o usuário autenticado no momento da operação.</p>
     *
     * @param dto     dados de atualização do pedido, incluindo o {@code id} do pedido alvo
     * @param usuario identificador do usuário autenticado que está realizando a operação
     * @return {@link PedidoResponseDTO} com os dados atualizados do pedido
     * @throws RecursoNaoEncontradoException se nenhum pedido com o {@code id} informado existir
     */
    public PedidoResponseDTO atualizarPedido(PedidoUpdateDTO dto, String usuario) {

        Pedido pedido = pedidoRepository.findById(dto.id())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Pedido não encontrado")
                );

        // Aplica as alterações sobre a entidade gerenciada pelo JPA
        pedido.setProduto(dto.produto());
        pedido.setQuantidade(dto.quantidade());
        pedido.setStatus(dto.status());
        pedido.setAtualizadoPor(usuario);

        Pedido salvo = pedidoRepository.save(pedido);

        // Publica evento para notificar outros serviços sobre a atualização do pedido
        var evento = new PedidoAtualizadoEvent(
                salvo.getId(),
                salvo.getProduto(),
                salvo.getQuantidade(),
                salvo.getStatus(),
                salvo.getAtualizadoPor()
        );

        amqpTemplate.convertAndSend(
                appProperties.rabbitmq().exchange(),
                appProperties.rabbitmq().routingKey(),
                evento
        );

        return toResponseDTO(salvo);
    }

    /**
     * Remove um pedido pelo seu identificador único.
     *
     * <p>Nota: esta operação não publica evento no RabbitMQ. Caso outros serviços
     * precisem ser notificados sobre exclusões, considere adicionar um evento
     * {@code PedidoRemovidoEvent} futuramente.</p>
     *
     * @param id identificador do pedido a ser removido
     * @throws RecursoNaoEncontradoException se nenhum pedido com o {@code id} informado existir
     */
    public void deletarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // Métodos auxiliares (uso interno)
    // -------------------------------------------------------------------------

    /**
     * Converte uma entidade {@link Pedido} para seu respectivo DTO de resposta.
     *
     * <p>Centraliza o mapeamento para evitar duplicação e facilitar futuras
     * mudanças na estrutura de resposta da API.</p>
     *
     * @param pedido entidade a ser convertida
     * @return {@link PedidoResponseDTO} com os dados da entidade
     */
    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getProduto(),
                pedido.getQuantidade(),
                pedido.getStatus(),
                pedido.getCriadoPor(),
                pedido.getAtualizadoPor(),
                pedido.getCriadoEm()
        );
    }
}