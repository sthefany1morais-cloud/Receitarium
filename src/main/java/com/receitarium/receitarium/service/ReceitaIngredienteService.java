package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.IngredienteDTO;
import com.receitarium.receitarium.dto.ReceitaIngredienteDTO;
import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.entity.Ingrediente;
import com.receitarium.receitarium.entity.ReceitaIngrediente;
import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.repository.IngredienteRepository;
import com.receitarium.receitarium.repository.ReceitaIngredienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceitaIngredienteService {

    private final ReceitaIngredienteRepository receitaIngredienteRepository;
    private final IngredienteRepository ingredienteRepository;


    @Transactional
    public ReceitaIngredienteDTO salvar(ReceitaIngredienteDTO dto) {

        if(dto == null || dto.getIngrediente() == null) {
            throw new IllegalArgumentException("Ingrediente não pode ser nulo.");
        }

        if(dto.getQuantidade() == null || dto.getQuantidade().signum() <= 0) {
            throw new IllegalArgumentException("Quantidade não pode ser nula.");
        }

        Ingrediente ingrediente = converterParaEntidade(dto.getIngrediente());

        ReceitaIngrediente receitaIngrediente = ReceitaIngrediente.builder()
                .ingrediente(ingrediente)
                .quantidade(dto.getQuantidade())
                .build();

        ReceitaIngrediente salva = receitaIngredienteRepository.salvar(receitaIngrediente);

        return converterParaDTO(salva);
    }

    public ReceitaIngredienteDTO buscarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        ReceitaIngrediente receitaIngrediente = receitaIngredienteRepository.buscarPorId(id);

        if (receitaIngrediente == null) {
            return null;
        }

        return converterParaDTO(receitaIngrediente);
    }

    public List<ReceitaIngredienteDTO> listarTodos() {
        return receitaIngredienteRepository.listarTodos()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public ReceitaIngredienteDTO atualizar(Long id, ReceitaIngredienteDTO dto) {

        ReceitaIngrediente existente = receitaIngredienteRepository.buscarPorId(id);

        if (existente == null) {
            return null;
        }

        if(dto == null || dto.getIngrediente() == null) {
            throw new IllegalArgumentException("Ingrediente não pode ser nulo.");
        }

        if(dto.getQuantidade() == null || dto.getQuantidade().signum() <= 0) {
            throw new IllegalArgumentException("Quantidade não pode ser nula.");
        }

        Ingrediente ingrediente = converterParaEntidade(dto.getIngrediente());

        existente.setQuantidade(dto.getQuantidade());
        existente.setIngrediente(ingrediente);

        ReceitaIngrediente atualizada = receitaIngredienteRepository.atualizar(existente);

        return converterParaDTO(atualizada);
    }

    @Transactional
    public boolean excluir(Long id) {
        ReceitaIngrediente existente = receitaIngredienteRepository.buscarPorId(id);

        if (existente == null) {
            return false;
        }

        receitaIngredienteRepository.excluir(id);
        return true;
    }

    private ReceitaIngredienteDTO converterParaDTO(ReceitaIngrediente receitaIngrediente) {
        Ingrediente ingrediente = receitaIngrediente.getIngrediente();
        UnidadeMedida unidadeMedida = ingrediente != null ? ingrediente.getUnidadeMedida() : null;

        IngredienteDTO ingredienteDTO = null;
        if (ingrediente != null) {
            UnidadeMedidaDTO unidadeMedidaDTO = null;
            if (unidadeMedida != null) {
                unidadeMedidaDTO = UnidadeMedidaDTO.builder()
                        .id(unidadeMedida.getId())
                        .nome(unidadeMedida.getNome())
                        .sigla(unidadeMedida.getSigla())
                        .build();
            }

            ingredienteDTO = IngredienteDTO.builder()
                    .id(ingrediente.getId())
                    .nome(ingrediente.getNome())
                    .unidadeMedida(unidadeMedidaDTO)
                    .build();
        }

        return ReceitaIngredienteDTO.builder()
                .ingrediente(ingredienteDTO)
                .quantidade(receitaIngrediente.getQuantidade())
                .build();
    }

    private Ingrediente converterParaEntidade(IngredienteDTO dto) {

        if (dto == null || dto.getId() == null) {
            return null;
        }

        Ingrediente ingrediente =
                ingredienteRepository.buscarPorId(dto.getId());

        if (ingrediente == null) {
            throw new IllegalArgumentException(
                    "Ingrediente não encontrado."
            );
        }

        return ingrediente;
    }
}
