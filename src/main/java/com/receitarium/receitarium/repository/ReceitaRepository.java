package com.receitarium.receitarium.repository;

import com.receitarium.receitarium.entity.Receita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReceitaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Receita salvar(Receita receita) {
        entityManager.persist(receita);
        return receita;
    }

    public Receita buscarPorId(Long id) {
        return entityManager.find(Receita.class, id);
    }

    public List<Receita> listarTodas() {
        return entityManager.createQuery(
                "SELECT DISTINCT r FROM Receita r " +
                        "LEFT JOIN FETCH r.ingredientes ri " +
                        "LEFT JOIN FETCH ri.ingrediente i " +
                        "LEFT JOIN FETCH i.unidadeMedida",
                Receita.class
        ).getResultList();
    }

    public List<Receita> buscarPorCategoria(Long categoriaId) {
        return entityManager.createQuery(
                        "SELECT DISTINCT r " +
                                "FROM Receita r " +
                                "JOIN r.categorias c " +
                                "LEFT JOIN FETCH r.ingredientes ri " +
                                "LEFT JOIN FETCH ri.ingrediente i " +
                                "LEFT JOIN FETCH i.unidadeMedida " +
                                "WHERE c.id = :categoriaId",
                        Receita.class
                )
                .setParameter("categoriaId", categoriaId)
                .getResultList();
    }

    public Receita atualizar(Receita receita) {
        return entityManager.merge(receita);
    }

    public void excluir(Long id) {
        Receita receita = buscarPorId(id);

        if (receita != null) {
            entityManager.remove(receita);
        }
    }
}