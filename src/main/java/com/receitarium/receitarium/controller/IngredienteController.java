package com.receitarium.receitarium.controller;

import com.receitarium.receitarium.dto.IngredienteDTO;
import com.receitarium.receitarium.service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredientes")
@RequiredArgsConstructor
@Tag(
        name = "Ingredientes",
        description = "Operações de cadastro e gerenciamento de ingredientes"
)
public class IngredienteController {
    private final IngredienteService service;

    @Operation(summary = "Cadastrar ingrediente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ingrediente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou unidade de medida inexistente"),
            @ApiResponse(responseCode = "409", description = "Já existe um ingrediente com esse nome")
    })
    @PostMapping
    public ResponseEntity<IngredienteDTO> salvar(
            @RequestBody IngredienteDTO dto
    ) {
        IngredienteDTO salvo = service.salvar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @Operation(summary = "Buscar ingrediente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingrediente encontrado"),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<IngredienteDTO> buscarPorId(
            @Parameter(description = "ID do ingrediente", example = "1")
            @PathVariable Long id
    ) {
        IngredienteDTO dto = service.buscarPorId(id);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar ingrediente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingrediente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já utilizado"),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<IngredienteDTO> atualizar(
            @Parameter(description = "ID do ingrediente", example = "1")
            @PathVariable Long id,
            @RequestBody IngredienteDTO dto
    ) {
        IngredienteDTO atualizado = service.atualizar(dto, id);

        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir ingrediente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ingrediente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ingrediente não encontrado"),
            @ApiResponse(responseCode = "409", description = "Ingrediente em uso por uma receita")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do ingrediente", example = "1")
            @PathVariable Long id
    ) {
        boolean excluido = service.excluir(id);

        if (!excluido) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
