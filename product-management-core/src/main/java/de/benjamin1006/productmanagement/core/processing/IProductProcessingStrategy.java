package de.benjamin1006.productmanagement.core.processing;


import de.benjamin1006.productmanagement.core.dto.ProductDto;

/**
 * @author Benjamin Woitczyk
 */
public interface IProductProcessingStrategy {

    boolean isCorrectType(Object product);

    int calculateQuality(ProductDto productDto);

    double calculateDayPrice(ProductDto productDto);

    boolean removeProduct(ProductDto productDto);
}
