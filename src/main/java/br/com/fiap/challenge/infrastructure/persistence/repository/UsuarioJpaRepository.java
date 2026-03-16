package br.com.fiap.challenge.infrastructure.persistence.repository;

import br.com.fiap.challenge.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository <Usuario, Long> {

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Optional<Usuario> findByLoginIgnoreCase(String login);


}

