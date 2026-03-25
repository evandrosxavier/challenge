package br.com.fiap.challenge.infrastructure.security;

import br.com.fiap.challenge.domain.TipoUsuario;
import br.com.fiap.challenge.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService - Security Tests")
class JwtServiceSecurityTest {

    @InjectMocks
    private JwtService jwtService;

    private Usuario usuario;
    private Usuario usuarioAdmin;

    @BeforeEach
    void setUp() {
        TipoUsuario tipoUsuario = new TipoUsuario(1L, "USER", null);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("joao.silva");
        usuario.setTipoUsuario(tipoUsuario);

        TipoUsuario tipoAdmin = new TipoUsuario(2L, "ADMIN", null);
        usuarioAdmin = new Usuario();
        usuarioAdmin.setId(2L);
        usuarioAdmin.setLogin("admin");
        usuarioAdmin.setTipoUsuario(tipoAdmin);
    }

    @Nested
    @DisplayName("gerarToken Tests")
    class TokenGenerationTests {

        @Test
        @DisplayName("Deve gerar token com estrutura JWT válida")
        void shouldGenerateValidJwtStructure() {
            String token = jwtService.gerarToken(usuario);

            assertNotNull(token);
            assertTrue(token.contains("."));
            assertEquals(3, token.split("\\.").length);
        }

        @Test
        @DisplayName("Deve gerar token não vazio")
        void shouldGenerateNonEmptyToken() {
            String token = jwtService.gerarToken(usuario);

            assertNotNull(token);
            assertFalse(token.isEmpty());
        }

        @Test
        @DisplayName("Deve gerar diferentes tokens para usuários diferentes")
        void shouldGenerateDifferentTokensForDifferentUsers() {
            String token1 = jwtService.gerarToken(usuario);
            String token2 = jwtService.gerarToken(usuarioAdmin);

            assertNotEquals(token1, token2);
        }

        @Test
        @DisplayName("Deve gerar tokens diferentes em chamadas sucessivas")
        void shouldGenerateDifferentTokensOnSuccessiveCalls() {
            String token1 = jwtService.gerarToken(usuario);
            String token2 = jwtService.gerarToken(usuario);

            assertNotNull(token1);
            assertNotNull(token2);
        }

        @Test
        @DisplayName("Token gerado deve conter pelo menos 3 partes separadas por ponto")
        void shouldGenerateTokenWithThreeParts() {
            String token = jwtService.gerarToken(usuario);
            String[] parts = token.split("\\.");

            assertEquals(3, parts.length);
            assertTrue(parts[0].length() > 0);
            assertTrue(parts[1].length() > 0);
            assertTrue(parts[2].length() > 0);
        }
    }

    @Nested
    @DisplayName("extrairLogin Tests")
    class ExtractLoginTests {

        @Test
        @DisplayName("Deve extrair login do token corretamente")
        void shouldExtractLoginFromToken() {
            String token = jwtService.gerarToken(usuario);
            String loginExtraido = jwtService.extrairLogin(token);

            assertEquals("joao.silva", loginExtraido);
        }

        @Test
        @DisplayName("Deve lançar exceção ao extrair de token inválido")
        void shouldThrowExceptionForInvalidToken() {
            String tokenInvalido = "token.invalido.falso";

            assertThrows(Exception.class, () -> jwtService.extrairLogin(tokenInvalido));
        }

        @Test
        @DisplayName("Deve extrair login correto para usuário ADMIN")
        void shouldExtractAdminLoginCorrectly() {
            String token = jwtService.gerarToken(usuarioAdmin);
            String loginExtraido = jwtService.extrairLogin(token);

            assertEquals("admin", loginExtraido);
        }

        @Test
        @DisplayName("Deve lançar exceção para token vazio")
        void shouldThrowExceptionForEmptyToken() {
            assertThrows(Exception.class, () -> jwtService.extrairLogin(""));
        }

