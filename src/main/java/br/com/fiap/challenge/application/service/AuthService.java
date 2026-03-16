package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.interfaces.dto.request.LoginRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.TokenResponseDTO;
import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.infrastructure.persistence.repository.UsuarioJpaRepository;
import br.com.fiap.challenge.infrastructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioJpaRepository usuarioJpaRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioJpaRepository
                .findByLoginIgnoreCase(dto.login())
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.INVALID_LOGIN_PASSWORD, HttpStatus.BAD_REQUEST)
                );

        boolean senhaValida = passwordEncoder.matches(
                dto.senha(),
                usuario.getSenha()
        );

        if (!senhaValida) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN_PASSWORD, HttpStatus.BAD_REQUEST);
        }

        String token = jwtService.gerarToken(usuario);

        return new TokenResponseDTO(token);
    }
}
