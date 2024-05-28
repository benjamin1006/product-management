package de.benjamin1006.productmanagement.core.dataimport;

import de.benjamin1006.productmanagement.core.dto.Product;

import java.util.List;

/**
 * Interface für das Mappen von verschiedenen Datenquelle auf eine List mit Objekten vom Typ Product
 * @author Benjamin Woitczyk
 */
public interface IDataImport {
    List<Product> importDataAndParseToProduct();
}
