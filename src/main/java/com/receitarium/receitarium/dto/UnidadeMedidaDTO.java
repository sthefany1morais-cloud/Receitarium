package com.receitarium.receitarium.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de uma unidade de medida")
public class UnidadeMedidaDTO {

    @Schema(
            description = "Identificador da unidade de medida",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome da unidade de medida",
            example = "Grama"
    )
    private String nome;

    @Schema(
            description = "Sigla da unidade de medida",
            example = "g"
    )
    private String sigla;
}