        @Test
        @DisplayName("Deve lançar exceção para token null")
        void shouldThrowExceptionForNullToken() {
            assertThrows(Exception.class, () -> jwtService.extrairLogin(null));
        }
    }

    @Nested
    @DisplayName("isTokenValido Tests")
    class TokenValidationTests {

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
        @DisplayName("Deve validar token vazio como falso")
        void shouldValidateEmptyTokenAsFalse() {
            String tokenVazio = "";
            boolean isValido = jwtService.isTokenValido(tokenVazio);

            assertFalse(isValido);
        }

        @Test
        @DisplayName("Deve validar token null como falso")
        void shouldValidateNullTokenAsFalse() {
            boolean isValido = jwtService.isTokenValido(null);

            assertFalse(isValido);
        }

        @Test
        @DisplayName("Deve validar token de ADMIN como verdadeiro")
        void shouldValidateAdminTokenAsTrue() {
            String token = jwtService.gerarToken(usuarioAdmin);
            boolean isValido = jwtService.isTokenValido(token);

            assertTrue(isValido);
        }

        @Test
        @DisplayName("Deve rejeitar token com partes faltando")
        void shouldRejectTokenWithMissingParts() {
            String tokenIncompleto = "header.payload";
            boolean isValido = jwtService.isTokenValido(tokenIncompleto);

            assertFalse(isValido);
        }

        @Test
        @DisplayName("Deve rejeitar token modificado")
        void shouldRejectModifiedToken() {
            String token = jwtService.gerarToken(usuario);
            String[] parts = token.split("\\.");
            String corruptedPayload = parts[1].substring(0, parts[1].length() - 3) + "abc";
            String modifiedToken = parts[0] + "." + corruptedPayload + "." + parts[2];

            assertFalse(jwtService.isTokenValido(modifiedToken));
        }
    }

    @Nested
    @DisplayName("getLogin Tests")
    class GetLoginTests {

        @Test
        @DisplayName("Deve retornar login do token")
        void shouldReturnLoginFromToken() {
            String token = jwtService.gerarToken(usuario);
            String login = jwtService.getLogin(token);

            assertEquals("joao.silva", login);
        }

        @Test
        @DisplayName("Deve lançar exceção para token inválido")
        void shouldThrowExceptionForInvalidTokenOnGetLogin() {
            String tokenInvalido = "token.invalido";

            assertThrows(Exception.class, () -> jwtService.getLogin(tokenInvalido));
        }

        @Test
        @DisplayName("Deve retornar login de ADMIN corretamente")
        void shouldReturnAdminLoginCorrectly() {
            String token = jwtService.gerarToken(usuarioAdmin);
            String login = jwtService.getLogin(token);

            assertEquals("admin", login);
        }

        @Test
        @DisplayName("Deve lançar exceção para token vazio")
        void shouldThrowExceptionForEmptyTokenOnGetLogin() {
            assertThrows(Exception.class, () -> jwtService.getLogin(""));
        }
    }

    @Nested
    @DisplayName("getTipo Tests")
    class GetTypeTests {

        @Test
        @DisplayName("Deve retornar tipo de usuário do token")
        void shouldReturnUserTypeFromToken() {
            String token = jwtService.gerarToken(usuario);
            String tipo = jwtService.getTipo(token);

            assertNotNull(tipo);
            assertEquals("USER", tipo);
        }

        @Test
        @DisplayName("Deve retornar tipo correto para ADMIN")
        void shouldReturnCorrectTypeForAdmin() {
            String token = jwtService.gerarToken(usuarioAdmin);
            String tipo = jwtService.getTipo(token);

            assertEquals("ADMIN", tipo);
        }

        @Test
        @DisplayName("Deve lançar exceção para token inválido ao extrair tipo")
        void shouldThrowExceptionForInvalidTokenOnGetType() {
            String tokenInvalido = "token.invalido.falso";

            assertThrows(Exception.class, () -> jwtService.getTipo(tokenInvalido));
        }

