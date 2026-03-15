package br.com.fiap.challenge.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString

@Entity
@Table(name = "tb_itens_cardapio")
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "disponivel_apenas_no_restaurante", nullable = false)
    private Boolean disponivelApenasNoRestaurante;

    @Column(name = "caminho_foto", length = 100)
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante", nullable = false)
    @JsonBackReference
    private Restaurante restaurante;

}

