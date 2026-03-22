package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.AuthService;
import br.com.fiap.challenge.interfaces.dto.response.TokenResponseDTO;
import br.com.fiap.challenge.support.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController - Integration Tests")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Nested
    @DisplayName("POST /auth/login")
    class LoginTests {

        @Test
        @DisplayName("Deve fazer login com sucesso e retornar token")
        void shouldLoginSuccessfullyAndReturnToken() throws Exception {
            var request = TestData.createLoginRequest();
            var response = new TokenResponseDTO("token_jwt_valido_12345");
            when(authService.login(any())).thenReturn(response);

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token_jwt_valido_12345"));

            verify(authService, times(1)).login(any());
        }

        @Test
        @DisplayName("Deve retornar 400 com credenciais inválidas")
        void shouldReturnStatus400WithInvalidCredentials() throws Exception {
            var request = TestData.createInvalidLoginRequest();
            when(authService.login(any()))
                .thenThrow(new BusinessException(ErrorCode.INVALID_LOGIN_PASSWORD, HttpStatus.UNAUTHORIZED));

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve validar que token não está vazio")
        void shouldValidateThatTokenIsNotEmpty() throws Exception {
            var request = TestData.createLoginRequest();
            var response = new TokenResponseDTO("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
            when(authService.login(any())).thenReturn(response);

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("Deve retornar 400 quando campos estão vazios")
        void shouldReturnStatus400WhenFieldsAreEmpty() throws Exception {
            var request = new br.com.fiap.challenge.interfaces.dto.request.LoginRequestDTO("", "");

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isBadRequest());
        }
    }
}

