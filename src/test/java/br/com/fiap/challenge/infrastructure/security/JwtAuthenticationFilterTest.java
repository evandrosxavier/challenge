package br.com.fiap.challenge.infrastructure.security;

import br.com.fiap.challenge.application.port.UsuarioRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);
    }

    @Nested
    @DisplayName("Testes do método doFilterInternal")
    class DoFilterInternalTests {

        @Test
        @DisplayName("Deve continuar a chain quando não há token")
        void shouldContinueChainWhenNoToken() throws ServletException, IOException {
            when(request.getHeader("Authorization")).thenReturn(null);

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Deve continuar a chain quando header não começa com Bearer")
        void shouldContinueChainWhenHeaderDoesNotStartWithBearer() throws ServletException, IOException {
            when(request.getHeader("Authorization")).thenReturn("Basic token123");

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Deve continuar a chain quando não há espaço em branco no header")
        void shouldContinueChainWhenNoSpaceInHeader() throws ServletException, IOException {
            when(request.getHeader("Authorization")).thenReturn("BearerToken");

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Deve processar token válido")
        void shouldProcessValidToken() throws ServletException, IOException {
            String token = "validToken123";
            String login = "joao.silva";

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtService.isTokenValido(token)).thenReturn(true);
            when(jwtService.getLogin(token)).thenReturn(login);

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService, times(1)).isTokenValido(token);
            verify(jwtService, times(1)).getLogin(token);
            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Deve continuar a chain para token inválido")
        void shouldContinueChainForInvalidToken() throws ServletException, IOException {
            String token = "invalidToken";

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtService.isTokenValido(token)).thenReturn(false);

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService, times(1)).isTokenValido(token);
            verify(filterChain, times(1)).doFilter(request, response);
        }
    }
}



