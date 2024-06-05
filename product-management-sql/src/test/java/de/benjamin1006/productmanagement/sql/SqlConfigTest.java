package de.benjamin1006.productmanagement.sql;

import de.benjamin1006.productmanagement.core.config.ApplicationConfig;
import de.benjamin1006.productmanagement.core.processing.days.ICurrentDayProvider;
import de.benjamin1006.productmanagement.sql.service.SqlImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test für die Klasse {@link SqlImportService}
 *
 * @author Benjamin Woitczyk
 */
@SpringBootTest(classes = TestSqlApplication.class,
        properties = {"spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=password",
                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "product-management.sql-import-active=true"})
class SqlConfigTest {

    @Autowired(required = false)
    private DataSourceAutoConfiguration dataSourceAutoConfiguration;

    @Autowired(required = false)
    private HibernateJpaAutoConfiguration hibernateJpaAutoConfiguration;

    @Autowired(required = false)
    private SqlImportService sqlImportService;

    @MockBean
    private ICurrentDayProvider currentDayProvider;

    @MockBean
    private ApplicationConfig applicationConfig;

    /**
     * Dieser Test soll sicherstellen das die Spring Boot AutoConfiguration für DataSource und HibernateJpa geladen werden,
     * außerdem überprüft er, ob das Bean vom Typ {@link SqlImportService} zur Verfügung steht, sobald das Property
     * sql-import-active auf true steht.
     */
    @Test
    void testSpringBootAutoConfigurationNotLoaded() {
        assertThat(dataSourceAutoConfiguration)
                .describedAs("Wir wollen die Application mit der von Spring Boot benutzte DatasourceAutoConfiguration starten, daher sollte das Ergebnis hier nicht null sein!")
                .isNotNull();
        assertThat(hibernateJpaAutoConfiguration)
                .describedAs("Wir wollen die Application mit der von Spring Boot benutzte HibernateJpaAutoConfiguration starten, daher sollte das Ergebnis hier nicht null sein!")
                .isNotNull();
        assertThat(sqlImportService)
                .describedAs("Die SqlConfig Klasse soll ein Bean für vom Typ SqlImportService bereitstellen, daher sollte das Ergebnis hier nicht null sein!")
                .isNotNull();
    }

}