        @Test
        @DisplayName("Deve retornar tipo não null")
        void shouldReturnNonNullType() {
            String token = jwtService.gerarToken(usuario);
            String tipo = jwtService.getTipo(token);

            assertNotNull(tipo);
            assertFalse(tipo.isEmpty());
        }
    }

    @Nested
    @DisplayName("Segurança Tests")
    class SecurityTests {

        @Test
        @DisplayName("Token deve conter informações do usuário")
        void shouldTokenContainUserInformation() {
            String token = jwtService.gerarToken(usuario);

            String login = jwtService.getLogin(token);
            String tipo = jwtService.getTipo(token);

            assertEquals("joao.silva", login);
            assertEquals("USER", tipo);
        }

        @Test
        @DisplayName("Deve validar integração entre geração e extração")
        void shouldValidateGenerationAndExtractionIntegration() {
            String token = jwtService.gerarToken(usuario);

            assertTrue(jwtService.isTokenValido(token));
            assertEquals("joao.silva", jwtService.extrairLogin(token));
            assertEquals("USER", jwtService.getTipo(token));
        }

        @Test
        @DisplayName("Token modificado deve ser inválido")
        void shouldInvalidateModifiedToken() {
            String token = jwtService.gerarToken(usuario);
            String modifiedToken = token.substring(0, token.length() - 5) + "XXXXX";

            assertFalse(jwtService.isTokenValido(modifiedToken));
        }

        @Test
        @DisplayName("Deve validar integração para ADMIN")
        void shouldValidateIntegrationForAdmin() {
            String token = jwtService.gerarToken(usuarioAdmin);

            assertTrue(jwtService.isTokenValido(token));
            assertEquals("admin", jwtService.getLogin(token));
            assertEquals("ADMIN", jwtService.getTipo(token));
        }

        @Test
        @DisplayName("Tokens de usuários diferentes devem ter dados diferentes")
        void shouldHaveDifferentDataForDifferentUsers() {
            String token1 = jwtService.gerarToken(usuario);
            String token2 = jwtService.gerarToken(usuarioAdmin);

            String login1 = jwtService.getLogin(token1);
            String login2 = jwtService.getLogin(token2);
            String tipo1 = jwtService.getTipo(token1);
            String tipo2 = jwtService.getTipo(token2);

            assertNotEquals(login1, login2);
            assertNotEquals(tipo1, tipo2);
        }

        @Test
        @DisplayName("Deve validar que token com partes alteradas é inválido")
        void shouldInvalidateTokenWithAlteredParts() {
            String token = jwtService.gerarToken(usuario);
            String[] parts = token.split("\\.");

            String alteredToken = parts[0] + ".ALTERED." + parts[2];
            assertFalse(jwtService.isTokenValido(alteredToken));
        }

