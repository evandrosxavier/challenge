package br.com.fiap.challenge.service;

import br.com.fiap.challenge.dto.request.LoginRequestDTO;
import br.com.fiap.challenge.dto.response.TokenResponseDTO;
import br.com.fiap.challenge.exception.ResourceNotFoundException;
import br.com.fiap.challenge.model.Usuario;
import br.com.fiap.challenge.repository.UsuarioRepository;
import br.com.fiap.challenge.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository
                .findByLoginIgnoreCase(dto.login())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Login ou senha inválidos")
                );

        boolean senhaValida = passwordEncoder.matches(
                dto.senha(),
                usuario.getSenha()
        );

        if (!senhaValida) {
            throw new ResourceNotFoundException("Login ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario);

        return new TokenResponseDTO(token);
    }
}
