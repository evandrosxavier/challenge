package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.application.port.RestauranteRepositoryPort;
import br.com.fiap.challenge.domain.entities.Restaurante;
import br.com.fiap.challenge.infrastructure.persistence.repository.RestauranteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestauranteRepositoryAdapter implements RestauranteRepositoryPort {

    private final RestauranteJpaRepository restauranteJpaRepository;

    public RestauranteRepositoryAdapter(RestauranteJpaRepository restauranteJpaRepository) {
        this.restauranteJpaRepository = restauranteJpaRepository;
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return this.restauranteJpaRepository.findById(id);
    }

    @Override
    public List<Restaurante> findAll() {
        return this.restauranteJpaRepository.findAll();
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        return this.restauranteJpaRepository.save(restaurante);
    }

    @Override
    public void deleteById(Long id) {
        this.restauranteJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Restaurante> findByNomeIgnoreCase(String nome) {
        return this.restauranteJpaRepository.findByNomeIgnoreCase(nome);
    }

}


