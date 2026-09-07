package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.repository.UnidadeMedidaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadeMedidaService {

    private final UnidadeMedidaRepository repository;

    @Transactional
    public UnidadeMedidaDTO salvar(UnidadeMedidaDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dados da unidade são obrigatórios.");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da unidade é obrigatório.");
        }

        if (dto.getSigla() == null || dto.getSigla().isBlank()) {
            throw new IllegalArgumentException("Sigla da unidade é obrigatória.");
        }

        UnidadeMedida unidadeMedida =
                UnidadeMedida.builder()
                        .nome(dto.getNome())
                        .sigla(dto.getSigla())
                        .build();

        UnidadeMedida salva = repository.salvar(unidadeMedida);

        return converterParaDTO(salva);
    }

    public UnidadeMedidaDTO buscarPorId(Long id) {

        UnidadeMedida unidadeMedida =
                repository.buscarPorId(id);

        if (unidadeMedida == null) {
            return null;
        }

        return converterParaDTO(unidadeMedida);
    }

    public List<UnidadeMedidaDTO> listarTodas() {

        return repository.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public UnidadeMedidaDTO atualizar(
            Long id,
            UnidadeMedidaDTO dto
    ) {

        UnidadeMedida existente =
                repository.buscarPorId(id);

        if (existente == null) {
            return null;
        }

        if (dto == null) {
            throw new IllegalArgumentException("Dados da unidade são obrigatórios.");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da unidade é obrigatório.");
        }

        if (dto.getSigla() == null || dto.getSigla().isBlank()) {
            throw new IllegalArgumentException("Sigla da unidade é obrigatória.");
        }

        existente.setNome(dto.getNome());
        existente.setSigla(dto.getSigla());

        UnidadeMedida atualizada =
                repository.atualizar(existente);

        return converterParaDTO(atualizada);
    }

    @Transactional
    public boolean excluir(Long id) {

        UnidadeMedida existente =
                repository.buscarPorId(id);

        if (existente == null) {
            return false;
        }

        repository.excluir(id);

        return true;
    }

    private UnidadeMedidaDTO converterParaDTO(
            UnidadeMedida unidadeMedida
    ) {

        return UnidadeMedidaDTO.builder()
                .id(unidadeMedida.getId())
                .nome(unidadeMedida.getNome())
                .sigla(unidadeMedida.getSigla())
                .build();
    }
}