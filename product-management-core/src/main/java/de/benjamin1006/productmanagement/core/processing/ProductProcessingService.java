package de.benjamin1006.productmanagement.core.processing;

import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.processing.strategies.IProductProcessingStrategy;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Benjamin Woitczyk
 */
@Service
public class ProductProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ProductProcessingService.class);

    private final List<IProductProcessingStrategy> iProductProcessingStrategies;
    private final ICurrentDayProvider currentDayProvider;

    public ProductProcessingService(List<IProductProcessingStrategy> iProductProcessingStrategies,
                                    ICurrentDayProvider currentDayProvider) {
        this.iProductProcessingStrategies = iProductProcessingStrategies;
        this.currentDayProvider = currentDayProvider;
    }

    public List<Product> processProductsForOneDay(List<Product> productList) {

        productList.forEach(this::updateQualityAndPrice);

        log.info("--------PRODUKTE AM {}---------", currentDayProvider.getCurrentDay());
        productList = productList.stream().filter(product -> {
            final boolean b = removeLowQualityOrExpiredProduct(product);
            if (b) {
                log.info("{} Bitte ausräumen!", product);
            } else {
                log.info(product.toString());

            }
            return !b;
        }).toList();

        return productList;
    }

    public boolean removeLowQualityOrExpiredProduct(Product product) {
        final Optional<Boolean> removeProduct = iProductProcessingStrategies.stream()
                .filter(iProductProcessingStrategy -> iProductProcessingStrategy.isCorrectType(product))
                .findAny()
                .map(iProductProcessingStrategy -> iProductProcessingStrategy.removeProduct(product));

        return removeProduct
                .orElseThrow(() -> new ProductNotFoundException(String.format("Es gibt keine ProductProcessingStrategy Implementierung für das Product vom Typ %s!", product.getType())));
    }

    private void updateQualityAndPrice(Product product) {
        iProductProcessingStrategies.stream()
                .filter(iProductProcessingStrategy -> iProductProcessingStrategy.isCorrectType(product))
                .findAny()
                .ifPresent(iProductProcessingStrategy -> {
                    product.setQuality(iProductProcessingStrategy.calculateQuality(product));
                    product.setPrice(iProductProcessingStrategy.calculateDayPrice(product));
                });

    }
}
