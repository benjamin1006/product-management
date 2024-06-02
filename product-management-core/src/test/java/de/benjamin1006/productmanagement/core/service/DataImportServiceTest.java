package de.benjamin1006.productmanagement.core.service;

import de.benjamin1006.productmanagement.core.component.IDataImport;
import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DataImportServiceTest {

    private DataImportService dataImportService;
    private IDataImport dataImport;

    @BeforeEach
    public void setup() {

        dataImport = Mockito.mock(IDataImport.class);

        List<IDataImport> dataImportList = List.of(dataImport);

        dataImportService = new DataImportService(dataImportList);
    }

    @Test
    void testImportDataAndParseToProduct() {
        List<ProductDto> mockProductDtoList = new ArrayList<>();
        ProductDto productDto = new CheeseDto();
        mockProductDtoList.add(productDto);

        when(dataImport.isActive()).thenReturn(true);
        when(dataImport.importDataAndParseToProduct()).thenReturn(mockProductDtoList);

        List<ProductDto> returnedProductDtoList = dataImportService.importDataAndParseToProduct();

        verify(dataImport, times(1)).isActive();
        verify(dataImport, times(1)).importDataAndParseToProduct();

        assertThat(returnedProductDtoList)
                .describedAs("Die Liste sollte nicht null sein")
                .isNotNull()
                .describedAs("Die Liste sollte die Größe eines haben")
                .hasSize(1)
                .describedAs("Die Liste sollte aus dem zuvor erstellen Objekt vom Typ Product bestehen!")
                .contains(productDto);
    }
}