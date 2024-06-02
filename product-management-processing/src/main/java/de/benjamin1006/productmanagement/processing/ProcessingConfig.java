package de.benjamin1006.productmanagement.processing;

import org.springframework.context.annotation.ComponentScan;

/**
 * @author Benjamin Woitczyk
 */
@ComponentScan({
        "de.benjamin1006.productmanagement.processing.dataimport",
        "de.benjamin1006.productmanagement.processing.extecution",
        "de.benjamin1006.productmanagement.processing.notification"
})
public class ProcessingConfig {
}
