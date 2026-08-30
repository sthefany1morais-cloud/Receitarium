package com.receitarium.receitarium.repository;

import com.receitarium.receitarium.entity.ReceitaIngrediente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReceitaIngredienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ReceitaIngrediente salvar(ReceitaIngrediente receitaIngrediente) {
        entityManager.persist(receitaIngrediente);
        return receitaIngrediente;
    }

    public ReceitaIngrediente buscarPorId(Long id) {
        return entityManager.find(ReceitaIngrediente.class, id);
    }

    public List<ReceitaIngrediente> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT ri FROM ReceitaIngrediente ri",
                        ReceitaIngrediente.class
                )
                .getResultList();
    }

    public ReceitaIngrediente atualizar(ReceitaIngrediente receitaIngrediente) {
        return entityManager.merge(receitaIngrediente);
    }

    public void excluir(Long id) {
        ReceitaIngrediente receitaIngrediente =
                entityManager.find(ReceitaIngrediente.class, id);

        if (receitaIngrediente != null) {
            entityManager.remove(receitaIngrediente);
        }
    }
}