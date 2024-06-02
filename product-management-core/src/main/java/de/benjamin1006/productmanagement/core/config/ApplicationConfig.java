package de.benjamin1006.productmanagement.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;

/**
 * @author Benjamin Woitczyk
 */
@ConfigurationProperties(prefix = "product-management")
public class ApplicationConfig {

    private int timePeriod;
    private String csvFilePath;
    private boolean fishIsActive;
    private boolean csvImportActive;

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public boolean isFishIsActive() {
        return fishIsActive;
    }

    public void setFishIsActive(boolean fishIsActive) {
        this.fishIsActive = fishIsActive;
    }

    public boolean isCsvImportActive() {
        return csvImportActive;
    }

    public void setCsvImportActive(boolean csvImportActive) {
        this.csvImportActive = csvImportActive;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ApplicationConfig.class.getSimpleName() + "[", "]")
                .add("timePeriod=" + timePeriod)
                .add("csvFilePath='" + csvFilePath + "'")
                .add("fishIsActive=" + fishIsActive)
                .add("csvImportActive=" + csvImportActive)
                .toString();
    }
}