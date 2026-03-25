package br.com.fiap.challenge.application.port;

import br.com.fiap.challenge.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Optional<Usuario> findById(Long id);

    List<Usuario> findAll();

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Optional<Usuario> findByLoginIgnoreCase(String login);

    boolean existsById(Long id);

}

