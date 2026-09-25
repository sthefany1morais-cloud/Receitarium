package com.receitarium.receitarium.controller;

import com.receitarium.receitarium.dto.ReceitaIngredienteDTO;
import com.receitarium.receitarium.service.ReceitaIngredienteService;
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
@RequestMapping("/api/receita-ingredientes")
@RequiredArgsConstructor
@Tag(
        name = "Receita e Ingrediente",
        description = "Vínculo entre receita e ingrediente, com a quantidade utilizada"
)
public class ReceitaIngredienteController {
    
    private final ReceitaIngredienteService service;

    @Operation(summary = "Cadastrar vínculo de ingrediente em receita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vínculo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ingrediente ausente ou quantidade inválida")
    })
    @PostMapping
    public ResponseEntity<ReceitaIngredienteDTO> salvar(
            @RequestBody ReceitaIngredienteDTO dto
    ) {

        ReceitaIngredienteDTO salvo = service.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salvo);
    }

    @Operation(summary = "Listar vínculos de ingredientes")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ReceitaIngredienteDTO>> listarTodos() {

        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar vínculo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vínculo encontrado"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaIngredienteDTO> buscarPorId(

            @Parameter(description = "ID do vínculo", example = "1")
            @PathVariable Long id
    ) {

        ReceitaIngredienteDTO dto = service.buscarPorId(id);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar vínculo de ingrediente em receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vínculo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Ingrediente ausente ou quantidade 
inválida"),
            @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaIngredienteDTO> atualizar(

            @Parameter(description = "ID do vínculo", example = "1")
            @PathVariable Long id,

            @RequestBody ReceitaIngredienteDTO dto
    ) {

        ReceitaIngredienteDTO atualizado = service.atualizar(id, dto);

        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir vínculo de ingrediente em receita")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vínculo excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(

            @Parameter(description = "ID do vínculo", example = "1")
            @PathVariable Long id
    ) {

        boolean excluido = service.excluir(id);

        if (!excluido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}