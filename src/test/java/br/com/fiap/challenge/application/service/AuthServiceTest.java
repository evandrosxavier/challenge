package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.domain.entities.TipoUsuario;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.infrastructure.persistence.repository.UsuarioJpaRepository;
import br.com.fiap.challenge.infrastructure.security.JwtService;
import br.com.fiap.challenge.interfaces.dto.request.LoginRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.TokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock
    private UsuarioJpaRepository usuarioJpaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private Usuario usuarioAdmin;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        TipoUsuario tipoUsuario = new TipoUsuario(1L, "USER", null);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("joao.silva");
        usuario.setSenha("hash_senha_bcrypt");
        usuario.setTipoUsuario(tipoUsuario);

        TipoUsuario tipoAdmin = new TipoUsuario(2L, "ADMIN", null);
        usuarioAdmin = new Usuario();
        usuarioAdmin.setId(2L);
        usuarioAdmin.setLogin("admin");
        usuarioAdmin.setSenha("hash_senha_admin");
        usuarioAdmin.setTipoUsuario(tipoAdmin);
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Deve fazer login com sucesso e retornar token")
        void shouldLoginSuccessfullyAndReturnToken() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token_jwt_valido");

            TokenResponseDTO result = authService.login(request);

            assertNotNull(result);
            assertEquals("token_jwt_valido", result.token());
            verify(usuarioJpaRepository, times(1)).findByLoginIgnoreCase("joao.silva");
            verify(passwordEncoder, times(1)).matches("senha123", "hash_senha_bcrypt");
            verify(jwtService, times(1)).gerarToken(usuario);
        }

        @Test
        @DisplayName("Deve retornar TokenResponseDTO não null")
        void shouldReturnNonNullTokenResponse() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token_jwt_valido");

            TokenResponseDTO result = authService.login(request);

            assertNotNull(result);
            assertNotNull(result.token());
        }

        @Test
        @DisplayName("Deve fazer login com usuário ADMIN")
        void shouldLoginSuccessfullyWithAdminUser() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("admin");
            when(request.senha()).thenReturn("admin123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("admin"))
                .thenReturn(Optional.of(usuarioAdmin));
            when(passwordEncoder.matches("admin123", "hash_senha_admin"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuarioAdmin))
                .thenReturn("token_jwt_admin");

            TokenResponseDTO result = authService.login(request);

            assertNotNull(result);
            assertEquals("token_jwt_admin", result.token());
        }

        @Test
        @DisplayName("Deve fazer login com case-insensitive no login")
        void shouldLoginWithCaseInsensitiveLogin() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("JOAO.SILVA");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("JOAO.SILVA"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token_jwt_valido");

            TokenResponseDTO result = authService.login(request);

            assertNotNull(result);
            verify(usuarioJpaRepository, times(1)).findByLoginIgnoreCase("JOAO.SILVA");
        }
    }

    @Nested
    @DisplayName("Password Validation Tests")
    class PasswordValidationTests {

        @Test
        @DisplayName("Deve lançar exceção quando senha está incorreta")
        void shouldThrowExceptionWhenPasswordIsWrong() {
            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha_errada", "hash_senha_bcrypt"))
                .thenReturn(false);

            LoginRequestDTO wrongPasswordRequest = mock(LoginRequestDTO.class);
            when(wrongPasswordRequest.login()).thenReturn("joao.silva");
            when(wrongPasswordRequest.senha()).thenReturn("senha_errada");

            assertThrows(BusinessException.class, () -> authService.login(wrongPasswordRequest));
            verify(jwtService, never()).gerarToken(any());
        }

        @Test
        @DisplayName("Deve validar múltiplas tentativas com senha errada")
        void shouldValidateMultipleFailedAttempts() {
            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches(anyString(), eq("hash_senha_bcrypt")))
                .thenReturn(false);

            LoginRequestDTO wrongPasswordRequest = mock(LoginRequestDTO.class);
            when(wrongPasswordRequest.login()).thenReturn("joao.silva");
            when(wrongPasswordRequest.senha()).thenReturn("senha_errada");

            for (int i = 0; i < 3; i++) {
                assertThrows(BusinessException.class, () -> authService.login(wrongPasswordRequest));
            }

            verify(jwtService, never()).gerarToken(any());
        }
    }

    @Nested
    @DisplayName("JWT Token Generation Tests")
    class JwtTokenGenerationTests {

        @Test
        @DisplayName("Deve gerar token JWT válido após login bem-sucedido")
        void shouldGenerateValidJwtTokenAfterSuccessfulLogin() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");

            TokenResponseDTO result = authService.login(request);

            assertTrue(result.token().startsWith("eyJ"));
            verify(jwtService, times(1)).gerarToken(usuario);
        }

        @Test
        @DisplayName("Deve chamar jwtService com usuário correto")
        void shouldCallJwtServiceWithCorrectUser() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token");

            authService.login(request);

            verify(jwtService).gerarToken(usuario);
        }

        @Test
        @DisplayName("Deve gerar tokens diferentes para usuários diferentes")
        void shouldGenerateDifferentTokensForDifferentUsers() {
            LoginRequestDTO request1 = mock(LoginRequestDTO.class);
            when(request1.login()).thenReturn("joao.silva");
            when(request1.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token_joao");

            TokenResponseDTO result1 = authService.login(request1);

            LoginRequestDTO request2 = mock(LoginRequestDTO.class);
            when(request2.login()).thenReturn("admin");
            when(request2.senha()).thenReturn("admin123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("admin"))
                .thenReturn(Optional.of(usuarioAdmin));
            when(passwordEncoder.matches("admin123", "hash_senha_admin"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuarioAdmin))
                .thenReturn("token_admin");

            TokenResponseDTO result2 = authService.login(request2);

            assertNotEquals(result1.token(), result2.token());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Deve lidar com senha vazia")
        void shouldHandleEmptyPassword() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("", "hash_senha_bcrypt"))
                .thenReturn(false);

            assertThrows(BusinessException.class, () -> authService.login(request));
        }

        @Test
        @DisplayName("Deve lidar com login com caracteres especiais")
        void shouldHandleLoginWithSpecialCharacters() {
            Usuario usuarioEspecial = new Usuario();
            usuarioEspecial.setId(3L);
            usuarioEspecial.setLogin("joao.silva@empresa.com.br");
            usuarioEspecial.setSenha("hash_senha");
            usuarioEspecial.setTipoUsuario(new TipoUsuario(1L, "USER", null));

            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva@empresa.com.br");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva@empresa.com.br"))
                .thenReturn(Optional.of(usuarioEspecial));
            when(passwordEncoder.matches("senha123", "hash_senha"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuarioEspecial))
                .thenReturn("token");

            TokenResponseDTO result = authService.login(request);

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Deve validar fluxo completo de login")
        void shouldValidateCompleteLoginFlow() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token_jwt_valido");

            TokenResponseDTO result = authService.login(request);

            assertNotNull(result);
            assertNotNull(result.token());
            assertEquals("token_jwt_valido", result.token());

            verify(usuarioJpaRepository).findByLoginIgnoreCase("joao.silva");
            verify(passwordEncoder).matches("senha123", "hash_senha_bcrypt");
            verify(jwtService).gerarToken(usuario);
        }

        @Test
        @DisplayName("Deve validar que repository é consultado antes de validar senha")
        void shouldConsultRepositoryBeforeValidatingPassword() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token");

            authService.login(request);

            InOrder inOrder = inOrder(usuarioJpaRepository, passwordEncoder, jwtService);
            inOrder.verify(usuarioJpaRepository).findByLoginIgnoreCase("joao.silva");
            inOrder.verify(passwordEncoder).matches("senha123", "hash_senha_bcrypt");
            inOrder.verify(jwtService).gerarToken(usuario);
        }

        @Test
        @DisplayName("Deve validar ordem correta de execução em falha de autenticação")
        void shouldValidateExecutionOrderOnAuthenticationFailure() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha_errada");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha_errada", "hash_senha_bcrypt"))
                .thenReturn(false);

            assertThrows(BusinessException.class, () -> authService.login(request));

            InOrder inOrder = inOrder(usuarioJpaRepository, passwordEncoder);
            inOrder.verify(usuarioJpaRepository).findByLoginIgnoreCase("joao.silva");
            inOrder.verify(passwordEncoder).matches("senha_errada", "hash_senha_bcrypt");
            verify(jwtService, never()).gerarToken(any());
        }
    }

    @Nested
    @DisplayName("Verification Tests")
    class VerificationTests {

        @Test
        @DisplayName("Deve verificar que passwordEncoder é chamado com argumentos corretos")
        void shouldVerifyPasswordEncoderCalledWithCorrectArguments() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token");

            authService.login(request);

            verify(passwordEncoder).matches("senha123", "hash_senha_bcrypt");
        }

        @Test
        @DisplayName("Deve verificar que repositories e services são chamados corretamente")
        void shouldVerifyRepositoriesAndServicesAreCalledCorrectly() {
            LoginRequestDTO request = mock(LoginRequestDTO.class);
            when(request.login()).thenReturn("joao.silva");
            when(request.senha()).thenReturn("senha123");

            when(usuarioJpaRepository.findByLoginIgnoreCase("joao.silva"))
                .thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senha123", "hash_senha_bcrypt"))
                .thenReturn(true);
            when(jwtService.gerarToken(usuario))
                .thenReturn("token");

            authService.login(request);

            verify(usuarioJpaRepository, times(1)).findByLoginIgnoreCase("joao.silva");
            verify(passwordEncoder, times(1)).matches("senha123", "hash_senha_bcrypt");
            verify(jwtService, times(1)).gerarToken(usuario);
        }
    }
}






