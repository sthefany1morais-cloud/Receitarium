package com.receitarium.receitarium.repository;

import com.receitarium.receitarium.entity.Receita;
import com.receitarium.receitarium.entity.ReceitaIngrediente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReceitaIngredienteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ReceitaIngrediente salvar(
            ReceitaIngrediente receitaIngrediente
    ) {
        entityManager.persist(receitaIngrediente);
        return receitaIngrediente;
    }

    public ReceitaIngrediente buscarPorId(Long id) {
        return entityManager.find(
                ReceitaIngrediente.class,
                id
        );
    }

    public List<ReceitaIngrediente> listarTodos() {
        return entityManager
                .createQuery(
                        "SELECT ri FROM ReceitaIngrediente ri",
                        ReceitaIngrediente.class
                )
                .getResultList();
    }

    public List<ReceitaIngrediente> buscarPorReceita(Long receitaId) {
        return entityManager
                .createQuery(
                        """
                        SELECT ri
                        FROM ReceitaIngrediente ri
                        JOIN FETCH ri.ingrediente i
                        JOIN FETCH i.unidadeMedida
                        WHERE ri.receita.id = :receitaId
                        """,
                        ReceitaIngrediente.class
                )
                .setParameter("receitaId", receitaId)
                .getResultList();
    }

    public List<ReceitaIngrediente> buscarPorIngrediente(
            Long ingredienteId
    ) {
        return entityManager
                .createQuery(
                        """
                        SELECT ri
                        FROM ReceitaIngrediente ri
                        JOIN FETCH ri.receita
                        WHERE ri.ingrediente.id = :ingredienteId
                        """,
                        ReceitaIngrediente.class
                )
                .setParameter("ingredienteId", ingredienteId)
                .getResultList();
    }

    public ReceitaIngrediente atualizar(
            ReceitaIngrediente receitaIngrediente
    ) {
        return entityManager.merge(receitaIngrediente);
    }

    public void excluir(Long id) {
        ReceitaIngrediente receitaIngrediente =
                entityManager.find(
                        ReceitaIngrediente.class,
                        id
                );

        if (receitaIngrediente != null) {
            entityManager.remove(receitaIngrediente);
        }
    }

    public boolean existePorIngrediente(Long ingredienteId) {
        Long quantidade = entityManager.createQuery(
                        "SELECT COUNT(ri) FROM ReceitaIngrediente ri " +
                                "WHERE ri.ingrediente.id = :ingredienteId",
                        Long.class
                )
                .setParameter("ingredienteId", ingredienteId)
                .getSingleResult();

        return quantidade > 0;
    }
}