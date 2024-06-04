package de.benjamin1006.productmanagement.sql.mapper;

import de.benjamin1006.productmanagement.core.component.strategy.IEntityMapperStrategy;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import de.benjamin1006.productmanagement.core.repository.wine.WineEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Benjamin Woitczyk
 */
@Component
public class WineEntityMapperStrategy implements IEntityMapperStrategy {

    private final ICurrentDayProvider currentDayProvider;

    public WineEntityMapperStrategy(ICurrentDayProvider currentDayProvider) {
        this.currentDayProvider = currentDayProvider;
    }

    @Override
    public String getMapperType() {
        return "wein";
    }

    @Override
    public ProductDto mapEntityToDto(ProductEntity entity) {
        final WineEntity wineEntity = (WineEntity) entity;
        wineEntity.setEntryDate(currentDayProvider.getCurrentDay());
        WineDto wine = new WineDto();
        BeanUtils.copyProperties(wineEntity, wine);
        return wine;
    }
}
