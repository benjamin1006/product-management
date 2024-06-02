package de.benjamin1006.productmanagement.csvimport.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import de.benjamin1006.productmanagement.core.exception.CsvParsingException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class CsvHelper {

    private CsvHelper() {
    }

    public static List<String[]> readCsvData(String csvFilePath) {

        if (!isCsvFile(csvFilePath)) {
            throw new CsvParsingException("Not a csv file! " + csvFilePath);
        }

        List<String[]> data = new ArrayList<>();


        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {

                data.add(line);
            }

        } catch (CsvValidationException | IOException e) {
            throw new CsvParsingException(e.getMessage());
        }
        return data;
    }

    private static boolean isCsvFile(String csvFilePath) {
        final String extension = csvFilePath.substring(csvFilePath.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("csv");
    }
}
