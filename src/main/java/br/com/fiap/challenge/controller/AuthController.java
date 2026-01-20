package br.com.fiap.challenge.controller;

import br.com.fiap.challenge.dto.request.LoginRequestDTO;
import br.com.fiap.challenge.dto.response.TokenResponseDTO;
import br.com.fiap.challenge.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth" )
@Tag(name = "Autenticação", description = "Endpoint para autenticação de usuários e geração de token JWT.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Realizar Login",
            description = "Autentica um usuário com base em seu login e senha, retornando um token JWT em caso de sucesso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login bem-sucedido. O token JWT é retornado no corpo da resposta.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição Inválida. Pode ser por campos 'login' ou 'senha' ausentes.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não Autorizado. Credenciais (login e/ou senha) inválidas.",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
