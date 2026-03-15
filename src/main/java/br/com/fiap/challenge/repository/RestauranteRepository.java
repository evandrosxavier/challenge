package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    Optional<Restaurante> findByNomeIgnoreCase(String nome);

}

