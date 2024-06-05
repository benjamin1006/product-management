package de.benjamin1006.productmanagement.core.processing.days;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Implementierung des Interfaces {@link ICurrentDayProvider} für das Simulieren von Tagen die in der Zukunft liegen.
 *
 * @author Benjamin Woitczyk
 */
@Component
public class SimulationCurrentDayProvider implements ICurrentDayProvider {

    private LocalDate currentSimulatedDay = LocalDate.now();

    @Override
    public LocalDate getCurrentDay() {
        return currentSimulatedDay;
    }

    @Override
    public void setCurrentDay(LocalDate newCurrentSimulatedDay) {
        this.currentSimulatedDay = newCurrentSimulatedDay;
    }
}
