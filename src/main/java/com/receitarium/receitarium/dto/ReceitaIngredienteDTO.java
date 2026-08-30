package com.receitarium.receitarium.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ingrediente utilizado em uma receita, com sua quantidade e unidade de medida")
public class ReceitaIngredienteDTO {

    @Schema(
            description = "Ingrediente utilizado na receita"
    )
    private IngredienteDTO ingrediente;

    @Schema(
            description = "Quantidade do ingrediente utilizada na receita",
            example = "300.00"
    )
    private BigDecimal quantidade;

    @Schema(
            description = "Unidade de medida utilizada para a quantidade do ingrediente"
    )
    private UnidadeMedidaDTO unidadeMedida;
}