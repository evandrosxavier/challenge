package br.com.fiap.challenge.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ItemCardapio Entity")
class ItemCardapioTest {

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("Moqueca");
        itemCardapio.setDescricao("Moqueca de peixe");
        itemCardapio.setPreco(new BigDecimal("45.50"));
        itemCardapio.setDisponivelApenasNoRestaurante(true);
    }

    @Test
    @DisplayName("Deve criar ItemCardapio com dados corretos")
    void shouldCreateItemCardapioWithCorrectData() {
        assertNotNull(itemCardapio);
        assertEquals(1L, itemCardapio.getId());
        assertEquals("Moqueca", itemCardapio.getNome());
        assertEquals(new BigDecimal("45.50"), itemCardapio.getPreco());
    }

    @Test
    @DisplayName("Deve definir nome")
    void shouldSetNome() {
        itemCardapio.setNome("Pastel");
        assertEquals("Pastel", itemCardapio.getNome());
    }

    @Test
    @DisplayName("Deve definir preco")
    void shouldSetPreco() {
        itemCardapio.setPreco(new BigDecimal("25.00"));
        assertEquals(new BigDecimal("25.00"), itemCardapio.getPreco());
    }

    @Test
    @DisplayName("Deve definir disponibilidade")
    void shouldSetDisponibilidade() {
        itemCardapio.setDisponivelApenasNoRestaurante(false);
        assertFalse(itemCardapio.getDisponivelApenasNoRestaurante());
    }

    @Test
    @DisplayName("Deve validar getters")
    void shouldValidateGetters() {
        Long id = itemCardapio.getId();
        String nome = itemCardapio.getNome();
        String descricao = itemCardapio.getDescricao();
        BigDecimal preco = itemCardapio.getPreco();
        Boolean disponibilidade = itemCardapio.getDisponivelApenasNoRestaurante();

        assertNotNull(id);
        assertNotNull(nome);
        assertNotNull(descricao);
        assertNotNull(preco);
        assertTrue(disponibilidade);
    }

    @Test
    @DisplayName("Deve validar preco positivo")
    void shouldValidatePositivePrice() {
        BigDecimal preco = itemCardapio.getPreco();
        assertTrue(preco.compareTo(BigDecimal.ZERO) > 0);
    }
}


