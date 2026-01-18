package br.com.fiap.challenge.model;

import br.com.fiap.challenge.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioCreateRequestDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "enderecos")

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(nullable = false, length = 255)
    private String senha;


    @Column(name = "data_ultima_alteracao", nullable = false)
    private LocalDateTime dataDaUltimaAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoUsuario tipoUsuario;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // <-- ADICIONE AQUI
    private List<Endereco> enderecos = new ArrayList<>();

//
//    public Usuario(UsuarioCreateRequestDTO usuarioCreateRequestDTO) {
//        this.nome = usuarioCreateRequestDTO.nome();
//        this.email = usuarioCreateRequestDTO.email();
//        this.login = usuarioCreateRequestDTO.login();
//        this.senha = usuarioCreateRequestDTO.senha();
//        this.tipoUsuario = usuarioCreateRequestDTO.tipoUsuario();
//        this.dataDaUltimaAlteracao = LocalDateTime.now();
//
//
//        if (usuarioCreateRequestDTO.enderecos() != null) {
//            for (EnderecoRequestDTO enderecoDto : usuarioCreateRequestDTO.enderecos()) {
//                Endereco endereco = new Endereco(enderecoDto);
//                endereco.setUsuario(this);
//                enderecos.add(endereco);
//            }
//        }this.setEnderecos(enderecos);
//    }
//
//    }
//
}