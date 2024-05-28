package de.benjamin1006.productmanagement.core.exception;

/**
 * Exception, die ausgelöst wird, wenn ein Fehler beim Parsen einer CSV-Datei auftritt.
 * @author Benjamin Woitczyk
 */
public class CsvParsingException extends RuntimeException {

    /**
     * Konstruktor, der eine neue CsvParsingException mit der angegebenen Fehlermeldung erstellt.
     *
     * @param errorMessage die Fehlermeldung.
     */
    public CsvParsingException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Konstruktor, der eine neue CsvParsingException mit der angegebenen Fehlermeldung und Ursache erstellt.
     *
     * @param errorMessage die Fehlermeldung.
     * @param cause die Ursache der Exception (wird zur späteren Abfrage durch die {@link Throwable#getCause()} Methode gespeichert).
     */
    public CsvParsingException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    /**
     * Konstruktor, der eine neue CsvParsingException mit der angegebenen Ursache erstellt.
     *
     * @param cause die Ursache der Exception (wird zur späteren Abfrage durch die {@link Throwable#getCause()} Methode gespeichert).
     */
    public CsvParsingException(Throwable cause) {
        super(cause);
    }
}

