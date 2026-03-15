package br.com.fiap.challenge.service;


import br.com.fiap.challenge.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.exception.BusinessException;
import br.com.fiap.challenge.mapper.TipoUsuarioMapper;
import br.com.fiap.challenge.model.ErrorCode;
import br.com.fiap.challenge.model.TipoUsuario;
import br.com.fiap.challenge.repository.TipoUsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoUsuarioService {


    private TipoUsuarioRepository tipoUsuarioRepository;
    private TipoUsuarioMapper tipoUsuarioMapper;

    public TipoUsuarioService (TipoUsuarioRepository tipoUsuarioRepository, TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    public TipoUsuarioResponseDTO findById(Long id) {
       TipoUsuario tipoUsuario = this.tipoUsuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
       return this.tipoUsuarioMapper.toResponseDTO(tipoUsuario);

    }

    public List<TipoUsuarioResponseDTO> findAll() {
        return this.tipoUsuarioRepository.findAll().stream().map(tipoUsuarioMapper:: toResponseDTO).toList();
    }

    @Transactional
    public TipoUsuarioResponseDTO save(TipoUsuarioRequestDTO tipoUsuarioRequestDTO) {
        TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toEntity(tipoUsuarioRequestDTO);
        if (this.tipoUsuarioRepository.findByDescricaoIgnoreCase(tipoUsuario.getDescricao()).isPresent()) {
        throw new BusinessException(ErrorCode.USER_TYPE_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        tipoUsuario = tipoUsuarioRepository.save(tipoUsuario);
        return this.tipoUsuarioMapper.toResponseDTO(tipoUsuario);
    }


    @Transactional
    public TipoUsuarioResponseDTO update(Long id, TipoUsuarioUpdateDTO tipoUsuarioUpdateDTO) {
        TipoUsuario tipoUsuario = this.tipoUsuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (this.tipoUsuarioRepository.findByDescricaoIgnoreCase(tipoUsuarioUpdateDTO.descricao()).isPresent()){
            throw  new BusinessException(ErrorCode.USER_TYPE_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        this.tipoUsuarioMapper.updateEntityFromDTO(tipoUsuarioUpdateDTO, tipoUsuario);
        TipoUsuario tipoUsuarioSalvo = this.tipoUsuarioRepository.save(tipoUsuario);
        return this.tipoUsuarioMapper.toResponseDTO(tipoUsuarioSalvo);

    }

    @Transactional
    public void delete(Long id) {
        this.tipoUsuarioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.tipoUsuarioRepository.deleteById(id);
    }

}
