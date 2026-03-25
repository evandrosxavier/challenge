package br.com.fiap.challenge.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Restaurante Entity")
class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria Bella");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("10:00-22:00");
    }

    @Test
    @DisplayName("Deve criar Restaurante com dados corretos")
    void shouldCreateRestauranteWithCorrectData() {
        assertNotNull(restaurante);
        assertEquals(1L, restaurante.getId());
        assertEquals("Pizzaria Bella", restaurante.getNome());
        assertEquals("Italiana", restaurante.getTipoCozinha());
    }

    @Test
    @DisplayName("Deve definir nome")
    void shouldSetNome() {
        restaurante.setNome("Churrascaria X");
        assertEquals("Churrascaria X", restaurante.getNome());
    }

    @Test
    @DisplayName("Deve definir tipo de cozinha")
    void shouldSetTipoCozinha() {
        restaurante.setTipoCozinha("Brasileira");
        assertEquals("Brasileira", restaurante.getTipoCozinha());
    }

    @Test
    @DisplayName("Deve definir horario funcionamento")
    void shouldSetHorarioFuncionamento() {
        restaurante.setHorarioFuncionamento("11:00-23:00");
        assertEquals("11:00-23:00", restaurante.getHorarioFuncionamento());
    }

    @Test
    @DisplayName("Deve validar getters")
    void shouldValidateGetters() {
        Long id = restaurante.getId();
        String nome = restaurante.getNome();
        String tipo = restaurante.getTipoCozinha();
        String horario = restaurante.getHorarioFuncionamento();

        assertNotNull(id);
        assertNotNull(nome);
        assertNotNull(tipo);
        assertNotNull(horario);
    }
}


