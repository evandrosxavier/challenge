package br.com.fiap.challenge.model;

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
@ToString(exclude = {"itensCardapio", "endereco"})

@Entity
@Table(name = "tb_restaurantes")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_endereco_restaurante", nullable = false)
    private EnderecoRestaurante endereco;

    @Column(name = "tipo_cozinha", nullable = false, length = 100)
    private String tipoCozinha;

    @Column(name = "horario_funcionamento", nullable = false, length = 100)
    private String horarioFuncionamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario donoRestaurante;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemCardapio> itensCardapio = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

}

