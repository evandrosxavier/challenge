package br.com.fiap.challenge.service;

import br.com.fiap.challenge.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.exception.EmailExistsException;
import br.com.fiap.challenge.exception.LoginExistsException;
import br.com.fiap.challenge.exception.ResourceNotFoundException;
import br.com.fiap.challenge.mapper.EnderecoMapper;
import br.com.fiap.challenge.mapper.UsuarioMapper;
import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.Usuario;
import br.com.fiap.challenge.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {


    private UsuarioRepository usuarioRepository;
    private UsuarioMapper usuarioMapper;
    private EnderecoMapper enderecoMapper;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, EnderecoMapper enderecoMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.enderecoMapper = enderecoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID " + id + " ."));
        return usuarioMapper.toResponseDTO(usuario);


    }

    public List<UsuarioResponseDTO> findAll() {
        return this.usuarioRepository.findAll().stream().map(usuarioMapper::toResponseDTO).toList();
    }

    public List<UsuarioResponseDTO> findByNome(String nome) {
        return this.usuarioRepository.findByNomeContainingIgnoreCase(nome).stream().map(usuarioMapper::toResponseDTO).toList();
    }

    public UsuarioResponseDTO save(UsuarioCreateRequestDTO usuarioCreateRequestDTO) {

        this.usuarioRepository.findByEmailIgnoreCase(usuarioCreateRequestDTO.email())
                .ifPresent(u -> {
                    throw new EmailExistsException("E-mail " + usuarioCreateRequestDTO.email() + " já cadastrado no sistema.");});

        this.usuarioRepository.findByLoginIgnoreCase(usuarioCreateRequestDTO.login())
                .ifPresent(u -> {
                    throw new LoginExistsException("Login " + usuarioCreateRequestDTO.login() + " já está em uso.");
                });

        Usuario usuario = usuarioMapper.toEntity(usuarioCreateRequestDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioCreateRequestDTO.senha()));
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());

        return usuarioMapper.toResponseDTO(this.usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO update(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, Long id) {

        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID " + id + " ."));

        usuarioRepository.findByEmailIgnoreCase(usuarioUpdateRequestDTO.email())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new EmailExistsException(
                            "E-mail " + usuarioUpdateRequestDTO.email() + " já cadastrado no sistema."
                    );
                });

        usuarioMapper.updateEntityFromDTO(usuarioUpdateRequestDTO, usuario);

        usuario.getEnderecos().clear();

        if (usuarioUpdateRequestDTO.enderecos() != null) {
            usuarioUpdateRequestDTO.enderecos().forEach(enderecoDTO -> {
                Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
                endereco.setUsuario(usuario);
                usuario.getEnderecos().add(endereco);
            });
        }

        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());

        Usuario usuarioSalvo = this.usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioSalvo);

    }

    public void delete(Long id) {
        this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID " + id + " ."));
        this.usuarioRepository.deleteById(id);

    }

    public void updateSenha(UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO, Long id) {
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID " + id + " ."));
        if (!passwordEncoder.matches(usuarioUpdateSenhaDTO.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentException("Senha atual inválida. Tente novamente!");
        }
        usuario.setSenha(passwordEncoder.encode(usuarioUpdateSenhaDTO.novaSenha()));
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());
        this.usuarioRepository.save(usuario);

    }

}
