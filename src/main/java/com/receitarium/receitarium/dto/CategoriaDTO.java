package com.receitarium.receitarium.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de uma categoria de receitas")
public class CategoriaDTO {

    @Schema(
            description = "Identificador da categoria",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome da categoria",
            example = "Bolos"
    )
    private String nome;
}