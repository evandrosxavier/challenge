package br.com.fiap.challenge.infrastructure.persistence.repository;

import br.com.fiap.challenge.domain.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapio , Long> {

    Optional<ItemCardapio> findByNomeIgnoreCase (String nome);


}