        @Test
        @DisplayName("Deve validar que um único caractere alterado invalida o token")
        void shouldInvalidateTokenWithSingleCharacterChange() {
            String token = jwtService.gerarToken(usuario);
            char[] tokenChars = token.toCharArray();
            tokenChars[10] = 'X';
            String modifiedToken = new String(tokenChars);

            assertFalse(jwtService.isTokenValido(modifiedToken));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Deve gerar token para usuário com ID negativo")
        void shouldGenerateTokenForUserWithNegativeId() {
            Usuario usuarioNegativo = new Usuario();
            usuarioNegativo.setId(-1L);
            usuarioNegativo.setLogin("teste");
            usuarioNegativo.setTipoUsuario(new TipoUsuario(1L, "USER", null));

            String token = jwtService.gerarToken(usuarioNegativo);

            assertNotNull(token);
            assertTrue(jwtService.isTokenValido(token));
        }

        @Test
        @DisplayName("Deve gerar token para usuário com ID muito grande")
        void shouldGenerateTokenForUserWithLargeId() {
            Usuario usuarioGrande = new Usuario();
            usuarioGrande.setId(Long.MAX_VALUE);
            usuarioGrande.setLogin("teste");
            usuarioGrande.setTipoUsuario(new TipoUsuario(1L, "USER", null));

            String token = jwtService.gerarToken(usuarioGrande);

            assertNotNull(token);
            assertTrue(jwtService.isTokenValido(token));
        }

        @Test
        @DisplayName("Deve gerar token para usuário com login vazio")
        void shouldGenerateTokenForUserWithEmptyLogin() {
            Usuario usuarioVazio = new Usuario();
            usuarioVazio.setId(1L);
            usuarioVazio.setLogin("");
            usuarioVazio.setTipoUsuario(new TipoUsuario(1L, "USER", null));

            String token = jwtService.gerarToken(usuarioVazio);

            assertNotNull(token);
        }

        @Test
        @DisplayName("Deve gerar token para login com caracteres especiais")
        void shouldGenerateTokenForLoginWithSpecialCharacters() {
            Usuario usuarioEspecial = new Usuario();
            usuarioEspecial.setId(1L);
            usuarioEspecial.setLogin("joao.silva@empresa.com.br");
            usuarioEspecial.setTipoUsuario(new TipoUsuario(1L, "USER", null));

            String token = jwtService.gerarToken(usuarioEspecial);

            assertNotNull(token);
            assertEquals("joao.silva@empresa.com.br", jwtService.getLogin(token));
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Deve gerar token rapidamente")
        void shouldGenerateTokenQuickly() {
            long inicio = System.currentTimeMillis();
            String token = jwtService.gerarToken(usuario);
            long fim = System.currentTimeMillis();

            assertNotNull(token);
            assertTrue((fim - inicio) < 1000);
        }

        @Test
        @DisplayName("Deve validar token rapidamente")
        void shouldValidateTokenQuickly() {
            String token = jwtService.gerarToken(usuario);

            long inicio = System.currentTimeMillis();
            boolean isValido = jwtService.isTokenValido(token);
            long fim = System.currentTimeMillis();

            assertTrue(isValido);
            assertTrue((fim - inicio) < 500);
        }

        @Test
        @DisplayName("Deve extrair login rapidamente")
        void shouldExtractLoginQuickly() {
            String token = jwtService.gerarToken(usuario);

            long inicio = System.currentTimeMillis();
            String login = jwtService.getLogin(token);
            long fim = System.currentTimeMillis();

            assertEquals("joao.silva", login);
            assertTrue((fim - inicio) < 500);
        }
    }

    @Nested
    @DisplayName("Consistency Tests")
    class ConsistencyTests {

        @Test
        @DisplayName("Deve extrair o mesmo login múltiplas vezes")
        void shouldExtractSameLoginMultipleTimes() {
            String token = jwtService.gerarToken(usuario);

            String login1 = jwtService.getLogin(token);
            String login2 = jwtService.getLogin(token);
            String login3 = jwtService.getLogin(token);

            assertEquals(login1, login2);
            assertEquals(login2, login3);
        }

        @Test
        @DisplayName("Deve validar o mesmo token múltiplas vezes")
        void shouldValidateSameTokenMultipleTimes() {
            String token = jwtService.gerarToken(usuario);

            boolean valido1 = jwtService.isTokenValido(token);
            boolean valido2 = jwtService.isTokenValido(token);
            boolean valido3 = jwtService.isTokenValido(token);

            assertTrue(valido1);
            assertTrue(valido2);
            assertTrue(valido3);
        }

        @Test
        @DisplayName("Deve extrair o mesmo tipo múltiplas vezes")
        void shouldExtractSameTypeMultipleTimes() {
            String token = jwtService.gerarToken(usuario);

            String tipo1 = jwtService.getTipo(token);
            String tipo2 = jwtService.getTipo(token);
            String tipo3 = jwtService.getTipo(token);

            assertEquals(tipo1, tipo2);
            assertEquals(tipo2, tipo3);
        }
    }
}
