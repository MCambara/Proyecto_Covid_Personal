package org.prograIII.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = PropertyReader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Properties file not found");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading the properties file", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }


    public static String getDbUrl() {
        return getProperty("db.url");
    }

    public static String getDbUsername() {
        return getProperty("db.username");
    }

    public static String getDbPassword() {
        return getProperty("db.password");
    }

    public static String getCovidApiKey() {
        return getProperty("covid.api.key");
    }

    public static String getCovidApiHost() {
        return getProperty("covid.api.host");
    }

    public static String getCovidApiRegionsUrl() {
        return getProperty("covid.api.regions-url");
    }

    public static String getCovidApiProvincesUrl() {
        return getProperty("covid.api.provinces-url");
    }

    public static String getCovidApiReportsUrl() {
        return getProperty("covid.api.reports-url");
    }

    public static String getCovidApiTargetDate() {
        return getProperty("covid.api.target-date");
    }


    public static String getAppInitialDelay() {
        return getProperty("app.initial-delay");
    }


    public static String getLoggingLevel() {
        return getProperty("logging.level.org.prograIII");
    }

    public static String getCovidQueryDate() {
        return getProperty("covid.report.date");
    }

    public static String getCovidIso() {
        return getProperty("covid.iso");
    }
}
