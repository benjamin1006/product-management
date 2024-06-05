package de.benjamin1006.productmanagement.sql;

import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test für Klasse {@link NoSqlConfig}.
 *
 * @author Benjamin Woitczyk
 */
@SpringBootTest(classes = TestSqlApplication.class,
        properties = {"product-management.sql-import-active=false"})
class NoSqlConfigTest {

    @Autowired(required = false)
    private DataSourceAutoConfiguration dataSourceAutoConfiguration;

    @Autowired(required = false)
    private HibernateJpaAutoConfiguration hibernateJpaAutoConfiguration;

    @MockBean
    private ICurrentDayProvider currentDayProvider;

    /**
     * Dieser Test soll sicherstellen das die Spring Boot AutoConfiguration für DataSource und HibernateJpa nicht geladen wird,
     * sobald das Property sql-import-active auf false steht.
     */
    @Test
    void testSpringBootAutoConfigurationNotLoaded() {
        assertThat(dataSourceAutoConfiguration)
                .describedAs("Wir wollen die Application ohne die von Spring Boot benutzte DatasourceAutoConfiguration starten, daher sollte das Ergebnis hier null sein!")
                .isNull();
        assertThat(hibernateJpaAutoConfiguration)
                .describedAs("Wir wollen die Application ohne die von Spring Boot benutzte HibernateJpaAutoConfiguration starten, daher sollte das Ergebnis hier null sein!")
                .isNull();
    }

}