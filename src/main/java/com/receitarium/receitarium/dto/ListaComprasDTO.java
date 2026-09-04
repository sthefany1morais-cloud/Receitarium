package com.receitarium.receitarium.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item da lista de compras")
public class ListaComprasDTO {

    @Schema(
            description = "Ingrediente necessário para a lista de compras"
    )
    private IngredienteDTO ingrediente;

    @Schema(
            description = "Quantidade total necessária do ingrediente",
            example = "500.00"
    )
    private BigDecimal quantidade;

    @Schema(
            description = "Unidade de medida do ingrediente"
    )
    private UnidadeMedidaDTO unidadeMedida;
}