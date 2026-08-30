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
        return entityManager.find(
                Receita.class,
                id
        );
    }

    public List<Receita> listarTodas() {
        return entityManager
                .createQuery(
                        "SELECT r FROM Receita r",
                        Receita.class
                )
                .getResultList();
    }

    public List<Receita> buscarPorCategoria(Long categoriaId) {
        return entityManager
                .createQuery(
                        """
                        SELECT DISTINCT r
                        FROM Receita r
                        JOIN r.categorias c
                        WHERE c.id = :categoriaId
                        """,
                        Receita.class
                )
                .setParameter("categoriaId", categoriaId)
                .getResultList();
    }

    public Receita atualizar(Receita receita) {
        return entityManager.merge(receita);
    }

    public void excluir(Long id) {
        Receita receita =
                entityManager.find(
                        Receita.class,
                        id
                );

        if (receita != null) {
            entityManager.remove(receita);
        }
    }
}