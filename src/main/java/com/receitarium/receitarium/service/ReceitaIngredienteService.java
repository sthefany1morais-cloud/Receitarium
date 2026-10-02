package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.IngredienteDTO;
import com.receitarium.receitarium.dto.ReceitaIngredienteDTO;
import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.entity.Ingrediente;
import com.receitarium.receitarium.entity.Receita;
import com.receitarium.receitarium.entity.ReceitaIngrediente;
import com.receitarium.receitarium.repository.IngredienteRepository;
import com.receitarium.receitarium.repository.ReceitaIngredienteRepository;
import com.receitarium.receitarium.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceitaIngredienteService {

    private final ReceitaIngredienteRepository repository;
    private final IngredienteRepository ingredienteRepository;
    private final ReceitaRepository receitaRepository;

    @Transactional
    public ReceitaIngredienteDTO salvar(
            ReceitaIngredienteDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Dados do ingrediente da receita não informados."
            );
        }

        if (dto.getReceitaId() == null) {
            throw new IllegalArgumentException(
                    "A receita deve ser informada."
            );
        }

        if (dto.getIngrediente() == null ||
                dto.getIngrediente().getId() == null) {

            throw new IllegalArgumentException(
                    "O ingrediente deve ser informado."
            );
        }

        if (dto.getQuantidade() == null ||
                dto.getQuantidade().signum() <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }

        Receita receita =
                receitaRepository.buscarPorId(dto.getReceitaId());

        if (receita == null) {
            throw new IllegalArgumentException(
                    "Receita não encontrada."
            );
        }

        Ingrediente ingrediente =
                ingredienteRepository.buscarPorId(
                        dto.getIngrediente().getId()
                );

        if (ingrediente == null) {
            throw new IllegalArgumentException(
                    "Ingrediente não encontrado."
            );
        }

        ReceitaIngrediente receitaIngrediente =
                ReceitaIngrediente.builder()
                        .receita(receita)
                        .ingrediente(ingrediente)
                        .quantidade(dto.getQuantidade())
                        .build();

        ReceitaIngrediente salvo =
                repository.salvar(receitaIngrediente);

        return converterParaDTO(salvo);
    }

    @Transactional(readOnly = true)
    public ReceitaIngredienteDTO buscarPorId(Long id) {

        ReceitaIngrediente receitaIngrediente =
                repository.buscarPorId(id);

        if (receitaIngrediente == null) {
            return null;
        }

        return converterParaDTO(receitaIngrediente);
    }

    @Transactional(readOnly = true)
    public List<ReceitaIngredienteDTO> listarTodos() {

        return repository.listarTodos()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReceitaIngredienteDTO> buscarPorReceita(
            Long receitaId) {

        return repository.buscarPorReceita(receitaId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public ReceitaIngredienteDTO atualizar(
            Long id,
            ReceitaIngredienteDTO dto) {

        ReceitaIngrediente existente =
                repository.buscarPorId(id);

        if (existente == null) {
            return null;
        }

        if (dto.getReceitaId() != null) {

            Receita receita =
                    receitaRepository.buscarPorId(
                            dto.getReceitaId()
                    );

            if (receita == null) {
                throw new IllegalArgumentException(
                        "Receita não encontrada."
                );
            }

            existente.setReceita(receita);
        }

        if (dto.getIngrediente() == null ||
                dto.getIngrediente().getId() == null) {

            throw new IllegalArgumentException(
                    "O ingrediente deve ser informado."
            );
        }

        Ingrediente ingrediente =
                ingredienteRepository.buscarPorId(
                        dto.getIngrediente().getId()
                );

        if (ingrediente == null) {
            throw new IllegalArgumentException(
                    "Ingrediente não encontrado."
            );
        }

        if (dto.getQuantidade() == null ||
                dto.getQuantidade().signum() <= 0) {

            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }

        existente.setIngrediente(ingrediente);
        existente.setQuantidade(dto.getQuantidade());

        ReceitaIngrediente atualizado =
                repository.atualizar(existente);

        return converterParaDTO(atualizado);
    }

    @Transactional
    public boolean excluir(Long id) {

        ReceitaIngrediente existente =
                repository.buscarPorId(id);

        if (existente == null) {
            return false;
        }

        repository.excluir(id);

        return true;
    }

    private ReceitaIngredienteDTO converterParaDTO(
            ReceitaIngrediente receitaIngrediente) {

        UnidadeMedidaDTO unidadeDTO = null;

        if (receitaIngrediente.getIngrediente() != null &&
                receitaIngrediente.getIngrediente()
                        .getUnidadeMedida() != null) {

            unidadeDTO = UnidadeMedidaDTO.builder()
                    .id(
                            receitaIngrediente.getIngrediente()
                                    .getUnidadeMedida().getId()
                    )
                    .nome(
                            receitaIngrediente.getIngrediente()
                                    .getUnidadeMedida().getNome()
                    )
                    .sigla(
                            receitaIngrediente.getIngrediente()
                                    .getUnidadeMedida().getSigla()
                    )
                    .build();
        }

        return ReceitaIngredienteDTO.builder()
                .receitaId(
                        receitaIngrediente.getReceita() != null
                                ? receitaIngrediente
                                .getReceita()
                                .getId()
                                : null
                )
                .ingrediente(
                        converterIngredienteParaDTO(
                                receitaIngrediente.getIngrediente()
                        )
                )
                .quantidade(
                        receitaIngrediente.getQuantidade()
                )
                .unidadeMedida(unidadeDTO)
                .build();
    }

    private IngredienteDTO converterIngredienteParaDTO(
            Ingrediente ingrediente) {

        if (ingrediente == null) {
            return null;
        }

        UnidadeMedidaDTO unidadeDTO = null;

        if (ingrediente.getUnidadeMedida() != null) {

            unidadeDTO = UnidadeMedidaDTO.builder()
                    .id(ingrediente.getUnidadeMedida().getId())
                    .nome(ingrediente.getUnidadeMedida().getNome())
                    .sigla(ingrediente.getUnidadeMedida().getSigla())
                    .build();
        }

        return IngredienteDTO.builder()
                .id(ingrediente.getId())
                .nome(ingrediente.getNome())
                .unidadeMedida(unidadeDTO)
                .build();
    }
}