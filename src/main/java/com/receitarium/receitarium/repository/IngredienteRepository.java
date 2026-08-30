package com.receitarium.receitarium.repository;

import com.receitarium.receitarium.entity.Ingrediente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IngredienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Ingrediente salvar(Ingrediente ingrediente) {
        entityManager.persist(ingrediente);
        return ingrediente;
    }

    public Ingrediente buscarPorId(Long id) {
        return entityManager.find(Ingrediente.class, id);
    }

    public List<Ingrediente> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT i FROM Ingrediente i",
                        Ingrediente.class
                )
                .getResultList();
    }

    public Ingrediente atualizar(Ingrediente ingrediente) {
        return entityManager.merge(ingrediente);
    }

    public void excluir(Long id) {
        Ingrediente ingrediente =
                entityManager.find(Ingrediente.class, id);

        if (ingrediente != null) {
            entityManager.remove(ingrediente);
        }
    }
}