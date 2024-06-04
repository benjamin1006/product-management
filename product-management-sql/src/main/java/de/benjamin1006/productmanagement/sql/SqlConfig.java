package de.benjamin1006.productmanagement.sql;

import de.benjamin1006.productmanagement.core.component.strategy.IEntityMapperStrategy;
import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.repository.ProductRepository;
import de.benjamin1006.productmanagement.sql.service.SqlImportService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * @author Benjamin Woitczyk
 */
@Configuration
@ConditionalOnProperty(name = "product-management.sql-import-active", havingValue = "true", matchIfMissing = true)
@EnableJpaRepositories(basePackages = "de.benjamin1006.productmanagement")
@EntityScan("de.benjamin1006.productmanagement")
public class SqlConfig {

    private final List<IEntityMapperStrategy> entityToDtoMappers;
    private final ProductRepository productRepository;
    private final ApplicationConfig applicationConfig;

    public SqlConfig(List<IEntityMapperStrategy> entityToDtoMappers, ProductRepository productRepository, ApplicationConfig applicationConfig) {
        this.entityToDtoMappers = entityToDtoMappers;
        this.productRepository = productRepository;
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public SqlImportService createSqlImportService() {
        return new SqlImportService(entityToDtoMappers, productRepository, applicationConfig);
    }
}