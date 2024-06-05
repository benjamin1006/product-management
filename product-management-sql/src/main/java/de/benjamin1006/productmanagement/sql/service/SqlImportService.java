package de.benjamin1006.productmanagement.sql.service;

import de.benjamin1006.productmanagement.core.component.IDataImport;
import de.benjamin1006.productmanagement.core.component.strategy.IEntityMapperStrategy;
import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import de.benjamin1006.productmanagement.core.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementierung des {@link IDataImport} Interfaces, für den Import Typ SQL.
 * @author Benjamin Woitczyk
 */
public class SqlImportService implements IDataImport {

    private static final Logger log = LoggerFactory.getLogger(SqlImportService.class);

    private final List<IEntityMapperStrategy> entityToDtoMappers;
    private final ProductRepository productRepository;
    private final ApplicationConfig applicationConfig;

    public SqlImportService(List<IEntityMapperStrategy> entityToDtoMappers, ProductRepository productRepository, ApplicationConfig applicationConfig) {
        this.entityToDtoMappers = entityToDtoMappers;
        this.productRepository = productRepository;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public boolean isActive() {
        return applicationConfig.isSqlImportActive();
    }

    @Override
    public List<ProductDto> importDataAndParseToProduct() {

        final List<ProductEntity> all = productRepository.findAll();

        List<ProductDto> productList = new ArrayList<>();

        all.forEach(productEntity ->
                mapToProduct(productEntity)
                        .ifPresentOrElse(productList::add,
                                () -> log.info("Die aktuelle Zeile enthält ein Produkt das bisher nicht bekannt ist, sie wird übersprungen! {}", productEntity)));

        return productList;
    }

    private Optional<ProductDto> mapToProduct(ProductEntity productEntity) {
        return entityToDtoMappers.stream()
                .filter(iCsvMapperStrategy -> iCsvMapperStrategy.getMapperType().equals(productEntity.getType()))
                .findAny()
                .map(iCsvMapperStrategy -> iCsvMapperStrategy.mapEntityToDto(productEntity));
    }
}
