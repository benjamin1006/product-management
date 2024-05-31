package de.benjamin1006.productmanagement.solution;

import de.benjamin1006.productmanagement.core.CoreConfig;
import de.benjamin1006.productmanagement.core.dataimport.IDataImport;
import de.benjamin1006.productmanagement.core.notification.NotificationService;
import de.benjamin1006.productmanagement.core.processing.ProductProcessingService;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.observer.ObserverConfig;
import de.benjamin1006.productmanagement.observer.manager.IEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static de.benjamin1006.productmanagement.observer.EventType.*;

@SpringBootApplication
@Import({CoreConfig.class, ObserverConfig.class})
public class ProductManagementSolutionApplication implements CommandLineRunner {

    @Value("${product-management.period}")
    private int period;

    private static final Logger log = LoggerFactory.getLogger(ProductManagementSolutionApplication.class);

    private final ICurrentDayProvider currentDayProvider;
    private final IDataImport csvImportService;
    private final ProductProcessingService productProcessingService;
    private final IEventManager productManagementEventManager;

    public ProductManagementSolutionApplication(ICurrentDayProvider currentDayProvider,
                                                IDataImport csvImportService,
                                                ProductProcessingService productProcessingService,
                                                IEventManager productManagementEventManager) {
        this.currentDayProvider = currentDayProvider;
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

        log.info("Verarbeitungslogik aller Produkte wird für einen Zeitraum von {} Tagen angewandt", period);
        for (int i = 0; i < period; i++) {
            currentDayProvider.setCurrentDay(LocalDate.now().plusDays(i + 1L));
            filteredProductList = productProcessingService.processProductsForOneDay(filteredProductList);
        }

    }


}
