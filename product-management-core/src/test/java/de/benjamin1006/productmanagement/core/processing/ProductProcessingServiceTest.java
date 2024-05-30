package de.benjamin1006.productmanagement.core.processing;

import de.benjamin1006.productmanagement.core.CoreConfig;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.datamodel.dto.Cheese;
import de.benjamin1006.productmanagement.datamodel.dto.Product;
import de.benjamin1006.productmanagement.datamodel.dto.Wine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Benjamin Woitczyk
 */
@SpringBootTest(classes = CoreConfig.class)
class ProductProcessingServiceTest {

    @Autowired
    private ProductProcessingService cut;
    @Autowired
    private ICurrentDayProvider currentDayProvider;

    private final LocalDate aktuellerTag = LocalDate.now();

    /**
     * Test für die Methode {@link ProductProcessingService#processProductsForOneDay(List)}, einen Tag nach der Eintragung der Produkte.
     * Erwartet wird hier, dass die Methode den Preis und die Qualität des Käses anpasst. Der Wein sollte unverändert bleiben.
     */
    @Test
    void testProcessProductsForOneDay() {
        //Der aktuelle Tag wird auf einen Tag in die Zukunft gesetzt
        currentDayProvider.setCurrentDay(aktuellerTag.plusDays(1L));

        //Es werden je ein Produkt vom Typ Wine und Cheese erstellt, beide Produkte passen von den Rahmenbedingungen in den Ablauf.
        Product wine = new Wine("wein", 10, 20, aktuellerTag);
        Product cheese = new Cheese("käse", 31, aktuellerTag.plusDays(50), 1.99);
        List<Product> products = List.of(wine, cheese);

        List<Product> result = cut.processProductsForOneDay(products);

        assertThat(result)
                .describedAs("Da zwei passende Produkte übergeben werden, sollten auch beide wieder zurückkommen.")
                .hasSize(2);
        assertThat(result.get(0).getQuality())
                .describedAs("Die Qualität des Weins sollte sich nicht verändert haben, da wir nur einen Tag in der Zukunft sind.")
                .isEqualTo(10);
        assertThat(result.get(0).getPrice())
                .describedAs("Die Preis des Weins sollte sich nicht verändert haben.")
                .isEqualTo(20.0);
        assertThat(result.get(1).getQuality())
                .describedAs("Der Käse sollte einen Qualitätspunkt verloren haben.")
                .isEqualTo(30);
        assertThat(cheese.getPrice())
                .describedAs("Der Käse sollte seinen Preis veränderten Preis haben.")
                .isEqualTo(4.99);

    }

    /**
     * Test für die Methode {@link ProductProcessingService#processProductsForOneDay(List), zehn Tage nach der Eintragung der Produkte.
     * Erwartet wird hier, dass die Methode den Preis und die Qualität des Käses anpasst. Der Wein sollte seine Qualität
     * erhöht haben.
     */
    @Test
    void testProcessProductsForOneDayAfter10Days() {
        //Der aktuelle Tag wird auf zehn Tage in die Zukunft gesetzt
        currentDayProvider.setCurrentDay(aktuellerTag.plusDays(10L));

        //Es werden je ein Produkt vom Typ Wine und Cheese erstellt, beide Produkte passen von den Rahmenbedingungen in den Ablauf.
        Product product1 = new Wine("wein", 10, 20, aktuellerTag);
        Product product2 = new Cheese("käse", 31, aktuellerTag.plusDays(50), 1.99);
        List<Product> products = List.of(product1, product2);

        List<Product> result = cut.processProductsForOneDay(products);

        assertThat(result)
                .describedAs("Da zwei passende Produkte übergeben werden, sollten auch beide wieder zurückkommen.")
                .hasSize(2);
        assertThat(result.get(0).getQuality())
                .describedAs("Die Qualität des Weins sollte sich  verändert haben, da wir zehn Tage in der Zukunft sind.")
                .isEqualTo(11);
        assertThat(result.get(0).getPrice())
                .describedAs("Die Preis des Weins sollte sich nicht verändert haben.")
                .isEqualTo(20.0);
        assertThat(result.get(1).getQuality())
                .describedAs("Der Käse sollte einen Qualitätspunkt verloren haben.")
                .isEqualTo(30);
        assertThat(result.get(1).getPrice())
                .describedAs("Der Käse sollte seinen Preis veränderten Preis haben.")
                .isEqualTo(4.99);

    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(Product)}.
     * Es wird ein Käse übergeben, der die Qualitätsgrenze von 30 unterscheidet, daher wird erwartet, dass die Methode true
     * zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForLowQualityCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        Product product = new Cheese("käse", 10, aktuellerTag.plusDays(50), 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(product);

        assertThat(result)
                .describedAs("Das Produkt sollte entfernt werden, da es nicht den Qualitätsansprüchen von Käse genügt ")
                .isTrue();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(Product)}.
     * Es wird ein Käse übergeben, der das Verfallsdatum überschritten hat, daher wird erwartet, dass die Methode true
     * zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForExpiredCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        Product product = new Cheese("käse", 30, aktuellerTag.minusDays(1), 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(product);

        assertThat(result)
                .describedAs("Das Produkt sollte entfernt werden, da es abgelaufen ist ")
                .isTrue();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(Product)}.
     * Es wird ein Käse innerhalb der Rahmenbedingungen übergeben, daher wird erwartet, dass die Methode
     * false zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForGoodCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        Product product = new Cheese("käse", 30, aktuellerTag.plusDays(50), 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(product);

        assertThat(result)
                .describedAs("Das Produkt sollte nicht entfernt werden, da es den Qualitätsansprüchen von Käse genügt ")
                .isFalse();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(Product)}.
     * Es wird ein Käse innerhalb der Rahmenbedingungen übergeben, daher wird erwartet, dass die Methode
     * false zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForNotExpiredCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        Product product = new Cheese("käse", 30, aktuellerTag, 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(product);

        assertThat(result)
                .describedAs("Das Produkt sollte nicht entfernt werden, da das Verfallsdatum noch nicht überschritten ist")
                .isFalse();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(Product)}.
     * Es wird ein Wein unterhalb der Qualitätsgrenze übergeben, daher wird erwartet, dass die Methode true zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForLowQualityWine() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        Product product = new Cheese("wine", -1, aktuellerTag.plusDays(50), 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(product);

        assertThat(result)
                .describedAs("Das Produkt sollte entfernt werden, da es nicht den Qualitätsansprüchen von Wein genügt ")
                .isTrue();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(Product)}.
     * Es wird ein Wein innerhalb der Qualitätsgrenze übergeben, daher wird erwartet, dass die Methode false zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForGoodyWine() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        Product product = new Wine("wine", 0, 1.99, aktuellerTag);

        boolean result = cut.removeLowQualityOrExpiredProduct(product);

        assertThat(result)
                .describedAs("Das Produkt sollte nicht entfernt werden, da es den Qualitätsansprüchen von Wein genügt ")
                .isFalse();
    }
}