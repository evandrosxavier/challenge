package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.port.TipoUsuarioRepositoryPort;
import br.com.fiap.challenge.application.port.UsuarioRepositoryPort;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.interfaces.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.interfaces.mapper.EnderecoMapper;
import br.com.fiap.challenge.interfaces.mapper.UsuarioMapper;
import br.com.fiap.challenge.domain.EnderecoUsuario;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.domain.TipoUsuario;
import br.com.fiap.challenge.domain.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EnderecoMapper enderecoMapper;
    private final PasswordEncoder passwordEncoder;
    private final TipoUsuarioRepositoryPort tipoUsuarioRepository;

    public UsuarioService(
        UsuarioRepositoryPort usuarioRepository,
        UsuarioMapper usuarioMapper,
        EnderecoMapper enderecoMapper,
        PasswordEncoder passwordEncoder,
        TipoUsuarioRepositoryPort tipoUsuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.enderecoMapper = enderecoMapper;
        this.passwordEncoder = passwordEncoder;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> findAll() {
        return this.usuarioRepository.findAll()
            .stream()
            .map(usuarioMapper::toResponseDTO)
            .toList();
    }

    public List<UsuarioResponseDTO> findByNome(String nome) {
        return this.usuarioRepository.findByNomeContainingIgnoreCase(nome)
            .stream()
            .map(usuarioMapper::toResponseDTO)
            .toList();
    }

    @Transactional
    public UsuarioResponseDTO save(UsuarioCreateRequestDTO usuarioCreateRequestDTO) {

        this.usuarioRepository.findByEmailIgnoreCase(usuarioCreateRequestDTO.email())
            .ifPresent(u -> {
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
            });

        this.usuarioRepository.findByLoginIgnoreCase(usuarioCreateRequestDTO.login())
            .ifPresent(u -> {
                throw new BusinessException(ErrorCode.LOGIN_ALREADY_EXISTS, HttpStatus.CONFLICT);
            });

        TipoUsuario tipoUsuario = this.tipoUsuarioRepository.findById(usuarioCreateRequestDTO.tipoUsuario())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioMapper.toEntity(usuarioCreateRequestDTO);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setSenha(passwordEncoder.encode(usuarioCreateRequestDTO.senha()));
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());

        return usuarioMapper.toResponseDTO(this.usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO update(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, Long id) {

        Usuario usuario = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        usuarioRepository.findByEmailIgnoreCase(usuarioUpdateRequestDTO.email())
            .filter(u -> !u.getId().equals(id))
            .ifPresent(u -> {
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
            });

        TipoUsuario tipoUsuario = this.tipoUsuarioRepository.findById(usuarioUpdateRequestDTO.tipoUsuario())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

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

    @Transactional
    public void delete(Long id) {
        this.usuarioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.usuarioRepository.deleteById(id);

    }

    @Transactional
    public void updateSenha(UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO, Long id) {
        Usuario usuario = this.usuarioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(usuarioUpdateSenhaDTO.senhaAtual(), usuario.getSenha())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
        }

        usuario.setSenha(passwordEncoder.encode(usuarioUpdateSenhaDTO.novaSenha()));
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());
        this.usuarioRepository.save(usuario);

    }

}
