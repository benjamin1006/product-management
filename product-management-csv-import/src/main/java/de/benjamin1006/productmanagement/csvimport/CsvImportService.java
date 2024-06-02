package de.benjamin1006.productmanagement.csvimport;

import de.benjamin1006.productmanagement.core.component.IDataImport;
import de.benjamin1006.productmanagement.core.component.strategy.ICsvMapperStrategy;
import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.csvimport.repository.CsvHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Mapped eine übergebene Csv-Datei auf die vorhandenen Product Objekte, dafür werden Strategien benutzt, welche das Interface
 * ICsvMapperStrategy implementieren.
 *
 * @author Benjamin Woitczyk
 */
@Component
public class CsvImportService implements IDataImport {

    private static final Logger log = LoggerFactory.getLogger(CsvImportService.class);

    private final List<ICsvMapperStrategy> csvMapperStrategies;
    private final ApplicationConfig applicationConfig;

    public CsvImportService(List<ICsvMapperStrategy> csvMapperStrategies,
                            ApplicationConfig applicationConfig) {
        this.csvMapperStrategies = csvMapperStrategies;
        this.applicationConfig = applicationConfig;
    }

    /**
     * Methode zum Mappen der konfigurierten Csv Datei zu einer Liste vom Typ Product.
     *
     * @return eine Liste vom Typ Product
     */
    @Override
    public List<ProductDto> importDataAndParseToProduct() {
        List<ProductDto> productDtos = new ArrayList<>();

        CsvHelper.readCsvData(applicationConfig.getCsvFilePath())
                .forEach(line -> mapToProduct(line)
                        .ifPresentOrElse(productDtos::add,
                                () -> log.info("Die aktuelle Zeile enthält ein Produkt das bisher nicht bekannt ist, sie wird übersprungen! {}", Arrays.toString(line))));

        return productDtos;
    }

    @Override
    public boolean isActive() {

        return applicationConfig.isCsvImportActive();
    }

    /**
     * Filtert die Liste der csvMapperStrategies anhand des Types, und mapped dann mit der gefunden MapperStrategie das übergebene String Array
     * auf das passende Objekt vom Typ Product.
     *
     * @param line die zu mappende String Array/Zeile aus der csv Datei
     * @return ein Optional mit dem gemappten Objekt vom Typ Product
     */
    private Optional<ProductDto> mapToProduct(String[] line) {
        return csvMapperStrategies.stream()
                .filter(iCsvMapperStrategy -> iCsvMapperStrategy.getMapperType().equals(line[0]))
                .findAny()
                .map(iCsvMapperStrategy -> iCsvMapperStrategy.mapTo(line));
    }
}