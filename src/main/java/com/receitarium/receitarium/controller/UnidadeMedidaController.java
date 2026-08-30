package com.receitarium.receitarium.controller;

import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.service.UnidadeMedidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades-medida")
@RequiredArgsConstructor
@Tag(
        name = "Unidades de Medida",
        description = "Operações de cadastro e gerenciamento das unidades de medida"
)
public class UnidadeMedidaController {

    private final UnidadeMedidaService service;

    @Operation(
            summary = "Cadastrar unidade de medida"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Unidade criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<UnidadeMedidaDTO> salvar(
            @RequestBody UnidadeMedidaDTO dto
    ) {

        UnidadeMedidaDTO salva =
                service.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salva);
    }

    @Operation(
            summary = "Listar unidades de medida"
    )
    @GetMapping
    public ResponseEntity<List<UnidadeMedidaDTO>> listarTodas() {

        return ResponseEntity.ok(
                service.listarTodas()
        );
    }

    @Operation(
            summary = "Buscar unidade de medida por ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Unidade encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Unidade não encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeMedidaDTO> buscarPorId(

            @Parameter(
                    description = "ID da unidade de medida",
                    example = "1"
            )
            @PathVariable Long id
    ) {

        UnidadeMedidaDTO dto =
                service.buscarPorId(id);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Atualizar unidade de medida"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Unidade atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Unidade não encontrada"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeMedidaDTO> atualizar(

            @PathVariable Long id,

            @RequestBody UnidadeMedidaDTO dto
    ) {

        UnidadeMedidaDTO atualizada =
                service.atualizar(id, dto);

        if (atualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizada);
    }

    @Operation(
            summary = "Excluir unidade de medida"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Unidade excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Unidade não encontrada"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id
    ) {

        boolean excluida =
                service.excluir(id);

        if (!excluida) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}