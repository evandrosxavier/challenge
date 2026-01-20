package br.com.fiap.challenge.config;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.TipoUsuario;
import br.com.fiap.challenge.model.Usuario;
import br.com.fiap.challenge.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Configuration
@Profile("test")
public class TestConfig implements org.springframework.boot.CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {

        Usuario usuario = new Usuario(
                null,
                "João Silva",
                "joao@email.com",
                "joao.silva",
                "senha123",
                LocalDateTime.now(),
                TipoUsuario.CLIENTE,
                new ArrayList<>()
        );

        Endereco endereco = new Endereco(
                null,
                "Rua das Flores",
                "123",
                "Bloco A",
                "Centro",
                "São Paulo",
                "SP",
                "01010-000",
                usuario
        );

        usuario.getEnderecos().add(endereco);

        usuarioRepository.save(usuario);

        var resultados = usuarioRepository.findAll();

        }
    }




