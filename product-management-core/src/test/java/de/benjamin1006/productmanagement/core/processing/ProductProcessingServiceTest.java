package de.benjamin1006.productmanagement.core.processing;

import de.benjamin1006.productmanagement.core.TestApplication;
import de.benjamin1006.productmanagement.core.dto.ProductDto;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.core.dto.CheeseDto;
import de.benjamin1006.productmanagement.core.dto.WineDto;
import de.benjamin1006.productmanagement.core.observer.EventType;
import de.benjamin1006.productmanagement.core.observer.manager.IEventManager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author Benjamin Woitczyk
 */
@SpringBootTest(classes = TestApplication.class)
class ProductProcessingServiceTest {

    @MockBean
    private IEventManager eventManager;
    @Autowired
    private ProductProcessingService cut;
    @Autowired
    private ICurrentDayProvider currentDayProvider;
    @Captor
    private ArgumentCaptor<List<ProductDto>> captor;

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
        ProductDto wine = new WineDto("wein", 10, 20, aktuellerTag);
        ProductDto cheese = new CheeseDto("käse", 31, aktuellerTag.plusDays(50), 4.99, 1.99);
        List<ProductDto> productDtos = List.of(wine, cheese);

        List<ProductDto> result = cut.processProductsForOneDay(productDtos);

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
        ProductDto productDto1 = new WineDto("wein", 10, 20, aktuellerTag);
        ProductDto productDto2 = new CheeseDto("käse", 31, aktuellerTag.plusDays(50), 4.99, 1.99);

        List<ProductDto> productDtos = List.of(productDto1, productDto2);

