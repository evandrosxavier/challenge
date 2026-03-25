package br.com.fiap.challenge.application.port;

import br.com.fiap.challenge.domain.ItemCardapio;

import java.util.List;
import java.util.Optional;

public interface ItemCardapioRepositoryPort {

    Optional<ItemCardapio> findById(Long id);

    List<ItemCardapio> findAll();

    ItemCardapio save(ItemCardapio itemCardapio);

    void deleteById(Long id);

    Optional<ItemCardapio> findByNomeIgnoreCase(String nome);

}

