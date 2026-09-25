package com.receitarium.receitarium.controller;

import com.receitarium.receitarium.dto.ListaComprasDTO;
import com.receitarium.receitarium.dto.ReceitaDTO;
import com.receitarium.receitarium.service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/receitas")
@RequiredArgsConstructor
@Tag(
        name = "Receitas",
        description = "Operações de cadastro, consulta e geração de lista de compras"
)
public class ReceitaController {

    private final ReceitaService service;

    @Operation(summary = "Cadastrar receita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receita criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, categoria ou ingrediente inexistente")
    })
    @PostMapping
    public ResponseEntity<ReceitaDTO> salvar(
            @RequestBody ReceitaDTO dto
    ) {
        ReceitaDTO salva = service.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salva);
    }

    @Operation(summary = "Listar receitas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ReceitaDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar receita por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita encontrada"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> buscarPorId(
            @Parameter(description = "ID da receita", example = "1")
            @PathVariable Long id
    ) {
        ReceitaDTO dto = service.buscarPorId(id);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar receitas de uma categoria")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ReceitaDTO>> buscarPorCategoria(
            @Parameter(description = "ID da categoria", example = "1")
            @PathVariable Long categoriaId
    ) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoriaId));
    }

    @Operation(summary = "Gerar lista de compras a partir de receitas selecionadas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de compras gerada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nenhum id informado"),
            @ApiResponse(responseCode = "404", description = "Alguma das receitas informadas não existe")
    })
    @PostMapping("/lista-compras")
    public ResponseEntity<List<ListaComprasDTO>> gerarListaDeCompras(
            @RequestBody List<Long> idsDasReceitas
    ) {
        if (idsDasReceitas == null || idsDasReceitas.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<ReceitaDTO> receitas = new ArrayList<>();

        for (Long id : idsDasReceitas) {

            ReceitaDTO receita = service.buscarPorId(id);

            if (receita == null) {
                return ResponseEntity.notFound().build();
            }

            receitas.add(receita);
        }

        return ResponseEntity.ok(service.gerarListaDeCompras(receitas));
    }

    @Operation(summary = "Atualizar receita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaDTO> atualizar(
            @Parameter(description = "ID da receita", example = "1")
            @PathVariable Long id,
            @RequestBody ReceitaDTO dto
    ) {

        ReceitaDTO atualizada = service.atualizar(dto, id);

        if (atualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizada);
    }

    @Operation(summary = "Excluir receita")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receita excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da receita", example = "1")
            @PathVariable Long id
    ) {
        
        boolean excluida = service.excluir(id);

        if (!excluida) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}