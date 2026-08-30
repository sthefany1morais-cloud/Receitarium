package com.receitarium.receitarium.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidade_medida")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    @Column(nullable = false, unique = true, length = 10)
    private String sigla;

    public UnidadeMedida(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }
}