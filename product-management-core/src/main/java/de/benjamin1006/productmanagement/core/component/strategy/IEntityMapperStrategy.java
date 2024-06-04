package de.benjamin1006.productmanagement.core.component.strategy;

import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;

/**
 * @author Benjamin Woitczyk
 */
public interface IEntityMapperStrategy {
    String getMapperType();

    ProductDto mapEntityToDto(ProductEntity entity);
}
