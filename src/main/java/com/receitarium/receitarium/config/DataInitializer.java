package com.receitarium.receitarium.config;

import com.receitarium.receitarium.entity.UnidadeMedida;
import com.receitarium.receitarium.repository.UnidadeMedidaRepository;
import com.receitarium.receitarium.service.UnidadeMedidaService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UnidadeMedidaService service;

    public DataInitializer(UnidadeMedidaService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {

        if (service.listarTodas().isEmpty()) {

            service.salvar(
                    new UnidadeMedida("Unidade", "un")
            );

            service.salvar(
                    new UnidadeMedida("Grama", "g")
            );

            service.salvar(
                    new UnidadeMedida("Mililitro", "ml")
            );
        }
    }
}