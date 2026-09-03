package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.CategoriaDTO;
import com.receitarium.receitarium.entity.Categoria;
import com.receitarium.receitarium.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    @Transactional
    public CategoriaDTO salvar(CategoriaDTO dto) {

        Categoria categoria =
              Categoria.builder()
                        .nome(dto.getNome())
                        .build();

        Categoria salvo = categoriaRepository.salvar(categoria);

        return converterParaDTO(salvo);
    }


    public CategoriaDTO buscarPorId(Long id) {

        Categoria categoria = categoriaRepository.buscarPorId(id);

        if (categoria == null) {
            return null;
        }

        return converterParaDTO(categoria);
    }
    public List<CategoriaDTO> listarTodas() {

        return categoriaRepository.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }


    @Transactional
    public CategoriaDTO atualizar(CategoriaDTO dto, Long id) {

        Categoria existente = categoriaRepository.buscarPorId(id);

        if(existente == null){
            return null;
        }

        existente.setNome(dto.getNome());

        Categoria atualizado = categoriaRepository.atualizar(existente);

        return converterParaDTO(atualizado);
    }


    @Transactional
    public boolean excluir(Long id) {

        Categoria categoria = categoriaRepository.buscarPorId(id);

        if(categoria == null){
            return false;
        }

        categoriaRepository.excluir(id);
        return true;
    }


    private CategoriaDTO converterParaDTO(Categoria categoria){

        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }

}
