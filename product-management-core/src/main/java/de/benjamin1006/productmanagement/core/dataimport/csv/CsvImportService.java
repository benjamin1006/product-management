package de.benjamin1006.productmanagement.core.dataimport.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import de.benjamin1006.productmanagement.core.dataimport.IDataImport;
import de.benjamin1006.productmanagement.core.dataimport.csv.strategies.ICsvMapperStrategy;
import de.benjamin1006.productmanagement.core.dto.Product;
import de.benjamin1006.productmanagement.core.exception.CsvParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
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

    @Value("${product-management.csvFilePath}")
    private String csvFilePath;

    private final List<ICsvMapperStrategy> csvMapperStrategies;

    public CsvImportService(List<ICsvMapperStrategy> csvMapperStrategies) {
        this.csvMapperStrategies = csvMapperStrategies;
    }

    /**
     * Methode zum Mappen der konfigurierten Csv Datei zu einer Liste vom Typ Product.
     *
     * @return eine Liste vom Typ Product
     */
    @Override
    public List<Product> importDataAndParseToProduct() {

        if (!isCsvFile()) {
            throw new CsvParsingException("Not a csv file! " + csvFilePath);
        }

        List<Product> products = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String[] currentLine = line;
                mapToProduct(currentLine)
                        .ifPresentOrElse(products::add,
                        () -> log.info("Die aktuelle Zeile enthält ein Produkt das bisher nicht bekannt ist, sie wird übersprungen! {}", Arrays.toString(currentLine)));
            }

        } catch (CsvValidationException | IOException e) {
            throw new CsvParsingException(e.getMessage());
        }
        return products;
    }

    /**
     * Filtert die Liste der csvMapperStrategies anhand des Types, und mapped dann mit der gefunden MapperStrategie das übergebene String Array
     * auf das passende Objekt vom Typ Product.
     *
     * @param line die zu mappende String Array/Zeile aus der csv Datei
     * @return ein Optional mit dem gemappten Objekt vom Typ Product
     */
    private Optional<Product> mapToProduct(String[] line) {
        return csvMapperStrategies.stream()
                .filter(iCsvMapperStrategy -> iCsvMapperStrategy.getMapperType().equals(line[0]))
                .findAny()
                .map(iCsvMapperStrategy -> iCsvMapperStrategy.mapTo(line));
    }

    private boolean isCsvFile() {
        final String extension = csvFilePath.substring(csvFilePath.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("csv");
    }
}