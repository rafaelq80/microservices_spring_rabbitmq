package com.rfl.order_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rfl.order_service.dto.PedidoCreateDTO;
import com.rfl.order_service.dto.PedidoUpdateStatusDTO;
import com.rfl.order_service.exception.RecursoInativoException;
import com.rfl.order_service.exception.RecursoNaoEncontradoException;
import com.rfl.order_service.messaging.PedidoEventPublisher;
import com.rfl.order_service.model.Pedido;
import com.rfl.order_service.model.PedidoItem;
import com.rfl.order_service.model.ProdutoCache;
import com.rfl.order_service.repository.PedidoItemRepository;
import com.rfl.order_service.repository.PedidoRepository;
import com.rfl.order_service.repository.ProdutoCacheRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository itemRepository;
    private final ProdutoCacheRepository produtoCacheRepository;
    private final PedidoEventPublisher pedidoEventPublisher;

    public PedidoService(PedidoRepository pedidoRepository,
                         PedidoItemRepository itemRepository,
                         ProdutoCacheRepository produtoCacheRepository,
                         PedidoEventPublisher pedidoEventPublisher) {
        this.pedidoRepository       = pedidoRepository;
        this.itemRepository         = itemRepository;
        this.produtoCacheRepository = produtoCacheRepository;
        this.pedidoEventPublisher   = pedidoEventPublisher;
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    public List<Pedido> listarPorCriador(String criador) {
        return pedidoRepository.findAllByCriadoPor(criador);
    }

    @Transactional
    public Pedido criar(PedidoCreateDTO dto, String usuario) {

        Pedido pedido = new Pedido();
        pedido.setCriadoPor(usuario);

        for (PedidoCreateDTO.ItemDTO itemDTO : dto.itens()) {
            ProdutoCache produto = produtoCacheRepository.findById(itemDTO.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto não encontrado: " + itemDTO.produtoId()
                    ));

            if (!produto.isAtivo()) {
                throw new RecursoInativoException(  
                        "Produto inativo: " + produto.getNome()
                );
            }

            PedidoItem item = new PedidoItem();
            item.setProdutoCache(produto);
            item.setPrecoUnitario(produto.getPreco());
            item.setQuantidade(itemDTO.quantidade());
            pedido.adicionarItem(item);
        }

        Pedido salvo = pedidoRepository.save(pedido);
        pedidoEventPublisher.publicarCriado(salvo);
        return salvo;
    }

    @Transactional
    public Pedido atualizar(PedidoUpdateStatusDTO dto) {

        Pedido pedido = buscarPorId(dto.pedidoId());

        if (dto.status() != null) {
            pedido.setStatus(dto.status());
        }

        if (dto.itens() != null) {
            for (PedidoUpdateStatusDTO.ItemDTO itemDTO : dto.itens()) {
                PedidoItem item = itemRepository.findById(itemDTO.itemId())
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Item não encontrado: " + itemDTO.itemId()
                        ));
                item.setStatus(itemDTO.status());
                itemRepository.save(item);
            }
            pedido.recalcularTotal();
        }

        Pedido salvo = pedidoRepository.save(pedido);
        pedidoEventPublisher.publicarAtualizado(salvo);
        return salvo;
    }

    public void deletarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }
}