package br.com.fiap.challenge.infrastructure.persistence.repository;

import br.com.fiap.challenge.domain.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestauranteJpaRepository extends JpaRepository<Restaurante, Long> {

    Optional<Restaurante> findByNomeIgnoreCase(String nome);

}

