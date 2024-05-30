package de.benjamin1006.productmanagement.solution;

import de.benjamin1006.productmanagement.core.CoreConfig;
import de.benjamin1006.productmanagement.core.dataimport.IDataImport;
import de.benjamin1006.productmanagement.core.processing.ProductProcessingService;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@Import({CoreConfig.class})
public class ProductManagementSolutionApplication implements CommandLineRunner {

    @Value("${product-management.period}")
    private int period;

    private static final Logger log = LoggerFactory.getLogger(ProductManagementSolutionApplication.class);

    private final ICurrentDayProvider currentDayProvider;
    private final IDataImport csvImportService;
    private final ProductProcessingService productProcessingService;

    public ProductManagementSolutionApplication(ICurrentDayProvider currentDayProvider, IDataImport csvImportService, ProductProcessingService productProcessingService) {
        this.currentDayProvider = currentDayProvider;
        this.csvImportService = csvImportService;
        this.productProcessingService = productProcessingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementSolutionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        currentDayProvider.setCurrentDay(LocalDate.now());
        final List<Product> productList = csvImportService.importDataAndParseToProduct();
        log.info("----------STARTWERTE ALLER PRODUKTE----------");
        List<Product> filteredProductList = productList.stream()
                .filter(product1 -> !productProcessingService.removeLowQualityOrExpiredProduct(product1))
                .toList();
        filteredProductList.forEach(product -> log.info(product.toString()));
        log.info("Verarbeitungslogik aller Produkte wird für einen Zeitraum von {} Tagen angewandt", period);
        for (int i = 0; i < period; i++) {
            currentDayProvider.setCurrentDay(LocalDate.now().plusDays(i + 1L));
            filteredProductList = productProcessingService.processProductsForOneDay(filteredProductList);
        }

    }


}
