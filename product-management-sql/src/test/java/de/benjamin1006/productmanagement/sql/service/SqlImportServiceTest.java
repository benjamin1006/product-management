package de.benjamin1006.productmanagement.sql.service;

import de.benjamin1006.productmanagement.core.component.strategy.IEntityMapperStrategy;
import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.repository.ProductEntity;
import de.benjamin1006.productmanagement.core.repository.ProductRepository;
import de.benjamin1006.productmanagement.core.repository.cheese.CheeseEntity;
import de.benjamin1006.productmanagement.core.repository.wine.WineEntity;
import de.benjamin1006.productmanagement.sql.mapper.CheeseEntityMapperStrategy;
import de.benjamin1006.productmanagement.sql.mapper.WineEntityMapperStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Benjamin Woitczyk
 */
class SqlImportServiceTest {

    private final IEntityMapperStrategy cheeseEntityMapperStrategy = new CheeseEntityMapperStrategy();
    private final ICurrentDayProvider currentDayProvider = Mockito.mock(ICurrentDayProvider.class);
    private final IEntityMapperStrategy wineEntityMapperStrategy = new WineEntityMapperStrategy(currentDayProvider);
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final ApplicationConfig applicationConfig = new ApplicationConfig();
    private final SqlImportService cut = new SqlImportService(List.of(cheeseEntityMapperStrategy, wineEntityMapperStrategy), productRepository, applicationConfig);

    /**
     * Test für die Methode {@link SqlImportService#SqlImportService(List, ProductRepository, ApplicationConfig)}.
     * Es wurde eine frische ApplicationConfig initialisiert, da diese keine Standardbelegung der Felder hat, sollte hier der
     * Value des Feldes sqlImportActive false sein.
     */
    @Test
    void testIsActive() {

        assertThat(cut.isActive())
                .describedAs("Die Methode sollte false zurückgeben, da die ApplicationConfig ohne initiale Wert initialisiert wurde.")
                .isFalse();
    }

    /**
     * Test für die Methode {@link SqlImportService#SqlImportService(List, ProductRepository, ApplicationConfig)}.
     */
    @Test
    void testIsActiveTrue() {
        applicationConfig.setSqlImportActive(true);

        assertThat(cut.isActive())
                .describedAs("Die Methode sollte true zurückgeben.")
                .isTrue();
    }


    /**
     * Test für die Metode {@link SqlImportService#importDataAndParseToProduct()}.
     * Die Entities haben ein Typ gesetzt, daher sollten sie von den Mapper erkannt werden. Es wird erwartet,
     * dass eine Liste mit zwei Objekten zurückgegeben wird.
     */
    @Test
    void importDataAndParseToProduct() {

        ProductEntity productEntity = new CheeseEntity();
        productEntity.setType("käse");
        ProductEntity wineEntity = new WineEntity();
        wineEntity.setType("wein");

        Mockito.when(productRepository.findAll()).thenReturn(List.of(productEntity, wineEntity));

        List<ProductDto> result = cut.importDataAndParseToProduct();

        assertThat(result)
                .describedAs("Es werden zwei Entities erstellt, die vom Mock Repository zurückgegeben werden, daher sollte die Liste eine Größe von 2 haben.")
                .hasSize(2);

    }

    /**
     * Test für die Metode {@link SqlImportService#importDataAndParseToProduct()}.
     * Die Entities haben keinen Typ gesetzt, daher sollten sie von den Mapper nicht erkannt werden. Es wird erwartet,
     * dass eine leere Liste zurückgegeben wird.
     */
    @Test
    void importDataAndParseToProductNoType() {
        ProductEntity productEntity = new CheeseEntity();
        ProductEntity wineEntity = new WineEntity();

        Mockito.when(productRepository.findAll()).thenReturn(List.of(productEntity, wineEntity));

        List<ProductDto> result = cut.importDataAndParseToProduct();

        assertThat(result)
                .describedAs("Die Liste sollte leer sein, da die Entities von den MapperStrategies nicht erkannt werden.")
                .isEmpty();

    }


}