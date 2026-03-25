package br.com.fiap.challenge.infrastructure.persistence.repository;

import br.com.fiap.challenge.domain.EnderecoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRestauranteJpaRepository extends JpaRepository<EnderecoRestaurante, Long> {
}

