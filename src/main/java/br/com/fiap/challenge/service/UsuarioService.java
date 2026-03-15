package br.com.fiap.challenge.service;

import br.com.fiap.challenge.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.exception.BusinessException;
import br.com.fiap.challenge.mapper.EnderecoMapper;
import br.com.fiap.challenge.mapper.TipoUsuarioMapper;
import br.com.fiap.challenge.mapper.UsuarioMapper;
import br.com.fiap.challenge.model.EnderecoUsuario;
import br.com.fiap.challenge.model.ErrorCode;
import br.com.fiap.challenge.model.TipoUsuario;
import br.com.fiap.challenge.model.Usuario;
import br.com.fiap.challenge.repository.TipoUsuarioRepository;
import br.com.fiap.challenge.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UsuarioService {


    private UsuarioRepository usuarioRepository;
    private UsuarioMapper usuarioMapper;
    private EnderecoMapper enderecoMapper;
    private PasswordEncoder passwordEncoder;
    private TipoUsuarioRepository tipoUsuarioRepository;
    private TipoUsuarioMapper   tipoUsuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, EnderecoMapper enderecoMapper, PasswordEncoder passwordEncoder, TipoUsuarioRepository tipoUsuarioRepository, TipoUsuarioMapper tipoUsuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.enderecoMapper = enderecoMapper;
        this.passwordEncoder = passwordEncoder;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
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
                    throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
                });

        this.usuarioRepository.findByLoginIgnoreCase(usuarioCreateRequestDTO.login())
                .ifPresent(u -> {
                    throw new BusinessException(ErrorCode.LOGIN_ALREADY_EXISTS, HttpStatus.CONFLICT);
                });

        TipoUsuario tipoUsuario = this.tipoUsuarioRepository.findById(usuarioCreateRequestDTO.tipoUsuario()).orElseThrow(() -> new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioMapper.toEntity(usuarioCreateRequestDTO);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setSenha(passwordEncoder.encode(usuarioCreateRequestDTO.senha()));
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());

        return usuarioMapper.toResponseDTO(this.usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO update(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, Long id) {

        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        usuarioRepository.findByEmailIgnoreCase(usuarioUpdateRequestDTO.email())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new BusinessException(
                            ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT
                    );
                });

        TipoUsuario tipoUsuario = this.tipoUsuarioRepository.findById(usuarioUpdateRequestDTO.tipoUsuario()).orElseThrow(() -> new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

        usuarioMapper.updateEntityFromDTO(usuarioUpdateRequestDTO, usuario);
        usuario.setTipoUsuario(tipoUsuario);


        usuario.getEnderecos().clear();

        if (usuarioUpdateRequestDTO.enderecos() != null) {
            usuarioUpdateRequestDTO.enderecos().forEach(enderecoDTO -> {
                EnderecoUsuario endereco = enderecoMapper.toEnderecoUsuario(enderecoDTO);
                endereco.setUsuario(usuario);
                usuario.getEnderecos().add(endereco);
            });
        }

        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());

        Usuario usuarioSalvo = this.usuarioRepository.save(usuario);

        return usuarioMapper.toResponseDTO(usuarioSalvo);

    }

    public void delete(Long id) {
        this.usuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.usuarioRepository.deleteById(id);

    }

    public void updateSenha(UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO, Long id) {
        Usuario usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (!passwordEncoder.matches(usuarioUpdateSenhaDTO.senhaAtual(), usuario.getSenha())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
        }
        usuario.setSenha(passwordEncoder.encode(usuarioUpdateSenhaDTO.novaSenha()));
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());
        this.usuarioRepository.save(usuario);

    }

}
