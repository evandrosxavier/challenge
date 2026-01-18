package br.com.fiap.challenge.mapper;

import br.com.fiap.challenge.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.model.Usuario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = EnderecoMapper.class
)
public interface UsuarioMapper {

    Usuario toEntity(UsuarioCreateRequestDTO usuarioCreateRequestDTO);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "enderecos", ignore = true)
    void updateEntityFromDTO(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, @MappingTarget Usuario usuario);

    @AfterMapping
    default void linkEnderecos(@MappingTarget Usuario usuario) {
        if (usuario.getEnderecos() != null) {
            usuario.getEnderecos().forEach(e -> e.setUsuario(usuario));
        }
    }

}
