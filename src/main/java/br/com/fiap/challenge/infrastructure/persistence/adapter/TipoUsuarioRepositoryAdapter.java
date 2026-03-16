package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.application.port.TipoUsuarioRepositoryPort;
import br.com.fiap.challenge.domain.entities.TipoUsuario;
import br.com.fiap.challenge.infrastructure.persistence.repository.TipoUsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoUsuarioRepositoryAdapter implements TipoUsuarioRepositoryPort {

    private final TipoUsuarioJpaRepository tipoUsuarioJpaRepository;

    public TipoUsuarioRepositoryAdapter(TipoUsuarioJpaRepository tipoUsuarioJpaRepository) {
        this.tipoUsuarioJpaRepository = tipoUsuarioJpaRepository;
    }

    @Override
    public Optional<TipoUsuario> findById(Long id) {
        return this.tipoUsuarioJpaRepository.findById(id);
    }

    @Override
    public List<TipoUsuario> findAll() {
        return this.tipoUsuarioJpaRepository.findAll();
    }

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        return this.tipoUsuarioJpaRepository.save(tipoUsuario);
    }

    @Override
    public void deleteById(Long id) {
        this.tipoUsuarioJpaRepository.deleteById(id);
    }

    @Override
    public Optional<TipoUsuario> findByDescricaoIgnoreCase(String descricao) {
        return this.tipoUsuarioJpaRepository.findByDescricaoIgnoreCase(descricao);
    }

}


