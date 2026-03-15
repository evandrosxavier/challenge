package br.com.fiap.challenge.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(exclude = "restaurante")

@Entity
@Table(name = "tb_enderecos_restaurante")
public class EnderecoRestaurante extends EnderecoBase {

    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    @JsonBackReference
    private Restaurante restaurante;

}


