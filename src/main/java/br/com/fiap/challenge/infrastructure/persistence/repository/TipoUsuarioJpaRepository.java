package br.com.fiap.challenge.infrastructure.persistence.repository;

import br.com.fiap.challenge.domain.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoUsuarioJpaRepository extends JpaRepository<TipoUsuario, Long> {

    Optional<TipoUsuario> findByDescricaoIgnoreCase (String descricao);

}

