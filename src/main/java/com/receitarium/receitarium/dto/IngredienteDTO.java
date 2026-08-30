package com.receitarium.receitarium.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de um ingrediente")
public class IngredienteDTO {

    @Schema(
            description = "Identificador do ingrediente",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome do ingrediente",
            example = "Farinha de trigo"
    )
    private String nome;

    @Schema(
            description = "Unidade de medida utilizada pelo ingrediente"
    )
    private UnidadeMedidaDTO unidadeMedida;
}