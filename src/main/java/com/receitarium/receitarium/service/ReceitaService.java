package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.CategoriaDTO;
import com.receitarium.receitarium.dto.IngredienteDTO;
import com.receitarium.receitarium.dto.ReceitaDTO;
import com.receitarium.receitarium.dto.ReceitaIngredienteDTO;
import com.receitarium.receitarium.entity.Categoria;
import com.receitarium.receitarium.entity.Ingrediente;
import com.receitarium.receitarium.entity.Receita;
import com.receitarium.receitarium.entity.ReceitaIngrediente;
import com.receitarium.receitarium.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;


    @Transactional
    public ReceitaDTO salvar(ReceitaDTO dto) {

        Receita receita = Receita.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .tempoPreparo(dto.getTempoPreparo())
                .rendimento(dto.getRendimento())
                .modoPreparo(dto.getModoPreparo())
                .categorias(dto.getCategorias().stream()
                        .map(this::converterCategoriaParaEntidade)
                        .toList())
                .ingredientes(dto.getIngredientes().stream()
                        .map(this::converterReceitaIngredienteParaEntidade)
                        .toList())
                .build();

        Receita salva = receitaRepository.salvar(receita);
        return converterParaDTO(salva);
    }

    public ReceitaDTO buscarPorId(Long id) {

        Receita receita = receitaRepository.buscarPorId(id);

        if (receita == null) {
            return null;
        }

        return converterParaDTO(receita);
    }

    public List<ReceitaDTO> listarTodas() {

        return receitaRepository.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public ReceitaDTO atualizar(ReceitaDTO dto, Long id){

        Receita existente = receitaRepository.buscarPorId(id);

        if(existente == null){
            return null;
        }

        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setTempoPreparo(dto.getTempoPreparo());
        existente.setRendimento(dto.getRendimento());
        existente.setModoPreparo(dto.getModoPreparo());
        existente.setCategorias(dto.getCategorias().stream()
                .map(this::converterCategoriaParaEntidade)
                .toList());
        existente.setIngredientes(dto.getIngredientes().stream()
                .map(this::converterReceitaIngredienteParaEntidade)
                .toList());

        Receita atuizada = receitaRepository.atualizar(existente);

        return converterParaDTO(atuizada);
    }

    @Transactional
    public boolean excluir(Long id) {

        Receita receita = receitaRepository.buscarPorId(id);

        if(receita == null){
            return false;
        }

        receitaRepository.excluir(id);
        return true;
    }

    private ReceitaDTO converterParaDTO(Receita receita) {

        return ReceitaDTO.builder()
                .id(receita.getId())
                .nome(receita.getNome())
                .descricao(receita.getDescricao())
                .tempoPreparo(receita.getTempoPreparo())
                .rendimento(receita.getRendimento())
                .modoPreparo(receita.getModoPreparo())
                .categorias(
                        receita.getCategorias().stream()
                        .map(this::converterCategoriaParaDTO)
                        .toList()
                )
                .ingredientes(receita.getIngredientes().stream()
                        .map(this::converterReceitaIngredienteParaDTO)
                        .toList())
                .build();
    }

    private CategoriaDTO converterCategoriaParaDTO(Categoria categoria) {

        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }

    private ReceitaIngredienteDTO converterReceitaIngredienteParaDTO(
            ReceitaIngrediente receitaIngrediente) {

        return ReceitaIngredienteDTO.builder()
                .ingrediente(converterIngredienteParaDTO(receitaIngrediente.getIngrediente()))
                .quantidade(receitaIngrediente.getQuantidade())
                .build();
    }

    private IngredienteDTO converterIngredienteParaDTO(Ingrediente ingrediente) {

        return IngredienteDTO.builder()
                .id(ingrediente.getId())
                .nome(ingrediente.getNome())
                .build();
    }

    private Categoria converterCategoriaParaEntidade(CategoriaDTO dto){

        return Categoria.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .build();
    }

    private Ingrediente converterIngredienteParaEntidade(IngredienteDTO dto){

        return Ingrediente.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .build();
    }
    private ReceitaIngrediente converterReceitaIngredienteParaEntidade(ReceitaIngredienteDTO dto){

        return ReceitaIngrediente.builder()
                .ingrediente(converterIngredienteParaEntidade(dto.getIngrediente()))
                .quantidade(dto.getQuantidade())
                .build();
    }

}
