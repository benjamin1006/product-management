package de.benjamin1006.productmanagement.core;

import org.springframework.context.annotation.ComponentScan;

/**
 * @author Benjamin Woitczyk
 */
@ComponentScan({
        "de.benjamin1006.productmanagement.core.dataimport",
        "de.benjamin1006.productmanagement.core.processing",
        "de.benjamin1006.productmanagement.core.notification"
})
public class CoreConfig {
}
