package com.receitarium.receitarium.service;

import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.repository.UnidadeMedidaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeMedidaService {

    private final UnidadeMedidaRepository repository;

    public UnidadeMedidaService(UnidadeMedidaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UnidadeMedida salvar(UnidadeMedida unidadeMedida) {
        return repository.salvar(unidadeMedida);
    }

    public UnidadeMedida buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<UnidadeMedida> listarTodas() {
        return repository.listarTodas();
    }

    @Transactional
    public UnidadeMedida atualizar(UnidadeMedida unidadeMedida) {
        return repository.atualizar(unidadeMedida);
    }

    @Transactional
    public void excluir(Long id) {
        repository.excluir(id);
    }
}