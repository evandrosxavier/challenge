package br.com.fiap.challenge.repository;

import br.com.fiap.challenge.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {

    Optional<TipoUsuario> findByDescricaoIgnoreCase (String descricao);
    
}
