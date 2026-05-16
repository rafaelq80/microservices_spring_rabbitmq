package com.rfl.order_service.consumer;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rfl.order_service.event.ProdutoEvent;
import com.rfl.order_service.model.ProdutoCache;
import com.rfl.order_service.repository.ProdutoCacheRepository;

@Component
public class ProdutoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProdutoEventConsumer.class);

    private final ProdutoCacheRepository cacheRepository;

    public ProdutoEventConsumer(ProdutoCacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @RabbitListener(queues = "products.queue")
    public void onProdutoEvent(ProdutoEvent evento) {
        log.info("[Cache] ProdutoEvent recebido: id={} tipo={}", evento.id(), evento.tipo());

        // Busca o cache existente ou cria um novo — upsert
        ProdutoCache cache = cacheRepository.findById(evento.id())
                .orElseGet(ProdutoCache::new);

        cache.setId(evento.id());
        cache.setNome(evento.nome());
        cache.setDescricao(evento.descricao());
        cache.setPreco(evento.preco());
        cache.setCategoriaId(evento.categoriaId());
        cache.setNomeCategoria(evento.nomeCategoria());
        cache.setAtivo(evento.ativo());
        cache.setSincronizadoEm(LocalDateTime.now());

        cacheRepository.save(cache);

        log.info("[Cache] Produto id={} sincronizado — ativo={}", evento.id(), evento.ativo());
    }
}