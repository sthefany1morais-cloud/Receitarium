package com.receitarium.receitarium.service;

import com.receitarium.receitarium.entity.Receita;
import com.receitarium.receitarium.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;

    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    public List<Receita> listarTodas() {
        return receitaRepository.listarTodas();
    }

    public Optional<Receita> buscarPorId(Long id) {
        return Optional.ofNullable(receitaRepository.buscarPorId(id));
    }

    public Receita salvar(Receita receita) {
        return receitaRepository.salvar(receita);
    }

    public Receita atualizar(Receita receita){
        return receitaRepository.atualizar(receita);
    }

    public void excluir(Long id) {
        receitaRepository.excluir(id);
    }

}
