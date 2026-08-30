package com.receitarium.receitarium.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados completos de uma receita")
public class ReceitaDTO {

    @Schema(
            description = "Identificador da receita",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome da receita",
            example = "Bolo de Chocolate"
    )
    private String nome;

    @Schema(
            description = "Descrição da receita",
            example = "Bolo de chocolate simples e fácil de preparar"
    )
    private String descricao;

    @Schema(
            description = "Tempo necessário para preparar a receita, em minutos",
            example = "60"
    )
    private Integer tempoPreparo;

    @Schema(
            description = "Quantidade de porções produzidas pela receita",
            example = "10"
    )
    private Integer rendimento;

    @Schema(
            description = "Modo de preparo da receita",
            example = "Misture os ingredientes secos, adicione os líquidos e leve ao forno."
    )
    private String modoPreparo;

    @Schema(
            description = "Categorias às quais a receita pertence"
    )
    @Builder.Default
    private List<CategoriaDTO> categorias = new ArrayList<>();

    @Schema(
            description = "Ingredientes utilizados na receita, suas quantidades e unidades de medida"
    )
    @Builder.Default
    private List<ReceitaIngredienteDTO> ingredientes = new ArrayList<>();
}