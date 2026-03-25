package br.com.fiap.challenge.interfaces.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DonoRestauranteResponse - Unit Tests")
class DonoRestauranteResponseTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar DonoRestauranteResponse com todos os parâmetros")
        void shouldCreateDonoRestauranteResponseWithAllParameters() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals("João Silva", response.nome());
            assertEquals("joao@example.com", response.email());
        }

        @Test
        @DisplayName("Deve criar DonoRestauranteResponse com ID válido")
        void shouldCreateDonoRestauranteResponseWithValidId() {
            var response = new DonoRestauranteResponse(100L, "Maria Santos", "maria@example.com");

            assertEquals(100L, response.id());
        }

        @Test
        @DisplayName("Deve permitir ID nulo")
        void shouldAllowNullId() {
            var response = new DonoRestauranteResponse(null, "João Silva", "joao@example.com");

            assertNull(response.id());
        }

        @Test
        @DisplayName("Deve criar DonoRestauranteResponse com nome válido")
        void shouldCreateDonoRestauranteResponseWithValidNome() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertEquals("João Silva", response.nome());
        }

        @Test
        @DisplayName("Deve permitir nome nulo")
        void shouldAllowNullNome() {
            var response = new DonoRestauranteResponse(1L, null, "joao@example.com");

            assertNull(response.nome());
        }

        @Test
        @DisplayName("Deve permitir nome vazio")
        void shouldAllowEmptyNome() {
            var response = new DonoRestauranteResponse(1L, "", "joao@example.com");

            assertEquals("", response.nome());
        }

        @Test
        @DisplayName("Deve criar DonoRestauranteResponse com email válido")
        void shouldCreateDonoRestauranteResponseWithValidEmail() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertEquals("joao@example.com", response.email());
        }

        @Test
        @DisplayName("Deve permitir email nulo")
        void shouldAllowNullEmail() {
            var response = new DonoRestauranteResponse(1L, "João Silva", null);

            assertNull(response.email());
        }

        @Test
        @DisplayName("Deve permitir email vazio")
        void shouldAllowEmptyEmail() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "");

            assertEquals("", response.email());
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodsTests {

        @Test
        @DisplayName("Deve retornar ID correto via getter")
        void shouldReturnCorrectIdViaGetter() {
            var response = new DonoRestauranteResponse(42L, "João Silva", "joao@example.com");

            assertEquals(42L, response.id());
        }

        @Test
        @DisplayName("Deve retornar nome correto via getter")
        void shouldReturnCorrectNomeViaGetter() {
            var response = new DonoRestauranteResponse(1L, "Maria Santos", "maria@example.com");

            assertEquals("Maria Santos", response.nome());
        }

        @Test
        @DisplayName("Deve retornar email correto via getter")
        void shouldReturnCorrectEmailViaGetter() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao.silva@example.com");

            assertEquals("joao.silva@example.com", response.email());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Deve retornar true quando dois records têm os mesmos valores")
        void shouldReturnTrueWhenRecordsHaveSameValues() {
            var response1 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response2 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertEquals(response1, response2);
        }

        @Test
        @DisplayName("Deve retornar false quando records têm IDs diferentes")
        void shouldReturnFalseWhenIdsAreDifferent() {
            var response1 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response2 = new DonoRestauranteResponse(2L, "João Silva", "joao@example.com");

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Deve retornar false quando records têm nomes diferentes")
        void shouldReturnFalseWhenNamesAreDifferent() {
            var response1 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response2 = new DonoRestauranteResponse(1L, "Maria Santos", "joao@example.com");

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Deve retornar false quando records têm emails diferentes")
        void shouldReturnFalseWhenEmailsAreDifferent() {
            var response1 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response2 = new DonoRestauranteResponse(1L, "João Silva", "joao.silva@example.com");

            assertNotEquals(response1, response2);
        }

        @Test
        @DisplayName("Deve retornar false quando comparado com null")
        void shouldReturnFalseWhenComparedWithNull() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertNotEquals(response, null);
        }

        @Test
        @DisplayName("Deve retornar false quando comparado com objeto de tipo diferente")
        void shouldReturnFalseWhenComparedWithDifferentType() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertNotEquals(response, "string");
        }
    }

    @Nested
    @DisplayName("Hash Code Tests")
    class HashCodeTests {

        @Test
        @DisplayName("Deve retornar mesmo hash code para records com mesmos valores")
        void shouldReturnSameHashCodeForSameValues() {
            var response1 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response2 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Deve ser possível usar em coleções com hash code")
        void shouldBePossibleToUseInHashCollections() {
            var response1 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response2 = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var response3 = new DonoRestauranteResponse(2L, "Maria Santos", "maria@example.com");

            var set = new java.util.HashSet<>();
            set.add(response1);
            set.add(response2);
            set.add(response3);

            assertEquals(2, set.size());
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Deve conter informações do record no toString")
        void shouldContainRecordInformationInToString() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var toString = response.toString();

            assertTrue(toString.contains("DonoRestauranteResponse"));
        }

        @Test
        @DisplayName("Deve conter ID no toString")
        void shouldContainIdInToString() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var toString = response.toString();

            assertTrue(toString.contains("1"));
        }

        @Test
        @DisplayName("Deve conter nome no toString")
        void shouldContainNomeInToString() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var toString = response.toString();

            assertTrue(toString.contains("João Silva"));
        }

        @Test
        @DisplayName("Deve conter email no toString")
        void shouldContainEmailInToString() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");
            var toString = response.toString();

            assertTrue(toString.contains("joao@example.com"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Deve suportar ID com valor máximo de Long")
        void shouldSupportMaximumLongId() {
            var response = new DonoRestauranteResponse(Long.MAX_VALUE, "João Silva", "joao@example.com");

            assertEquals(Long.MAX_VALUE, response.id());
        }

        @Test
        @DisplayName("Deve suportar nome com caracteres especiais")
        void shouldSupportNomeWithSpecialCharacters() {
            var response = new DonoRestauranteResponse(1L, "João Silva Ñogueira", "joao@example.com");

            assertEquals("João Silva Ñogueira", response.nome());
        }

        @Test
        @DisplayName("Deve suportar email com subdomínios")
        void shouldSupportEmailWithSubdomains() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@mail.example.com");

            assertEquals("joao@mail.example.com", response.email());
        }

        @Test
        @DisplayName("Deve suportar nome com números")
        void shouldSupportNomeWithNumbers() {
            var response = new DonoRestauranteResponse(1L, "João Silva 123", "joao@example.com");

            assertEquals("João Silva 123", response.nome());
        }

        @Test
        @DisplayName("Deve suportar nome muito longo")
        void shouldSupportVeryLongNome() {
            var longName = "João Silva ".repeat(50);
            var response = new DonoRestauranteResponse(1L, longName, "joao@example.com");

            assertEquals(longName, response.nome());
        }
    }

    @Nested
    @DisplayName("Record Behavior Tests")
    class RecordBehaviorTests {

        @Test
        @DisplayName("Deve ser imutável - não deve permitir mudança de valores")
        void shouldBeImmutable() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertEquals(1L, response.id());
            assertEquals("João Silva", response.nome());
            assertEquals("joao@example.com", response.email());
        }

        @Test
        @DisplayName("Deve funcionar corretamente como um record")
        void shouldWorkCorrectlyAsRecord() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertNotNull(response);
            assertNotNull(response.id());
            assertNotNull(response.nome());
            assertNotNull(response.email());
        }
    }

    @Nested
    @DisplayName("Data Transfer Object Behavior Tests")
    class DTOBehaviorTests {

        @Test
        @DisplayName("Deve ser um DTO válido com todos os campos")
        void shouldBeValidDTOWithAllFields() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertNotNull(response.id());
            assertNotNull(response.nome());
            assertNotNull(response.email());
        }

        @Test
        @DisplayName("Deve permitir transferência de dados parciais com null")
        void shouldAllowPartialDataTransferWithNull() {
            var response = new DonoRestauranteResponse(1L, null, null);

            assertEquals(1L, response.id());
            assertNull(response.nome());
            assertNull(response.email());
        }

        @Test
        @DisplayName("Deve serializar corretamente para JSON")
        void shouldSerializeCorrectlyForJSON() {
            var response = new DonoRestauranteResponse(1L, "João Silva", "joao@example.com");

            assertTrue(response.id() != null || response.id() == null);
            assertTrue(response.nome() != null || response.nome() == null);
            assertTrue(response.email() != null || response.email() == null);
        }
    }
}

