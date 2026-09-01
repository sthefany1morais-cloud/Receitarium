package com.receitarium.receitarium.service;

import com.receitarium.receitarium.entity.Categoria;
import com.receitarium.receitarium.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodas();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return Optional.ofNullable(categoriaRepository.buscarPorId(id));
    }

    public Categoria salvar(Categoria categoria) {

        Categoria categoriaSalva =
                Categoria.builder()
                .nome(categoria.getNome())
                .build();
        return categoriaRepository.salvar(categoria);
    }

    public Categoria atualizar(Categoria categoria) {
        return categoriaRepository.atualizar(categoria);
    }

    public void excluir(Long id) {
        categoriaRepository.excluir(id);
    }

}
