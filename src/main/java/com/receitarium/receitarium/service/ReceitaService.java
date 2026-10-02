package com.receitarium.receitarium.service;

import com.receitarium.receitarium.dto.*;
import com.receitarium.receitarium.entity.*;
import com.receitarium.receitarium.repository.CategoriaRepository;
import com.receitarium.receitarium.repository.IngredienteRepository;
import com.receitarium.receitarium.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CategoriaRepository categoriaRepository;
    private final IngredienteRepository ingredienteRepository;

    @Transactional
    public ReceitaDTO salvar(ReceitaDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Dados da receita não informados.");
        }

        if (dto.getIngredientes() == null || dto.getIngredientes().isEmpty()) {
            throw new IllegalArgumentException(
                    "A receita deve possuir pelo menos um ingrediente."
            );
        }

        for (ReceitaIngredienteDTO ingrediente : dto.getIngredientes()) {

            if (ingrediente == null ||
                    ingrediente.getIngrediente() == null ||
                    ingrediente.getIngrediente().getId() == null) {

                throw new IllegalArgumentException(
                        "Ingrediente inválido."
                );
            }

            if (ingrediente.getQuantidade() == null ||
                    ingrediente.getQuantidade().signum() <= 0) {

                throw new IllegalArgumentException(
                        "A quantidade do ingrediente deve ser maior que zero."
                );
            }
        }

        Receita receita = Receita.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .tempoPreparo(dto.getTempoPreparo())
                .rendimento(dto.getRendimento())
                .modoPreparo(dto.getModoPreparo())
                .categorias(
                        dto.getCategorias() == null
                                ? new ArrayList<>()
                                : dto.getCategorias().stream()
                                .map(this::converterCategoriaParaEntidade)
                                .toList()
                )
                .build();

        List<ReceitaIngrediente> receitaIngredientes = new ArrayList<>();

        for (ReceitaIngredienteDTO ingredienteDto : dto.getIngredientes()) {

            ReceitaIngrediente receitaIngrediente =
                    converterReceitaIngredienteParaEntidade(ingredienteDto);

            receitaIngrediente.setReceita(receita);

            receitaIngredientes.add(receitaIngrediente);
        }

        receita.setIngredientes(receitaIngredientes);

        Receita salva = receitaRepository.salvar(receita);

        return converterParaDTO(salva);
    }

    @Transactional(readOnly = true)
    public ReceitaDTO buscarPorId(Long id) {

        Receita receita = receitaRepository.buscarPorId(id);

        if (receita == null) {
            return null;
        }

        return converterParaDTO(receita);
    }

    @Transactional(readOnly = true)
    public List<ReceitaDTO> listarTodas() {

        return receitaRepository.listarTodas()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReceitaDTO> buscarPorCategoria(Long categoriaId) {

        return receitaRepository.buscarPorCategoria(categoriaId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ListaComprasDTO> gerarListaDeCompras(List<Long> receitaIds) {
        if (receitaIds == null || receitaIds.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma receita informada.");
        }

        List<ListaComprasDTO> listaCompras = new ArrayList<>();

        for (Long receitaId : receitaIds) {

            if (receitaId == null) {
                throw new IllegalArgumentException("ID de receita inválido.");
            }

            Receita receita = receitaRepository.buscarPorId(receitaId);

            if (receita == null) {
                throw new IllegalArgumentException(
                        "Receita não encontrada: " + receitaId
                );
            }

            for (ReceitaIngrediente receitaIngrediente : receita.getIngredientes()) {

                if (receitaIngrediente == null
                        || receitaIngrediente.getIngrediente() == null) {
                    continue;
                }

                Long ingredienteId =
                        receitaIngrediente.getIngrediente().getId();

                ListaComprasDTO itemExistente = listaCompras.stream()
                        .filter(item ->
                                item.getIngrediente().getId().equals(ingredienteId))
                        .findFirst()
                        .orElse(null);

                if (itemExistente != null) {

                    itemExistente.setQuantidade(
                            itemExistente.getQuantidade()
                                    .add(receitaIngrediente.getQuantidade())
                    );

                } else {

                    Ingrediente ingrediente =
                            receitaIngrediente.getIngrediente();

                    UnidadeMedida unidadeMedida =
                            ingrediente.getUnidadeMedida();

                    IngredienteDTO ingredienteDTO = IngredienteDTO.builder()
                            .id(ingrediente.getId())
                            .nome(ingrediente.getNome())
                            .unidadeMedida(
                                    unidadeMedida == null
                                            ? null
                                            : UnidadeMedidaDTO.builder()
                                            .id(unidadeMedida.getId())
                                            .nome(unidadeMedida.getNome())
                                            .sigla(unidadeMedida.getSigla())
                                            .build()
                            )
                            .build();

                    UnidadeMedidaDTO unidadeMedidaDTO =
                            unidadeMedida == null
                                    ? null
                                    : UnidadeMedidaDTO.builder()
                                    .id(unidadeMedida.getId())
                                    .nome(unidadeMedida.getNome())
                                    .sigla(unidadeMedida.getSigla())
                                    .build();

                    ListaComprasDTO novoItem = ListaComprasDTO.builder()
                            .ingrediente(ingredienteDTO)
                            .quantidade(receitaIngrediente.getQuantidade())
                            .unidadeMedida(unidadeMedidaDTO)
                            .build();

                    listaCompras.add(novoItem);
                }
            }
        }

        return listaCompras;
    }

    @Transactional
    public ReceitaDTO atualizar(ReceitaDTO dto, Long id) {

        Receita existente = receitaRepository.buscarPorId(id);

        if (existente == null) {
            return null;
        }

        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setTempoPreparo(dto.getTempoPreparo());
        existente.setRendimento(dto.getRendimento());
        existente.setModoPreparo(dto.getModoPreparo());

        existente.setCategorias(
                dto.getCategorias() == null
                        ? new ArrayList<>()
                        : dto.getCategorias().stream()
                        .map(this::converterCategoriaParaEntidade)
                        .toList()
        );

        List<ReceitaIngrediente> novosIngredientes =
                new ArrayList<>();

        if (dto.getIngredientes() != null) {

            for (ReceitaIngredienteDTO ingredienteDto :
                    dto.getIngredientes()) {

                if (ingredienteDto.getQuantidade() == null ||
                        ingredienteDto.getQuantidade().signum() <= 0) {

                    throw new IllegalArgumentException(
                            "A quantidade do ingrediente deve ser maior que zero."
                    );
                }

                ReceitaIngrediente receitaIngrediente =
                        converterReceitaIngredienteParaEntidade(
                                ingredienteDto
                        );

                receitaIngrediente.setReceita(existente);

                novosIngredientes.add(receitaIngrediente);
            }
        }

        existente.getIngredientes().clear();
        existente.getIngredientes().addAll(novosIngredientes);

        Receita atualizada = receitaRepository.atualizar(existente);

        return converterParaDTO(atualizada);
    }

    @Transactional
    public boolean excluir(Long id) {

        Receita receita = receitaRepository.buscarPorId(id);

        if (receita == null) {
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
                        receita.getCategorias() == null
                                ? new ArrayList<>()
                                : receita.getCategorias().stream()
                                .map(this::converterCategoriaParaDTO)
                                .toList()
                )
                .ingredientes(
                        receita.getIngredientes() == null
                                ? new ArrayList<>()
                                : receita.getIngredientes().stream()
                                .map(
                                        this::converterReceitaIngredienteParaDTO
                                )
                                .toList()
                )
                .build();
    }

    private CategoriaDTO converterCategoriaParaDTO(
            Categoria categoria) {

        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }

    private ReceitaIngredienteDTO converterReceitaIngredienteParaDTO(
            ReceitaIngrediente receitaIngrediente) {

        UnidadeMedidaDTO unidadeDTO = null;

        if (receitaIngrediente.getIngrediente() != null &&
                receitaIngrediente.getIngrediente().getUnidadeMedida() != null) {

            unidadeDTO = UnidadeMedidaDTO.builder()
                    .id(
                            receitaIngrediente
                                    .getIngrediente()
                                    .getUnidadeMedida()
                                    .getId()
                    )
                    .nome(
                            receitaIngrediente
                                    .getIngrediente()
                                    .getUnidadeMedida()
                                    .getNome()
                    )
                    .sigla(
                            receitaIngrediente
                                    .getIngrediente()
                                    .getUnidadeMedida()
                                    .getSigla()
                    )
                    .build();
        }

        return ReceitaIngredienteDTO.builder()
                .ingrediente(
                        converterIngredienteParaDTO(
                                receitaIngrediente.getIngrediente()
                        )
                )
                .quantidade(receitaIngrediente.getQuantidade())
                .unidadeMedida(unidadeDTO)
                .build();
    }

    private IngredienteDTO converterIngredienteParaDTO(
            Ingrediente ingrediente) {

        if (ingrediente == null) {
            return null;
        }

        UnidadeMedidaDTO unidadeDTO = null;

        if (ingrediente.getUnidadeMedida() != null) {

            unidadeDTO = UnidadeMedidaDTO.builder()
                    .id(ingrediente.getUnidadeMedida().getId())
                    .nome(ingrediente.getUnidadeMedida().getNome())
                    .sigla(ingrediente.getUnidadeMedida().getSigla())
                    .build();
        }

        return IngredienteDTO.builder()
                .id(ingrediente.getId())
                .nome(ingrediente.getNome())
                .unidadeMedida(unidadeDTO)
                .build();
    }

    private Categoria converterCategoriaParaEntidade(
            CategoriaDTO dto) {

        if (dto == null || dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Categoria inválida."
            );
        }

        Categoria categoria =
                categoriaRepository.buscarPorId(dto.getId());

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "Categoria não encontrada."
            );
        }

        return categoria;
    }

    private Ingrediente converterIngredienteParaEntidade(
            IngredienteDTO dto) {

        if (dto == null || dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Ingrediente inválido."
            );
        }

        Ingrediente ingrediente =
                ingredienteRepository.buscarPorId(dto.getId());

        if (ingrediente == null) {
            throw new IllegalArgumentException(
                    "Ingrediente não encontrado."
            );
        }

        return ingrediente;
    }

    private ReceitaIngrediente converterReceitaIngredienteParaEntidade(
            ReceitaIngredienteDTO dto) {

        return ReceitaIngrediente.builder()
                .ingrediente(
                        converterIngredienteParaEntidade(
                                dto.getIngrediente()
                        )
                )
                .quantidade(dto.getQuantidade())
                .build();
    }
}