        List<ProductDto> result = cut.processProductsForOneDay(productDtos);

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
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(ProductDto)}.
     * Es wird ein Käse übergeben, der die Qualitätsgrenze von 30 unterscheidet, daher wird erwartet, dass die Methode true
     * zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForLowQualityCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        ProductDto productDto = new CheeseDto("käse", 10, aktuellerTag.plusDays(50), 4.99, 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(productDto);

        assertThat(result)
                .describedAs("Das Produkt sollte entfernt werden, da es nicht den Qualitätsansprüchen von Käse genügt ")
                .isTrue();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(ProductDto)}.
     * Es wird ein Käse übergeben, der das Verfallsdatum überschritten hat, daher wird erwartet, dass die Methode true
     * zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForExpiredCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        ProductDto productDto = new CheeseDto("käse", 30, aktuellerTag.minusDays(1), 4.99, 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(productDto);

        assertThat(result)
                .describedAs("Das Produkt sollte entfernt werden, da es abgelaufen ist ")
                .isTrue();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(ProductDto)}.
     * Es wird ein Käse innerhalb der Rahmenbedingungen übergeben, daher wird erwartet, dass die Methode
     * false zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForGoodCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        ProductDto productDto = new CheeseDto("käse", 30, aktuellerTag.plusDays(50), 4.99, 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(productDto);

        assertThat(result)
                .describedAs("Das Produkt sollte nicht entfernt werden, da es den Qualitätsansprüchen von Käse genügt ")
                .isFalse();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(ProductDto)}.
     * Es wird ein Käse innerhalb der Rahmenbedingungen übergeben, daher wird erwartet, dass die Methode
     * false zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForNotExpiredCheese() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        ProductDto productDto = new CheeseDto("käse", 30, aktuellerTag, 4.99, 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(productDto);

        assertThat(result)
                .describedAs("Das Produkt sollte nicht entfernt werden, da das Verfallsdatum noch nicht überschritten ist")
                .isFalse();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(ProductDto)}.
     * Es wird ein Wein unterhalb der Qualitätsgrenze übergeben, daher wird erwartet, dass die Methode true zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForLowQualityWine() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        ProductDto productDto = new CheeseDto("wine", -1, aktuellerTag.plusDays(50), 4.99, 1.99);

        boolean result = cut.removeLowQualityOrExpiredProduct(productDto);

        assertThat(result)
                .describedAs("Das Produkt sollte entfernt werden, da es nicht den Qualitätsansprüchen von Wein genügt ")
                .isTrue();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#removeLowQualityOrExpiredProduct(ProductDto)}.
     * Es wird ein Wein innerhalb der Qualitätsgrenze übergeben, daher wird erwartet, dass die Methode false zurückliefert.
     */
    @Test
    void testRemoveLowQualityOrExpiredProductForGoodyWine() {
        currentDayProvider.setCurrentDay(aktuellerTag);
        ProductDto productDto = new WineDto("wine", 0, 1.99, aktuellerTag);

        boolean result = cut.removeLowQualityOrExpiredProduct(productDto);

        assertThat(result)
                .describedAs("Das Produkt sollte nicht entfernt werden, da es den Qualitätsansprüchen von Wein genügt ")
                .isFalse();
    }

    /**
     * Test für die Methode {@link ProductProcessingService#processProductsForTimePeriod(List, int)}.
     * Es werden ein Wein Objekt und drei Käse Objekte erstellt, die im Anschluss für 20 Tage verarbeitet werden.
     * <p>
     * Für den Test werden die Interaktionen mit dem ObserverPattern in Form eines Mocks abgetestet.
     * Zusätzlich wird mithilfe eines Captors die Liste vom Typ Product am Ende des Verarbeitungszeitraums abgefangen und überprüft.
     * Es wird erwartet das die Liste aus zwei Objekten einem Wein und einem Käse Objekt besteht.
     */
    @Test
    void testProcessProductsForTimePeriod() {
        final int timePeriod = 20;
        //Die Ausgangswerte von den Objekte wine und goodCheese werden gesichert, um diese später in den Assertions nutzen zu können!
        final int startingQualityWine = 10;
        final int startingPriceWine = 20;
        final int startingQualityGoodCheese = 50;
        final double basePriceGoodCheese = 1.99;
        final double startingPriceGoodCheese = 1.99 + 0.1 * startingQualityGoodCheese;

        ProductDto wine = new WineDto("wein", startingQualityWine, startingPriceWine, aktuellerTag);
        ProductDto lowQualityCheese = new CheeseDto("käse", 31, aktuellerTag.plusDays(15), 4.99, 1.99);
        ProductDto goodCheese = new CheeseDto("käse", startingQualityGoodCheese, aktuellerTag.plusDays(21), startingPriceGoodCheese, basePriceGoodCheese);
        ProductDto expiredCheese = new CheeseDto("käse", 50, aktuellerTag.plusDays(5), 2.99, 0.99);

        final List<ProductDto> productDtoList = List.of(wine, lowQualityCheese, goodCheese, expiredCheese);

        cut.processProductsForTimePeriod(productDtoList, timePeriod);

        verify(eventManager, times(1)).notifyProductListObservers(any(), captor.capture());
        //Der Wein sollte jeden Tag des Verarbeitungszeitraums geupdatet werden und er sollte nicht entfernt werden.
        verify(eventManager, times(20)).notifyProductObservers(EventType.UPDATE, wine);
        verify(eventManager, times(0)).notifyProductObservers(EventType.REMOVE, wine);
        //Der lowQualityCheese sollte einmal während des Verarbeitungszeitraums geupdatet werden und er sollte einmal entfernt werden.
        verify(eventManager, times(1)).notifyProductObservers(EventType.UPDATE, lowQualityCheese);
        verify(eventManager, times(1)).notifyProductObservers(EventType.REMOVE, lowQualityCheese);
        //Der goodCheese sollte jeden Tag des Verarbeitungszeitraums geupdatet werden und er sollte nicht entfernt werden.
        verify(eventManager, times(20)).notifyProductObservers(EventType.UPDATE, goodCheese);
        verify(eventManager, times(0)).notifyProductObservers(EventType.REMOVE, goodCheese);
        //Der expiredCheese sollte fünfmal während des Verarbeitungszeitraums geupdatet werden und er sollte einmal entfernt werden.
        verify(eventManager, times(5)).notifyProductObservers(EventType.UPDATE, expiredCheese);
        verify(eventManager, times(1)).notifyProductObservers(EventType.REMOVE, expiredCheese);


        List<ProductDto> capturedList = captor.getValue();
        //Nun wird die Liste die mithilfe des Capots abgefangen wurde überprüft.
        assertThat(capturedList)
                .describedAs("Die Liste sollte aus zwei Objekten bestehen! Tatsächlicher Wert: " + capturedList.size())
                .hasSize(2)
                .describedAs("Die Liste sollte aus den Objekten wine und goodCheese bestehen.")
                .contains(wine, goodCheese);

        final ProductDto updatedWine = capturedList.get(0);
        assertThat(updatedWine.getQuality())
                .describedAs("Die Qualität des Weines sollte um zwei Punkte gestiegen sein. Ausgangs Wert: " + startingQualityWine)
                .isEqualTo(12);
        assertThat(updatedWine.getPrice())
                .describedAs("Der Preis des Weines sollte sich nicht verändert haben. AusgangsWert: " + startingPriceWine)
                .isEqualTo(startingPriceWine);

        final ProductDto updatedCheese = capturedList.get(1);
        assertThat(updatedCheese.getQuality())
                .describedAs("Die Qualität des Käses sollte um zwanzig Punkte gesunken sein. Ausgangs Wert: " + startingQualityGoodCheese)
                .isEqualTo(30);
        assertThat(updatedCheese.getPrice())
                .describedAs("Der Preis des Käses sollte sich auf 4.99 verändert haben. AusgangsWert: " + startingPriceGoodCheese)
                .isEqualTo(4.99);
    }

}