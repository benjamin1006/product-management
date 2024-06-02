package de.benjamin1006.productmanagement.core.observer;

/**
 * Enum für die unterschiedlichen Eventarten des ObserverPatterns
 *
 * @author Benjamin Woitczyk
 */
public enum EventType {

    CREATE("create"),
    UPDATE("update"),
    REMOVE("remove"),
    NEW_DAY("newDay");

    public final String label;

    EventType(String label) {
        this.label = label;
    }
}
