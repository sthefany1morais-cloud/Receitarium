package com.receitarium.receitarium.service;

import com.receitarium.receitarium.entity.ReceitaIngrediente;
import com.receitarium.receitarium.repository.ReceitaIngredienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaIngredienteService {

    private final ReceitaIngredienteRepository receitaIngredienteRepository;

    public ReceitaIngredienteService(ReceitaIngredienteRepository repository) {
        this.receitaIngredienteRepository = repository;
    }

    public List<ReceitaIngrediente> listarTodos() {
        return receitaIngredienteRepository.listarTodos();
    }

    public Optional<ReceitaIngrediente> buscarPorId(Long id) {
        return Optional.ofNullable(receitaIngredienteRepository.buscarPorId(id));
    }

    public ReceitaIngrediente salvar(ReceitaIngrediente receitaIngrediente) {
        return receitaIngredienteRepository.salvar(receitaIngrediente);
    }

    public ReceitaIngrediente atualizar(ReceitaIngrediente receitaIngrediente) {
        return receitaIngredienteRepository.atualizar(receitaIngrediente);
    }

    public void excluir(Long id) {
        receitaIngredienteRepository.excluir(id);
    }
}
