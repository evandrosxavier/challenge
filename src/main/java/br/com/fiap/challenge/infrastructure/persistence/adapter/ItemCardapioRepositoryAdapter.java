package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.application.port.ItemCardapioRepositoryPort;
import br.com.fiap.challenge.domain.ItemCardapio;
import br.com.fiap.challenge.infrastructure.persistence.repository.ItemCardapioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ItemCardapioRepositoryAdapter implements ItemCardapioRepositoryPort {

    private final ItemCardapioJpaRepository itemCardapioJpaRepository;

    public ItemCardapioRepositoryAdapter(ItemCardapioJpaRepository itemCardapioJpaRepository) {
        this.itemCardapioJpaRepository = itemCardapioJpaRepository;
    }

    @Override
    public Optional<ItemCardapio> findById(Long id) {
        return this.itemCardapioJpaRepository.findById(id);
    }

    @Override
    public List<ItemCardapio> findAll() {
        return this.itemCardapioJpaRepository.findAll();
    }

    @Override
    public ItemCardapio save(ItemCardapio itemCardapio) {
        return this.itemCardapioJpaRepository.save(itemCardapio);
    }

    @Override
    public void deleteById(Long id) {
        this.itemCardapioJpaRepository.deleteById(id);
    }

    @Override
    public Optional<ItemCardapio> findByNomeIgnoreCase(String nome) {
        return this.itemCardapioJpaRepository.findByNomeIgnoreCase(nome);
    }

}


