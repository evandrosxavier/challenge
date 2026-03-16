package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.application.port.UsuarioRepositoryPort;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return this.usuarioJpaRepository.findById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return this.usuarioJpaRepository.findAll();
    }

    @Override
    public List<Usuario> findByNomeContainingIgnoreCase(String nome) {
        return this.usuarioJpaRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return this.usuarioJpaRepository.save(usuario);
    }

    @Override
    public void deleteById(Long id) {
        this.usuarioJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findByEmailIgnoreCase(String email) {
        return this.usuarioJpaRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Optional<Usuario> findByLoginIgnoreCase(String login) {
        return this.usuarioJpaRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public boolean existsById(Long id) {
        return this.usuarioJpaRepository.existsById(id);
    }

}


