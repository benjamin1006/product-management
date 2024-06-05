package de.benjamin1006.productmanagement.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * Klasse zum Abgreifen von UserInput.
 * @author Benjamin Woitczyk
 */
@Service
public class UserInputService {

    private static final Logger log = LoggerFactory.getLogger(UserInputService.class);

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Fordert den User auf eine natürliche ganze Zahl einzugeben.
     * Ist die Zahl keine natürliche ganze Zahl, wird er nochmals aufgefordert.
     * @return eine Zahl vom Typ int
     */
    public int getUserInput() {
        while (true) {
            log.info("Info: Es werden nur ganze Zahlen akzeptiert, mit einer 0 wird der Programmablauf beendet.");
            log.info("Bitte geben Sie die Anzahl der Tage an, die Sie simulieren möchten!");

            if (!scanner.hasNextInt()) {
                log.info("Bitte geben Sie nur ganze Zahlen ein und bestätigen Sie mit der ENTER Taste! (Beispiel: 5)");
                scanner.next();
                continue;
            }

            int days = scanner.nextInt();
            log.info("Ihre Eingabe war: {}", days);
            return days;
        }
    }

}
