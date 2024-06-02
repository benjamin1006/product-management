package de.benjamin1006.productmanagement.solution;

import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.service.ExecutionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "de.benjamin1006.productmanagement")
@EnableConfigurationProperties(ApplicationConfig.class)
public class ProductManagementSolutionApplication implements CommandLineRunner {

    private final ExecutionService executionService;

    public ProductManagementSolutionApplication(ExecutionService executionService) {
        this.executionService = executionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementSolutionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        executionService.importDataAndProcess();
    }
}
