package br.com.fiap.challenge.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "restaurante")

@Entity
@Table(name = "tb_enderecos_restaurante")
public class EnderecoRestaurante extends EnderecoBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante", nullable = false)
    @JsonBackReference
    private Restaurante restaurante;

}


