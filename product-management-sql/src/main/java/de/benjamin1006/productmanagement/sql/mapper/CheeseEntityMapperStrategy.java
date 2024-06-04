package de.benjamin1006.productmanagement.sql.mapper;

import de.benjamin1006.productmanagement.core.component.strategy.IEntityMapperStrategy;
import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import de.benjamin1006.productmanagement.core.repository.cheese.CheeseEntity;
import de.benjamin1006.productmanagement.core.util.CheeseDtoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


/**
 * @author Benjamin Woitczyk
 */
@Component
public class CheeseEntityMapperStrategy implements IEntityMapperStrategy {

    @Override
    public String getMapperType() {
        return "käse";
    }

    @Override
    public ProductDto mapEntityToDto(ProductEntity entity) {
        final CheeseEntity cheeseEntity = (CheeseEntity) entity;
        cheeseEntity.setBasePrice(cheeseEntity.getPrice());
        cheeseEntity.setPrice(CheeseDtoUtil.calculateFirstDayPrice(cheeseEntity.getBasePrice(), cheeseEntity.getQuality()));
        cheeseEntity.setExpirationDate(CheeseDtoUtil.getRandomExpirationDate());
        CheeseDto cheese = new CheeseDto();
        BeanUtils.copyProperties(cheeseEntity, cheese);
        return cheese;
    }

}
