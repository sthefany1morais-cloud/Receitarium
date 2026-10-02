package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.IngredienteDTO;
import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.entity.Ingrediente;
import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.repository.IngredienteRepository;
import com.receitarium.receitarium.repository.ReceitaIngredienteRepository;
import com.receitarium.receitarium.repository.UnidadeMedidaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@AllArgsConstructor
@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final ReceitaIngredienteRepository receitaIngredienteRepository;
    private final UnidadeMedidaRepository unidadeMedidaRepository;


    @Transactional
    public IngredienteDTO salvar(IngredienteDTO dto) {

        if (dto == null || dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do ingrediente é obrigatório.");
        }

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
            throw new IllegalArgumentException("Ingrediente já existente.");
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

        if(receitaIngredienteRepository.existePorIngrediente(id)){
            throw new IllegalStateException(
                    "Ingrediente não pode ser excluído porque está sendo utilizado em uma receita."
            );
        }

        ingredienteRepository.excluir(id);
        return true;
    }


    private IngredienteDTO converterParaDTO(Ingrediente ingrediente) {

        return IngredienteDTO.builder()
                .id(ingrediente.getId())
                .nome(ingrediente.getNome())
                .unidadeMedida(
                        ingrediente.getUnidadeMedida() != null
                                ? converterUnidadeDeMedidaParaDTO(
                                ingrediente.getUnidadeMedida())
                                : null
                )
                .build();
    }


    private UnidadeMedidaDTO converterUnidadeDeMedidaParaDTO(
            UnidadeMedida unidadeMedida){

        if (unidadeMedida == null) {
            return null;
        }

        return UnidadeMedidaDTO.builder()
                .id(unidadeMedida.getId())
                .nome(unidadeMedida.getNome())
                .sigla(unidadeMedida.getSigla())
                .build();
    }


    private UnidadeMedida converterUnidadeDeMedidaParaEntidade(
            UnidadeMedidaDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Unidade de medida é obrigatória."
            );
        }

        if (dto.getId() != null) {
            UnidadeMedida unidadeMedida = unidadeMedidaRepository.buscarPorId(dto.getId());
            if (unidadeMedida != null) {
                if (dto.getNome() != null) unidadeMedida.setNome(dto.getNome());
                if (dto.getSigla() != null) unidadeMedida.setSigla(dto.getSigla());
                return unidadeMedida;
            }
        }

        return UnidadeMedida.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .sigla(dto.getSigla())
                .build();
    }


}
