package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.interfaces.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.domain.entities.Usuario;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {EnderecoMapper.class, TipoUsuarioMapper.class}
)
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataDaUltimaAlteracao", ignore = true)
    @Mapping(target = "tipoUsuario", ignore = true)
    Usuario toEntity(UsuarioCreateRequestDTO usuarioCreateRequestDTO);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "dataDaUltimaAlteracao", ignore = true)
    @Mapping(target = "enderecos", ignore = true)
    @Mapping(target = "tipoUsuario", ignore = true)
    void updateEntityFromDTO(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, @MappingTarget Usuario usuario);

    @AfterMapping
    default void linkEnderecos(@MappingTarget Usuario usuario) {
        if (usuario.getEnderecos() != null) {
            usuario.getEnderecos().forEach(e -> e.setUsuario(usuario));
        }
    }

}

