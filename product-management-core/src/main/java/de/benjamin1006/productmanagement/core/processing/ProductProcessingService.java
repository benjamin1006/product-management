package de.benjamin1006.productmanagement.core.processing;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.exception.ProductNotFoundException;
import de.benjamin1006.productmanagement.core.observer.manager.IEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static de.benjamin1006.productmanagement.core.observer.EventType.REMOVE;
import static de.benjamin1006.productmanagement.core.observer.EventType.UPDATE;

/**
 * Service für die Verarbeitung von Produkten, nutzt das StrategyPattern für die Verarbeitung({@link IProductProcessingStrategy}
 * und das ObserverPattern für das Versenden von Notifications
 *
 * @author Benjamin Woitczyk
 */
@Service
public class ProductProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ProductProcessingService.class);

    private final List<IProductProcessingStrategy> iProductProcessingStrategies;
    private final IEventManager productManagementEventManager;
    private final ICurrentDayProvider currentDayProvider;

    public ProductProcessingService(List<IProductProcessingStrategy> iProductProcessingStrategies,
                                    IEventManager productManagementEventManager,
                                    ICurrentDayProvider currentDayProvider) {
        this.iProductProcessingStrategies = iProductProcessingStrategies;
        this.productManagementEventManager = productManagementEventManager;
        this.currentDayProvider = currentDayProvider;
    }

    public void processProductsForTimePeriod(List<ProductDto> productDtoList, int timePeriod) {

        log.info("Verarbeitungslogik aller Produkte wird für einen Zeitraum von {} Tagen angewandt", timePeriod);
        for (int i = 0; i < timePeriod; i++) {
            currentDayProvider.setCurrentDay(LocalDate.now().plusDays(i + 1L));
            productDtoList = processProductsForOneDay(productDtoList);
        }

        productManagementEventManager.notifyProductListObservers(UPDATE, productDtoList);
    }

    /**
     * Führt die Verarbeitung von Produkten für einen kompletten Tag durch, dafür wird die {@link IProductProcessingStrategy}
     * genutzt. In dieser Methode werden sowohl die Qualität als auch der Preis angepasst. Zusätzlich werden abgelaufene oder
     * unter die Qualitätsgrenze gefallene Produkte entfernt.
     * Zusätzlich werden mithilfe des ObserverPatterns, die Produkte ausgegeben.
     *
     * @param productDtoList die Liste für die die Verarbeitung durchgeführt werden soll
     * @return die verarbeitete Liste ohne Produkte die entfernt werden müssen
     */
    public List<ProductDto> processProductsForOneDay(List<ProductDto> productDtoList) {

        productDtoList = productDtoList.stream()
                .filter(product -> {
                    updateQualityAndPrice(product);
                    final boolean removeProduct = removeLowQualityOrExpiredProduct(product);
                    if (removeProduct) {
                        productManagementEventManager.notifyProductObservers(REMOVE, product);
                    } else {
                        productManagementEventManager.notifyProductObservers(UPDATE, product);
                    }
                    //Wir drehen hier den booleschen Wert um, da wird die Produkte ja aus der Liste entfernen wollen,
                    // die nicht mehr den Rahmenbedingungen entsprechen
                    return !removeProduct;
                }).toList();

        return productDtoList;
    }

    /**
     * Überprüft, ob das übergebene Produkt abgelaufene oder unter die Qualitätsgrenze gefallen ist.
     *
     * @param productDto das zu überprüfen Produkt
     * @return true, wenn das Produkt entfernt werden soll oder false, wenn es nicht entfernt werden soll.
     */
    public boolean removeLowQualityOrExpiredProduct(ProductDto productDto) {
        final Optional<Boolean> removeProduct = iProductProcessingStrategies.stream()
                .filter(iProductProcessingStrategy -> iProductProcessingStrategy.isCorrectType(productDto))
                .findAny()
                .map(iProductProcessingStrategy -> iProductProcessingStrategy.removeProduct(productDto));

        return removeProduct
                .orElseThrow(() -> new ProductNotFoundException(String.format("Es gibt keine ProductProcessingStrategy Implementierung für das Product vom Typ %s!", productDto.getType())));
    }

    /**
     * In dieser Methode werden sowohl die Qualität als auch der Preis angepasst
     *
     * @param productDto das anzupassende Produkt
     */
    private void updateQualityAndPrice(ProductDto productDto) {
        iProductProcessingStrategies.stream()
                .filter(iProductProcessingStrategy -> iProductProcessingStrategy.isCorrectType(productDto))
                .findAny()
                .ifPresent(iProductProcessingStrategy -> {
                    productDto.setQuality(iProductProcessingStrategy.calculateQuality(productDto));
                    productDto.setPrice(iProductProcessingStrategy.calculateDayPrice(productDto));
                });

    }
}
