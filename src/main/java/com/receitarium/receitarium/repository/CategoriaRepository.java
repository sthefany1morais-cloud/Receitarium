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

    public Categoria buscarPorNome(String nome) {
        return entityManager.createQuery(
                        "SELECT c FROM Categoria c WHERE c.nome = :nome",
                        Categoria.class
                )
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean existePorCategoria(Long categoriaId) {
        Long quantidade = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Receita r JOIN r.categorias c WHERE c.id = :categoriaId",
                        Long.class
                )
                .setParameter("categoriaId", categoriaId)
                .getSingleResult();

        return quantidade > 0;
    }
}