package com.receitarium.receitarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "tempo_preparo")
    private Integer tempoPreparo;

    private Integer rendimento;

    @Column(name = "modo_preparo", columnDefinition = "TEXT")
    private String modoPreparo;

    @ManyToMany
    @JoinTable(
            name = "receita_categoria",
            joinColumns = @JoinColumn(name = "receita_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Builder.Default
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "receita")
    @Builder.Default
    private List<ReceitaIngrediente> ingredientes = new ArrayList<>();
}