package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio , Long> {

    Optional<ItemCardapio> findByNomeIgnoreCase (String nome);


}
