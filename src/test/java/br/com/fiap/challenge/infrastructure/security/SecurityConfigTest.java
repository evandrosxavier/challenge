package br.com.fiap.challenge.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("SecurityConfig")
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve ter PasswordEncoder bean configurado")
    void shouldHavePasswordEncoderBeanConfigured() {
        assertNotNull(passwordEncoder);
    }

    @Test
    @DisplayName("Deve codificar senha corretamente")
    void shouldEncodePasswordCorrectly() {
        String rawPassword = "senha123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    @DisplayName("Deve rejeitar senha incorreta")
    void shouldRejectIncorrectPassword() {
        String rawPassword = "senha123";
        String wrongPassword = "senhaErrada";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }

    @Test
    @DisplayName("Deve retornar valores diferentes ao codificar mesma senha múltiplas vezes")
    void shouldReturnDifferentValuesWhenEncodingSamePasswordMultipleTimes() {
        String password = "senha123";
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);

        assertNotEquals(encoded1, encoded2);
        assertTrue(passwordEncoder.matches(password, encoded1));
        assertTrue(passwordEncoder.matches(password, encoded2));
    }

    @Test
    @DisplayName("Deve validar senha vazia")
    void shouldValidateEmptyPassword() {
        String emptyPassword = "";
        String encodedEmpty = passwordEncoder.encode(emptyPassword);

        assertTrue(passwordEncoder.matches(emptyPassword, encodedEmpty));
    }

    @Test
    @DisplayName("Deve validar senha com caracteres especiais")
    void shouldValidatePasswordWithSpecialCharacters() {
        String specialPassword = "Senh@123!#$";
        String encoded = passwordEncoder.encode(specialPassword);

        assertTrue(passwordEncoder.matches(specialPassword, encoded));
    }
}

