package br.com.fiap.challenge.application.port;

import br.com.fiap.challenge.domain.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface TipoUsuarioRepositoryPort {

    Optional<TipoUsuario> findById(Long id);

    List<TipoUsuario> findAll();

    TipoUsuario save(TipoUsuario tipoUsuario);

    void deleteById(Long id);

    Optional<TipoUsuario> findByDescricaoIgnoreCase(String descricao);

}

