package de.benjamin1006.productmanagement.core.service;

import de.benjamin1006.productmanagement.core.component.IDataImport;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin Woitczyk
 */
@Service
public class DataImportService {

    private final List<IDataImport> dataImportList;

    public DataImportService(List<IDataImport> dataImportList) {
        this.dataImportList = dataImportList;
    }

    public List<ProductDto> importDataAndParseToProduct() {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (IDataImport dataImport : dataImportList) {
            if (dataImport.isActive()) {

                productDtoList = dataImport.importDataAndParseToProduct();
            }
        }
        return productDtoList;
    }
}
