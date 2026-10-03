package com.reservationhub.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("Unable to find config.properties in classpath.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration properties.", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}