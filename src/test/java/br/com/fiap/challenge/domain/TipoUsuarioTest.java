package br.com.fiap.challenge.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TipoUsuario Entity")
class TipoUsuarioTest {

    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "USER", null);
    }

    @Test
    @DisplayName("Deve criar TipoUsuario com dados corretos")
    void shouldCreateTipoUsuarioWithCorrectData() {
        assertNotNull(tipoUsuario);
        assertEquals(1L, tipoUsuario.getId());
        assertEquals("USER", tipoUsuario.getDescricao());
    }

    @Test
    @DisplayName("Deve definir descricao")
    void shouldSetDescricao() {
        tipoUsuario.setDescricao("ADMIN");
        assertEquals("ADMIN", tipoUsuario.getDescricao());
    }

    @Test
    @DisplayName("Deve definir ID")
    void shouldSetId() {
        tipoUsuario.setId(2L);
        assertEquals(2L, tipoUsuario.getId());
    }

    @Test
    @DisplayName("Deve validar getters")
    void shouldValidateGetters() {
        Long id = tipoUsuario.getId();
        String descricao = tipoUsuario.getDescricao();

        assertNotNull(id);
        assertNotNull(descricao);
        assertEquals(1L, id);
        assertEquals("USER", descricao);
    }
}

