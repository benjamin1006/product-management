package de.benjamin1006.productmanagement.solution;

import de.benjamin1006.productmanagement.processing.ProcessingConfig;
import de.benjamin1006.productmanagement.dataimport.CsvImportConfig;
import de.benjamin1006.productmanagement.core.interfaces.dataimport.IDataImport;
import de.benjamin1006.productmanagement.processing.notification.NotificationService;
import de.benjamin1006.productmanagement.processing.extecution.ProductProcessingService;
import de.benjamin1006.productmanagement.core.dto.Product;
import de.benjamin1006.productmanagement.fish.FishConfig;
import de.benjamin1006.productmanagement.observer.ObserverConfig;
import de.benjamin1006.productmanagement.observer.manager.IEventManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.List;

import static de.benjamin1006.productmanagement.observer.EventType.*;

@SpringBootApplication
@Import({ProcessingConfig.class, ObserverConfig.class, FishConfig.class, CsvImportConfig.class})
public class ProductManagementSolutionApplication implements CommandLineRunner {

    @Value("${product-management.time-period}")
    private int timePeriod;

    private final IDataImport csvImportService;
    private final ProductProcessingService productProcessingService;
    private final IEventManager productManagementEventManager;

    public ProductManagementSolutionApplication(IDataImport csvImportService,
                                                ProductProcessingService productProcessingService,
                                                IEventManager productManagementEventManager) {
        this.csvImportService = csvImportService;
        this.productProcessingService = productProcessingService;
        this.productManagementEventManager = productManagementEventManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementSolutionApplication.class, args);
    }

    @Override
    public void run(String... args) {
        productManagementEventManager.subscribe(new NotificationService(), CREATE, UPDATE, REMOVE, NEW_DAY);
        final List<Product> productList = csvImportService.importDataAndParseToProduct();
        List<Product> filteredProductList = productList.stream()
                .filter(product -> !productProcessingService.removeLowQualityOrExpiredProduct(product))
                .toList();
        productManagementEventManager.notifyProductListObservers(CREATE, filteredProductList);

        productProcessingService.processProductsForTimePeriod(filteredProductList, timePeriod);
    }
}
