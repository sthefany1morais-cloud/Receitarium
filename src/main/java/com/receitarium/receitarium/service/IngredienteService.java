package com.receitarium.receitarium.service;

import com.receitarium.receitarium.entity.Ingrediente;
import com.receitarium.receitarium.repository.IngredienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository){
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<Ingrediente> listarTodas() {
        return ingredienteRepository.listarTodos();
    }

    public Optional<Ingrediente> buscarPorId(Long id) {
        return Optional.ofNullable(ingredienteRepository.buscarPorId(id));
    }

    public Ingrediente salvar(Ingrediente ingrediente) {
        return ingredienteRepository.salvar(ingrediente);
    }

    public Ingrediente atualizar(Ingrediente ingrediente) {
        return ingredienteRepository.atualizar(ingrediente);
    }

    public void excluir(Long id) {
        ingredienteRepository.excluir(id);
    }

}
