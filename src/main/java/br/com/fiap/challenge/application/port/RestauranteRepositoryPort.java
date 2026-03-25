package br.com.fiap.challenge.application.port;

import br.com.fiap.challenge.domain.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepositoryPort {

    Optional<Restaurante> findById(Long id);

    List<Restaurante> findAll();

    Restaurante save(Restaurante restaurante);

    void deleteById(Long id);

    Optional<Restaurante> findByNomeIgnoreCase(String nome);

}

