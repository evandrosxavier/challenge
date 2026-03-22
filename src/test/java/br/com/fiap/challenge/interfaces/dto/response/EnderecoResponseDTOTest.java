package br.com.fiap.challenge.interfaces.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EnderecoResponseDTO - Unit Tests")
class EnderecoResponseDTOTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar EnderecoResponseDTO com todos os parâmetros")
        void shouldCreateEnderecoResponseDTOWithAllParameters() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Brigadeiro Faria Lima",
                "1500",
                "Bela Vista",
                "Andar 10, Cj 101",
                "01452002",
                "São Paulo",
                "SP"
            );

            assertNotNull(endereco);
            assertEquals(101L, endereco.id());
            assertEquals("Avenida Brigadeiro Faria Lima", endereco.logradouro());
            assertEquals("1500", endereco.numero());
            assertEquals("Bela Vista", endereco.bairro());
            assertEquals("Andar 10, Cj 101", endereco.complemento());
            assertEquals("01452002", endereco.cep());
            assertEquals("São Paulo", endereco.cidade());
            assertEquals("SP", endereco.estado());
        }

        @Test
        @DisplayName("Deve permitir ID nulo")
        void shouldAllowNullId() {
            var endereco = new EnderecoResponseDTO(
                null,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNull(endereco.id());
        }

        @Test
        @DisplayName("Deve permitir complemento nulo")
        void shouldAllowNullComplemento() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                null,
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNull(endereco.complemento());
        }

        @Test
        @DisplayName("Deve permitir campos vazios")
        void shouldAllowEmptyFields() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            );

            assertEquals("", endereco.logradouro());
            assertEquals("", endereco.numero());
            assertEquals("", endereco.bairro());
            assertEquals("", endereco.complemento());
            assertEquals("", endereco.cep());
            assertEquals("", endereco.cidade());
            assertEquals("", endereco.estado());
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodsTests {

        @Test
        @DisplayName("Deve retornar ID correto")
        void shouldReturnCorrectId() {
            var endereco = new EnderecoResponseDTO(
                42L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(42L, endereco.id());
        }

        @Test
        @DisplayName("Deve retornar logradouro correto")
        void shouldReturnCorrectLogradouro() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Rua Augusta",
                "2000",
                "Centro",
                null,
                "01305100",
                "São Paulo",
                "SP"
            );

            assertEquals("Rua Augusta", endereco.logradouro());
        }

        @Test
        @DisplayName("Deve retornar número correto")
        void shouldReturnCorrectNumero() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "2500",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals("2500", endereco.numero());
        }

        @Test
        @DisplayName("Deve retornar bairro correto")
        void shouldReturnCorrectBairro() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Consolação",
                "Apto 101",
                "01311100",
                "São Paulo",
                "SP"
            );

            assertEquals("Consolação", endereco.bairro());
        }

        @Test
        @DisplayName("Deve retornar complemento correto")
        void shouldReturnCorrectComplemento() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Bloco A, Apto 501",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals("Bloco A, Apto 501", endereco.complemento());
        }

        @Test
        @DisplayName("Deve retornar CEP correto")
        void shouldReturnCorrectCep() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310999",
                "São Paulo",
                "SP"
            );

            assertEquals("01310999", endereco.cep());
        }

        @Test
        @DisplayName("Deve retornar cidade correta")
        void shouldReturnCorrectCidade() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Rua Augusta",
                "1000",
                "Centro",
                "Apto 101",
                "01305100",
                "Rio de Janeiro",
                "RJ"
            );

            assertEquals("Rio de Janeiro", endereco.cidade());
        }

        @Test
        @DisplayName("Deve retornar estado correto")
        void shouldReturnCorrectEstado() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Rua Augusta",
                "1000",
                "Centro",
                "Apto 101",
                "01305100",
                "Rio de Janeiro",
                "RJ"
            );

            assertEquals("RJ", endereco.estado());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Deve retornar true quando dois records têm os mesmos valores")
        void shouldReturnTrueWhenRecordsHaveSameValues() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando IDs são diferentes")
        void shouldReturnFalseWhenIdsDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                102L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando logradouros são diferentes")
        void shouldReturnFalseWhenLogradourosDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Rua Augusta",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando números são diferentes")
        void shouldReturnFalseWhenNumerosDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "2000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando bairros são diferentes")
        void shouldReturnFalseWhenBairrosDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Consolação",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando CEPs são diferentes")
        void shouldReturnFalseWhenCepsDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310999",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando cidades são diferentes")
        void shouldReturnFalseWhenCidadesDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "Rio de Janeiro",
                "SP"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando estados são diferentes")
        void shouldReturnFalseWhenEstadosDifferent() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "RJ"
            );

            assertNotEquals(endereco1, endereco2);
        }

        @Test
        @DisplayName("Deve retornar false quando comparado com null")
        void shouldReturnFalseWhenComparedWithNull() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco, null);
        }

        @Test
        @DisplayName("Deve retornar false quando comparado com objeto de tipo diferente")
        void shouldReturnFalseWhenComparedWithDifferentType() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotEquals(endereco, "string");
        }
    }

    @Nested
    @DisplayName("Hash Code Tests")
    class HashCodeTests {

        @Test
        @DisplayName("Deve retornar mesmo hash code para records com mesmos valores")
        void shouldReturnSameHashCodeForSameValues() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(endereco1.hashCode(), endereco2.hashCode());
        }

        @Test
        @DisplayName("Deve ser possível usar em HashSet")
        void shouldBePossibleToUseInHashSet() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco2 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var endereco3 = new EnderecoResponseDTO(
                102L,
                "Rua Augusta",
                "2000",
                "Centro",
                null,
                "01305100",
                "São Paulo",
                "SP"
            );

            var set = new java.util.HashSet<>();
            set.add(endereco1);
            set.add(endereco2);
            set.add(endereco3);

            assertEquals(2, set.size());
        }

        @Test
        @DisplayName("Deve ser possível usar como chave em HashMap")
        void shouldBePossibleToUseAsHashMapKey() {
            var endereco1 = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var map = new java.util.HashMap<>();
            map.put(endereco1, "São Paulo");

            assertEquals("São Paulo", map.get(endereco1));
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Deve conter informações do record no toString")
        void shouldContainRecordInformationInToString() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var toString = endereco.toString();

            assertTrue(toString.contains("EnderecoResponseDTO"));
        }

        @Test
        @DisplayName("Deve conter ID no toString")
        void shouldContainIdInToString() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var toString = endereco.toString();

            assertTrue(toString.contains("101"));
        }

        @Test
        @DisplayName("Deve conter logradouro no toString")
        void shouldContainLogradouroInToString() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var toString = endereco.toString();

            assertTrue(toString.contains("Avenida Paulista"));
        }

        @Test
        @DisplayName("Deve conter CEP no toString")
        void shouldContainCepInToString() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var toString = endereco.toString();

            assertTrue(toString.contains("01310100"));
        }

        @Test
        @DisplayName("Deve conter cidade no toString")
        void shouldContainCidadeInToString() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            var toString = endereco.toString();

            assertTrue(toString.contains("São Paulo"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Deve suportar ID com valor máximo de Long")
        void shouldSupportMaximumLongId() {
            var endereco = new EnderecoResponseDTO(
                Long.MAX_VALUE,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(Long.MAX_VALUE, endereco.id());
        }

        @Test
        @DisplayName("Deve suportar logradouro com caracteres especiais")
        void shouldSupportLogradouroWithSpecialCharacters() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida São João Câmara",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals("Avenida São João Câmara", endereco.logradouro());
        }

        @Test
        @DisplayName("Deve suportar número como texto com caracteres especiais")
        void shouldSupportNumeroWithSpecialCharacters() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000-A",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals("1000-A", endereco.numero());
        }

        @Test
        @DisplayName("Deve suportar complemento muito longo")
        void shouldSupportVeryLongComplemento() {
            var longComplemento = "Bloco A, Apto 501, Condomínio " + "X".repeat(100);
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                longComplemento,
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(longComplemento, endereco.complemento());
        }

        @Test
        @DisplayName("Deve suportar CEP no formato padrão brasileiro")
        void shouldSupportBrazilianCepFormat() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310-100",
                "São Paulo",
                "SP"
            );

            assertEquals("01310-100", endereco.cep());
        }

        @Test
        @DisplayName("Deve suportar cidade com mais de um nome")
        void shouldSupportCityWithMultipleNames() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "Rio de Janeiro do Sul",
                "RJ"
            );

            assertEquals("Rio de Janeiro do Sul", endereco.cidade());
        }

        @Test
        @DisplayName("Deve suportar estado como sigla de dois caracteres")
        void shouldSupportStateAsTwoCharacterAbbreviation() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(2, endereco.estado().length());
        }
    }

    @Nested
    @DisplayName("Record Behavior Tests")
    class RecordBehaviorTests {

        @Test
        @DisplayName("Deve ser imutável")
        void shouldBeImmutable() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(101L, endereco.id());
            assertEquals("Avenida Paulista", endereco.logradouro());
        }

        @Test
        @DisplayName("Deve funcionar como um record com todos os acessadores")
        void shouldWorkAsRecordWithAllAccessors() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotNull(endereco.id());
            assertNotNull(endereco.logradouro());
            assertNotNull(endereco.numero());
            assertNotNull(endereco.bairro());
            assertNotNull(endereco.complemento());
            assertNotNull(endereco.cep());
            assertNotNull(endereco.cidade());
            assertNotNull(endereco.estado());
        }
    }

    @Nested
    @DisplayName("Data Transfer Object Behavior Tests")
    class DTOBehaviorTests {

        @Test
        @DisplayName("Deve ser um DTO válido com todos os campos")
        void shouldBeValidDTOWithAllFields() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertNotNull(endereco.id());
            assertNotNull(endereco.logradouro());
            assertNotNull(endereco.numero());
            assertNotNull(endereco.bairro());
            assertNotNull(endereco.complemento());
            assertNotNull(endereco.cep());
            assertNotNull(endereco.cidade());
            assertNotNull(endereco.estado());
        }

        @Test
        @DisplayName("Deve permitir transferência de dados parciais com null")
        void shouldAllowPartialDataTransferWithNull() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                null,
                "01310100",
                "São Paulo",
                "SP"
            );

            assertEquals(101L, endereco.id());
            assertNull(endereco.complemento());
        }

        @Test
        @DisplayName("Deve representar corretamente um endereço completo")
        void shouldCorrectlyRepresentCompleteAddress() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 101",
                "01310100",
                "São Paulo",
                "SP"
            );

            assertTrue(endereco.logradouro().contains("Avenida"));
            assertTrue(endereco.bairro().contains("Bela Vista"));
            assertTrue(endereco.cidade().contains("São Paulo"));
            assertEquals("SP", endereco.estado());
        }
    }

    @Nested
    @DisplayName("Endereco Information Tests")
    class EnderecoInformationTests {

        @Test
        @DisplayName("Deve conter informações válidas de endereço de São Paulo")
        void shouldContainValidSaoPauloAddressInfo() {
            var endereco = new EnderecoResponseDTO(
                101L,
                "Avenida Brigadeiro Faria Lima",
                "1500",
                "Bela Vista",
                "Andar 10, Cj 101",
                "01452002",
                "São Paulo",
                "SP"
            );

            assertEquals("SP", endereco.estado());
            assertEquals("São Paulo", endereco.cidade());
            assertTrue(endereco.logradouro().contains("Avenida"));
        }

        @Test
        @DisplayName("Deve conter informações válidas de endereço do Rio de Janeiro")
        void shouldContainValidRioDeJaneiroAddressInfo() {
            var endereco = new EnderecoResponseDTO(
                102L,
                "Avenida Atlântica",
                "2000",
                "Copacabana",
                "Apto 1500",
                "22010020",
                "Rio de Janeiro",
                "RJ"
            );

            assertEquals("RJ", endereco.estado());
            assertEquals("Rio de Janeiro", endereco.cidade());
            assertEquals("Copacabana", endereco.bairro());
        }

        @Test
        @DisplayName("Deve conter informações válidas de endereço de Belo Horizonte")
        void shouldContainValidBeloHorizonteAddressInfo() {
            var endereco = new EnderecoResponseDTO(
                103L,
                "Avenida Getúlio Vargas",
                "3000",
                "Centro",
                "Sala 500",
                "30130100",
                "Belo Horizonte",
                "MG"
            );

            assertEquals("MG", endereco.estado());
            assertEquals("Belo Horizonte", endereco.cidade());
        }
    }
}


