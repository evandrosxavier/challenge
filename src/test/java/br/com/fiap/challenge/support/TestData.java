package br.com.fiap.challenge.support;

import br.com.fiap.challenge.domain.entities.*;
import br.com.fiap.challenge.interfaces.dto.request.*;
import br.com.fiap.challenge.interfaces.dto.response.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static TipoUsuario createTipoUsuario() {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(1L);
        tipo.setDescricao("USER");
        return tipo;
    }

    public static TipoUsuarioRequestDTO createTipoUsuarioRequest() {
        return new TipoUsuarioRequestDTO("ADMIN");
    }

    public static TipoUsuarioUpdateDTO createTipoUsuarioUpdate() {
        return new TipoUsuarioUpdateDTO("GERENTE");
    }

    public static EnderecoUsuario createEnderecoUsuario() {
        EnderecoUsuario endereco = new EnderecoUsuario();
        endereco.setId(1L);
        endereco.setLogradouro("Avenida Paulista");
        endereco.setNumero("1000");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Bela Vista");
        endereco.setCep("01310100");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        return endereco;
    }

    public static EnderecoRequestDTO createEnderecoRequest() {
        return new EnderecoRequestDTO(
            "Rua das Flores", "500", "Sala 10", "Centro",
            "12345678", "São Paulo", "SP"
        );
    }

    public static EnderecoRestaurante createEnderecoRestaurante() {
        EnderecoRestaurante endereco = new EnderecoRestaurante();
        endereco.setId(1L);
        endereco.setLogradouro("Avenida Paulista");
        endereco.setNumero("1000");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Bela Vista");
        endereco.setCep("01310100");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        return endereco;
    }

    public static Usuario createUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao.silva");
        usuario.setSenha("hash_senha_bcrypt");
        usuario.setTipoUsuario(createTipoUsuario());
        usuario.setEnderecos(new ArrayList<>());
        usuario.setDataDaUltimaAlteracao(LocalDateTime.now());
        return usuario;
    }

    public static UsuarioCreateRequestDTO createUsuarioRequest() {
        return new UsuarioCreateRequestDTO(
            "Maria Santos", "maria@email.com", "maria.santos", "senha123", 1L, List.of(createEnderecoRequest())
        );
    }

    public static UsuarioUpdateRequestDTO createUsuarioUpdate() {
        return new UsuarioUpdateRequestDTO(
            "Maria S.", "maria.new@email.com", "maria.santos.new", 1L, new ArrayList<>()
        );
    }

    public static UsuarioResponseDTO createUsuarioResponse() {
        return new UsuarioResponseDTO(
            1L, "João Silva", "joao@email.com",
            new TipoUsuarioResponseDTO(1L, "USER"),
            new ArrayList<>()
        );
    }

    public static Restaurante createRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria Bella");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("10:00-22:00");
        restaurante.getEnderecos().add(createEnderecoRestaurante());
        restaurante.setDataCriacao(LocalDateTime.now());
        return restaurante;
    }

    public static RestauranteRequest createRestauranteRequest() {
        return new RestauranteRequest(
            "Churrascaria X", "Brasileira", "11:00-23:00", 1L, List.of(createEnderecoRequest())
        );
    }

    public static RestauranteUpdateRequest createRestauranteUpdate() {
        return new RestauranteUpdateRequest(
            "Churrascaria X Updated", "Brasileira Premium", "11:00-23:30", 1L, List.of(createEnderecoRequest())
        );
    }

    public static RestauranteResponse createRestauranteResponse() {
        return new RestauranteResponse(
            1L, "Pizzaria Bella", "Italiana", "10:00-22:00",
            null, null, null
        );
    }

    public static ItemCardapio createItemCardapio() {
        ItemCardapio item = new ItemCardapio();
        item.setId(1L);
        item.setNome("Moqueca");
        item.setDescricao("Moqueca de peixe com leite de coco");
        item.setPreco(new BigDecimal("45.50"));
        item.setRestaurante(createRestaurante());
        return item;
    }

    public static ItemCardapioRequest createItemCardapioRequest() {
        return new ItemCardapioRequest(
            "Pastel", "Pastel de carne", new BigDecimal("15.00"), true, "foto.jpg", 1L
        );
    }

    public static ItemCardapioUpdateRequest createItemCardapioUpdate() {
        return new ItemCardapioUpdateRequest(
            "Pastel Premium", "Pastel de carne especial", new BigDecimal("18.00"), true, "foto2.jpg"
        );
    }

    public static ItemCardapioResponse createItemCardapioResponse() {
        return new ItemCardapioResponse(
            1L, "Moqueca", "Moqueca de peixe", new BigDecimal("45.50"), true, null
        );
    }

    public static LoginRequestDTO createLoginRequest() {
        return new LoginRequestDTO("joao.silva", "senha123");
    }

    public static LoginRequestDTO createInvalidLoginRequest() {
        return new LoginRequestDTO("invalid.user", "wrong.password");
    }
}
