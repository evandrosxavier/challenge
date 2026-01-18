package br.com.fiap.challenge.controller;

import br.com.fiap.challenge.dto.request.LoginRequestDTO;
import br.com.fiap.challenge.dto.response.TokenResponseDTO;
import br.com.fiap.challenge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }
}