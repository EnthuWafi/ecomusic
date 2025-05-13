package com.enth.ecomusic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private final Properties properties;

    public AppConfig() {
    	this.properties = new Properties();
        try (InputStream input = Thread.currentThread()
                                       .getContextClassLoader()
                                       .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }
    
    public String get(String key) {
        return properties.getProperty(key);
    }
}