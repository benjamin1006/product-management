package de.benjamin1006.productmanagement.sql;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author Benjamin Woitczyk
 */
@Configuration
@ConditionalOnProperty(name = "product-management.sql-import-active", havingValue = "false", matchIfMissing = true)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class NoSqlConfig {
}
