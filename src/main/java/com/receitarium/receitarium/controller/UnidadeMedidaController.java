package com.receitarium.receitarium.controller;

import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.service.UnidadeMedidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades-medida")
@Tag(
        name = "Unidades de Medida",
        description = "Operações de cadastro e gerenciamento das unidades de medida"
)
public class UnidadeMedidaController {

    private final UnidadeMedidaService service;

    public UnidadeMedidaController(UnidadeMedidaService service) {
        this.service = service;
    }

    @Operation(
            summary = "Cadastrar uma unidade de medida",
            description = "Cria uma nova unidade de medida no Receitarium."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Unidade de medida criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @PostMapping
    public ResponseEntity<UnidadeMedida> salvar(
            @RequestBody UnidadeMedida unidadeMedida
    ) {
        UnidadeMedida salva = service.salvar(unidadeMedida);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salva);
    }

    @Operation(
            summary = "Listar unidades de medida",
            description = "Retorna todas as unidades de medida cadastradas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de unidades retornada com sucesso"
    )
    @GetMapping
    public ResponseEntity<List<UnidadeMedida>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(
            summary = "Buscar unidade de medida por ID",
            description = "Retorna uma unidade de medida específica."
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
    public ResponseEntity<UnidadeMedida> buscarPorId(
            @Parameter(
                    description = "ID da unidade de medida",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        UnidadeMedida unidade = service.buscarPorId(id);

        if (unidade == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(unidade);
    }

    @Operation(
            summary = "Atualizar unidade de medida",
            description = "Atualiza uma unidade de medida existente."
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
    public ResponseEntity<UnidadeMedida> atualizar(
            @Parameter(
                    description = "ID da unidade de medida",
                    example = "1"
            )
            @PathVariable Long id,

            @RequestBody UnidadeMedida unidadeMedida
    ) {
        UnidadeMedida existente = service.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        unidadeMedida.setId(id);

        UnidadeMedida atualizada =
                service.atualizar(unidadeMedida);

        return ResponseEntity.ok(atualizada);
    }

    @Operation(
            summary = "Excluir unidade de medida",
            description = "Exclui uma unidade de medida pelo ID."
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
            @Parameter(
                    description = "ID da unidade de medida",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        UnidadeMedida unidade = service.buscarPorId(id);

        if (unidade == null) {
            return ResponseEntity.notFound().build();
        }

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}