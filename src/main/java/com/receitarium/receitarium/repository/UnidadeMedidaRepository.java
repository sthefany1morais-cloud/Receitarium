package com.receitarium.receitarium.repository;

import com.receitarium.receitarium.entity.UnidadeMedida;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnidadeMedidaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UnidadeMedida salvar(UnidadeMedida unidadeMedida) {
        entityManager.persist(unidadeMedida);
        return unidadeMedida;
    }

    public UnidadeMedida buscarPorId(Long id) {
        return entityManager.find(UnidadeMedida.class, id);
    }

    public List<UnidadeMedida> listarTodas() {
        return entityManager
                .createQuery(
                        "SELECT u FROM UnidadeMedida u",
                        UnidadeMedida.class
                )
                .getResultList();
    }

    public UnidadeMedida atualizar(UnidadeMedida unidadeMedida) {
        return entityManager.merge(unidadeMedida);
    }

    public void excluir(Long id) {
        UnidadeMedida unidadeMedida =
                entityManager.find(UnidadeMedida.class, id);

        if (unidadeMedida != null) {
            entityManager.remove(unidadeMedida);
        }
    }
}