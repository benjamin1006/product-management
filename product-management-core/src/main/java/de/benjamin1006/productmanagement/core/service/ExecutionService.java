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
 * Klasse zum Ausführen der Verarbeitungslogik.
 * Je nach property Konfiguration wird zusätzlich noch UserInput abgefragt.
 * @author Benjamin Woitczyk
 */
@Service
public class ExecutionService {

    private static final Logger log = LoggerFactory.getLogger(ExecutionService.class);
    private final IEventManager eventManager;
    private final DataImportService dataImportService;
    private final ProductProcessingService productProcessingService;
    private final ApplicationConfig applicationConfig;
    private final UserInputService userInputService;

    public ExecutionService(IEventManager eventManager, DataImportService dataImportService,
                            ProductProcessingService productProcessingService, ApplicationConfig applicationConfig,
                            UserInputService userInputService) {
        this.eventManager = eventManager;
        this.dataImportService = dataImportService;
        this.productProcessingService = productProcessingService;
        this.applicationConfig = applicationConfig;
        this.userInputService = userInputService;
    }

    /**
     * Methode zum Importieren und Verarbeiten von Daten.
     */
    public void importDataAndProcess() {
        setupEventManager();
        List<ProductDto> filteredProductList = importDataAndRemoveLowQualityProducts();

        if (filteredProductList.isEmpty()) {
            log.error("Es wurden keine Daten zum importieren gefunden! Programmablauf endet hier!");
            return;
        }

        processProducts(filteredProductList);

        log.info("Der Programmablauf wird nun beendet!");
    }

    /**
     * Importiert die Daten und entfernt gleichzeitig Produkte, die nicht den vorgegebenen Parametern entsprechen.
     * @return eine Liste vom Type ProductDto
     */
    private List<ProductDto> importDataAndRemoveLowQualityProducts() {
        final List<ProductDto> productDtoList = dataImportService.importDataAndParseToProduct();

        return productDtoList.stream()
                .filter(product -> !productProcessingService.removeLowQualityOrExpiredProduct(product))
                .toList();
    }

    /**
     * Verarbeiter die Produkte auf grundlage der Verarbeitungsregeln.
     * Je nach Konfiguration wird hier auch UserInput zur Anzahl der Tage, für die die Verarbeitung ausgeführt werden soll.
     * @param filteredProductList Eine Liste mit importierten Daten.
     */
    private void processProducts(List<ProductDto> filteredProductList) {
        eventManager.notifyProductListObservers(CREATE, filteredProductList);

        int timePeriod = getTimePeriod();

        while (timePeriod != 0) {
            filteredProductList = productProcessingService.processProductsForTimePeriod(filteredProductList, timePeriod);

            if (!applicationConfig.isInteractiveMode()) {
                break;
            }

            timePeriod = userInputService.getUserInput();
        }
    }

    private void setupEventManager() {
        eventManager.subscribe(new NotificationService(), CREATE, UPDATE, REMOVE, NEW_DAY);
    }

    private int getTimePeriod() {
        return applicationConfig.isInteractiveMode() ? userInputService.getUserInput() : applicationConfig.getTimePeriod();
    }
}
