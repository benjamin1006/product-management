package de.benjamin1006.productmanagement.core.processing.days;

import de.benjamin1006.productmanagement.datamodel.interfaces.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.observer.EventType;
import de.benjamin1006.productmanagement.observer.manager.IEventManager;
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

    private final IEventManager productManagementEventManager;

    public SimulationCurrentDayProvider(IEventManager productManagementEventManager) {
        this.productManagementEventManager = productManagementEventManager;
    }

    @Override
    public LocalDate getCurrentDay() {
        return currentSimulatedDay;
    }

    @Override
    public void setCurrentDay(LocalDate newCurrentSimulatedDay) {
        productManagementEventManager.notifyNewDayObservers(EventType.NEW_DAY, newCurrentSimulatedDay);
        this.currentSimulatedDay = newCurrentSimulatedDay;
    }
}
