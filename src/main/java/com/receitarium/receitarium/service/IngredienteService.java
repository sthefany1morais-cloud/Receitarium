package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.IngredienteDTO;
import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.entity.Ingrediente;
import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.repository.IngredienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@AllArgsConstructor
@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;


    @Transactional
    public IngredienteDTO salvar(IngredienteDTO dto) {

        Ingrediente existente = ingredienteRepository.buscarPorNome(dto.getNome());

        if(existente != null){
            throw new RuntimeException("Ingrediente já existente");
        }

        Ingrediente ingrediente = Ingrediente.builder()
                .nome(dto.getNome())
                .unidadeMedida(
                        converterUnidadeDeMedidaParaEntidade(dto.getUnidadeMedida())
                )
                .build();

        Ingrediente salvo = ingredienteRepository.salvar(ingrediente);
        return converterParaDTO(salvo);
    }


    public IngredienteDTO buscarPorId(Long id) {

        Ingrediente ingrediente = ingredienteRepository.buscarPorId(id);

        if (ingrediente == null) {
            return null;
        }

        return converterParaDTO(ingrediente);
    }

    public List<IngredienteDTO> listarTodas() {

        return ingredienteRepository.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }


    @Transactional
    public IngredienteDTO atualizar(IngredienteDTO dto, Long id) {

        Ingrediente existente = ingredienteRepository.buscarPorId(id);

        if (existente == null) {
            return null;
        }

        Ingrediente ingredienteHomonimo = ingredienteRepository.buscarPorNome(dto.getNome());

        if(ingredienteHomonimo != null && !ingredienteHomonimo.getId().equals(id)){
            throw new RuntimeException("Ingrediente já existente");
        }

        existente.setNome(dto.getNome());
        existente.setUnidadeMedida(
                converterUnidadeDeMedidaParaEntidade(dto.getUnidadeMedida())
        );

        Ingrediente atualizado = ingredienteRepository.atualizar(existente);

        return converterParaDTO(atualizado);

    }


    @Transactional
    public boolean excluir(Long id) {

        Ingrediente ingrediente = ingredienteRepository.buscarPorId(id);

        if (ingrediente == null) {
            return false;
        }

        if(ingredienteRepository.existePorIngrediente(id)){
            throw new RuntimeException("Ingrediente não pode ser excluido");
        }

        ingredienteRepository.excluir(id);
        return true;
    }


    private IngredienteDTO converterParaDTO(Ingrediente ingrediente){

        return IngredienteDTO.builder()
                .id(ingrediente.getId())
                .nome(ingrediente.getNome())
                .unidadeMedida(
                        converterUnidadeDeMedidaParaDTO(ingrediente.getUnidadeMedida()))
                .build();
    }


    private UnidadeMedidaDTO converterUnidadeDeMedidaParaDTO(
            UnidadeMedida unidadeMedida){

        return UnidadeMedidaDTO.builder()
                .id(unidadeMedida.getId())
                .nome(unidadeMedida.getNome())
                .sigla(unidadeMedida.getSigla())
                .build();
    }


    private UnidadeMedida converterUnidadeDeMedidaParaEntidade(
            UnidadeMedidaDTO dto){

        return UnidadeMedida.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .sigla(dto.getSigla())
                .build();
    }


}
