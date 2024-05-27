package de.benjamin1006.productmanagementsolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductManagementSolutionApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductManagementSolutionApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementSolutionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Hello!!");
    }
}
