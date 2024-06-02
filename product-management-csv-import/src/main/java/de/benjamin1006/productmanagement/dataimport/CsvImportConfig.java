package de.benjamin1006.productmanagement.dataimport;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Benjamin Woitczyk
 */
@ConditionalOnProperty(prefix = "product-management", name = "import", havingValue = "csv")
@ComponentScan({
        "de.benjamin1006.productmanagement.dataimport.csv.strategies",
        "de.benjamin1006.productmanagement.dataimport.csv"
})
public class CsvImportConfig {
}
