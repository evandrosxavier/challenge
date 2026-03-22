package br.com.fiap.challenge.infrastructure.security;

import br.com.fiap.challenge.domain.entities.TipoUsuario;
import br.com.fiap.challenge.domain.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtService")
class JwtServiceTest {

    private JwtService jwtService;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        TipoUsuario tipoUsuario = new TipoUsuario(1L, "USER", null);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("joao.silva");
        usuario.setTipoUsuario(tipoUsuario);
    }

    @Test
    @DisplayName("Deve gerar token JWT válido")
    void shouldGenerateValidJwtToken() {
        String token = jwtService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
        assertEquals(3, token.split("\\.").length); // JWT tem 3 partes
    }

    @Test
    @DisplayName("Deve extrair login do token")
    void shouldExtractLoginFromToken() {
        String token = jwtService.gerarToken(usuario);
        String loginExtraido = jwtService.extrairLogin(token);

        assertEquals("joao.silva", loginExtraido);
    }

    @Test
    @DisplayName("Deve validar token válido como verdadeiro")
    void shouldValidateValidTokenAsTrue() {
        String token = jwtService.gerarToken(usuario);
        boolean isValido = jwtService.isTokenValido(token);

        assertTrue(isValido);
    }

    @Test
    @DisplayName("Deve validar token inválido como falso")
    void shouldValidateInvalidTokenAsFalse() {
        String tokenInvalido = "token.invalido.falso";
        boolean isValido = jwtService.isTokenValido(tokenInvalido);

        assertFalse(isValido);
    }

    @Test
    @DisplayName("Deve retornar login do token via getLogin")
    void shouldReturnLoginFromToken() {
        String token = jwtService.gerarToken(usuario);
        String login = jwtService.getLogin(token);

        assertEquals("joao.silva", login);
    }

    @Test
    @DisplayName("Deve retornar tipo de usuario do token")
    void shouldReturnUserTypeFromToken() {
        String token = jwtService.gerarToken(usuario);
        String tipo = jwtService.getTipo(token);

        assertNotNull(tipo);
        assertEquals("USER", tipo);
    }

    @Test
    @DisplayName("Deve gerar tokens diferentes para usuarios diferentes")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setLogin("maria.santos");
        usuario2.setTipoUsuario(new TipoUsuario(1L, "ADMIN", null));

        String token1 = jwtService.gerarToken(usuario);
        String token2 = jwtService.gerarToken(usuario2);

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Deve falhar ao extrair login de token inválido")
    void shouldFailToExtractLoginFromInvalidToken() {
        String tokenInvalido = "token.invalido.falso";

        assertThrows(Exception.class, () -> jwtService.extrairLogin(tokenInvalido));
    }

    @Test
    @DisplayName("Deve conter informações do usuario no token")
    void shouldContainUserInfoInToken() {
        String token = jwtService.gerarToken(usuario);

        String login = jwtService.getLogin(token);
        String tipo = jwtService.getTipo(token);

        assertEquals("joao.silva", login);
        assertEquals("USER", tipo);
    }
}

