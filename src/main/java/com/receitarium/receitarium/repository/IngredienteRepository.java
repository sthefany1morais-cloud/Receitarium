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

    public List<Ingrediente> listarTodas() {
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

    public Ingrediente buscarPorNome(String nome) {
        return entityManager.createQuery(
                        "SELECT i FROM Ingrediente i WHERE i.nome = :nome",
                        Ingrediente.class
                )
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst()
                .orElse(null);
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