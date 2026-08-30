package com.receitarium.receitarium.config;

import com.receitarium.receitarium.dto.UnidadeMedidaDTO;
import com.receitarium.receitarium.service.UnidadeMedidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UnidadeMedidaService service;

    @Override
    public void run(String... args) {

        if (service.listarTodas().isEmpty()) {

            service.salvar(
                    UnidadeMedidaDTO.builder()
                            .nome("Unidade")
                            .sigla("un")
                            .build()
            );

            service.salvar(
                    UnidadeMedidaDTO.builder()
                            .nome("Grama")
                            .sigla("g")
                            .build()
            );

            service.salvar(
                    UnidadeMedidaDTO.builder()
                            .nome("Mililitro")
                            .sigla("ml")
                            .build()
            );
        }
    }
}