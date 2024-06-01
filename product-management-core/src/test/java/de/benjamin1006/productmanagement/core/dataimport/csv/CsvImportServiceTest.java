package de.benjamin1006.productmanagement.core.dataimport.csv;

import de.benjamin1006.productmanagement.datamodel.interfaces.strategy.csv.ICsvMapperStrategy;
import de.benjamin1006.productmanagement.datamodel.dto.Cheese;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.dto.Wine;
import de.benjamin1006.productmanagement.datamodel.exception.CsvParsingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CsvImportServiceTest {

    private CsvImportService cut;
    private ICsvMapperStrategy iCsvMapperStrategy;
    private ICsvMapperStrategy iCsvMapperStrategy1;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        iCsvMapperStrategy = Mockito.mock(ICsvMapperStrategy.class);
        iCsvMapperStrategy1 = Mockito.mock(ICsvMapperStrategy.class);

        //Stell die Mockito Objekts bereit
        autoCloseable = MockitoAnnotations.openMocks(this);

        //Es wird eine Liste erstellt mit den oben deklarierte ICsvMapperStrategy Objekten
        List<ICsvMapperStrategy> csvMappers = new ArrayList<>();
        csvMappers.add(iCsvMapperStrategy);
        csvMappers.add(iCsvMapperStrategy1);

        //Nun instanziieren wir die zu testende Klasse, dafür übergeben wir die vorher erstellte Liste.
        cut = new CsvImportService(csvMappers);
    }

    /**
     * Test den Ablauf der Methode importDataAndParseToProduct.
     * Dabei werden die Interaktionen mit den Mappern weggemocked und wir testen dort nur die Anzahl.
     * Zusätzlich wird überprüft, ob die erstellten Objekte sich auch in dem Rückgabewert der Methode befinden.
     */
    @Test
    void testImportDataAndParseToProduct() {
        //Per Reflection setzen wir nun das normalerweise per Spring Boot befüllte csvFilePath Feld
        ReflectionTestUtils.setField(cut, "csvFilePath", "src/test/resources/test.csv");

        //Zum Schluss erstellen wir noch zwei Objekte die uns für die Tests dienen
        Product cheese = new Cheese("kaese", -1, LocalDate.now().plusDays(50), 1.0, 0.5);

        Product wine = new Wine("wein", 1, 5.9, LocalDate.now());

        when(iCsvMapperStrategy.getMapperType()).thenReturn("wein");
        when(iCsvMapperStrategy.mapTo(any(String[].class))).thenReturn(wine);

        when(iCsvMapperStrategy1.getMapperType()).thenReturn("kaese");
        when(iCsvMapperStrategy1.mapTo(any(String[].class))).thenReturn(cheese);

        List<Product> products = cut.importDataAndParseToProduct();

        assertThat(products)
                .describedAs("Die zurückgegebene Liste sollte zwei Objekte vom Typ Produkt haben! Tatsächlicher Wert: " + products.size())
                .hasSize(2)
                .describedAs("Bei den Objekten sollte es sich um die deklarierten Objekte cheese und wine handeln!")
                .contains(cheese, wine);

        // Wir verifizieren die Interaktionen mit den von uns erstellten Mocks.
        verify(iCsvMapperStrategy, times(2)).getMapperType();
        verify(iCsvMapperStrategy, times(1)).mapTo(any(String[].class));
        verify(iCsvMapperStrategy1, times(1)).getMapperType();
        verify(iCsvMapperStrategy1, times(1)).mapTo(any(String[].class));

    }

    @Test
    void testImportDataAndParseToProductWithCsvException() {

        //Per Reflection setzen wir nun das normalerweise per Spring Boot befüllte csvFilePath Feld
        ReflectionTestUtils.setField(cut, "csvFilePath", "this/is/not/a/valid/path.csv");

        assertThatThrownBy(() -> cut.importDataAndParseToProduct()).isInstanceOf(CsvParsingException.class)
                .hasMessageContaining("(The system cannot find the path specified)");
    }

    @Test
    void testImPortDataNadParseToProductNotACsvFile() {
        String filePath = "this/is/not/a/valid/path";
        ReflectionTestUtils.setField(cut, "csvFilePath", filePath);
        assertThatThrownBy(() -> cut.importDataAndParseToProduct()).isInstanceOf(CsvParsingException.class)
                .hasMessage("Not a csv file! " + filePath);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
}
