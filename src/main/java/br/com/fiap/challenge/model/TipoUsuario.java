package br.com.fiap.challenge.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "usuarios")

@Entity
@Table (name = "tb_tipo_usuario")
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String descricao;

    @OneToMany(mappedBy = "tipoUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios = new ArrayList<>();

}
