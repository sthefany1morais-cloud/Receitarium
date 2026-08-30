package com.receitarium.receitarium.repository;

import com.receitarium.receitarium.entity.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Categoria salvar(Categoria categoria) {
        entityManager.persist(categoria);
        return categoria;
    }

    public Categoria buscarPorId(Long id) {
        return entityManager.find(Categoria.class, id);
    }

    public List<Categoria> listarTodas() {
        return entityManager
                .createQuery(
                        "SELECT c FROM Categoria c",
                        Categoria.class
                )
                .getResultList();
    }

    public Categoria atualizar(Categoria categoria) {
        return entityManager.merge(categoria);
    }

    public void excluir(Long id) {
        Categoria categoria =
                entityManager.find(Categoria.class, id);

        if (categoria != null) {
            entityManager.remove(categoria);
        }
    }
}