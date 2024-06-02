package de.benjamin1006.productmanagement.core.exception;

/**
 * @author Benjamin Woitczyk
 */
public class ProductNotFoundException extends RuntimeException {
    /**
     * Konstruktor, der eine neue ProductNotFoundException mit der angegebenen Fehlermeldung erstellt.
     *
     * @param errorMessage die Fehlermeldung.
     */
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Konstruktor, der eine neue ProductNotFoundException mit der angegebenen Fehlermeldung und Ursache erstellt.
     *
     * @param errorMessage die Fehlermeldung.
     * @param cause        die Ursache der Exception (wird zur späteren Abfrage durch die {@link Throwable#getCause()} Methode gespeichert).
     */
    public ProductNotFoundException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    /**
     * Konstruktor, der eine neue ProductNotFoundException mit der angegebenen Ursache erstellt.
     *
     * @param cause die Ursache der Exception (wird zur späteren Abfrage durch die {@link Throwable#getCause()} Methode gespeichert).
     */
    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }
}
