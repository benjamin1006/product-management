package de.benjamin1006.productmanagement.core.service;

import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.observer.manager.IEventManager;
import de.benjamin1006.productmanagement.core.processing.NotificationService;
import de.benjamin1006.productmanagement.core.processing.ProductProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.benjamin1006.productmanagement.core.observer.EventType.*;

/**
 * @author Benjamin Woitczyk
 */
@Service
public class ExecutionService {

    private static final Logger log = LoggerFactory.getLogger(ExecutionService.class);
    private final IEventManager eventManager;
    private final DataImportService dataImportService;
    private final ProductProcessingService productProcessingService;
    private final ApplicationConfig applicationConfig;


    public ExecutionService(IEventManager eventManager, DataImportService dataImportService, ProductProcessingService productProcessingService, ApplicationConfig applicationConfig) {
        this.eventManager = eventManager;
        this.dataImportService = dataImportService;
        this.productProcessingService = productProcessingService;
        this.applicationConfig = applicationConfig;
    }

    public void importDataAndProcess() {
        eventManager.subscribe(new NotificationService(), CREATE, UPDATE, REMOVE, NEW_DAY);

        final List<ProductDto> productDtoList = dataImportService.importDataAndParseToProduct();
        if (productDtoList.isEmpty()) {
            log.error("Es wurden keine Daten zum importieren gefunden! Programmablauf endet hier!");

            return;
        }
        List<ProductDto> filteredProductDtoList = productDtoList.stream()
                .filter(product -> !productProcessingService.removeLowQualityOrExpiredProduct(product))
                .toList();
        eventManager.notifyProductListObservers(CREATE, filteredProductDtoList);

        productProcessingService.processProductsForTimePeriod(filteredProductDtoList, applicationConfig.getTimePeriod());
    }

}
