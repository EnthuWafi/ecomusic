package com.enth.ecomusic.controller.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class AppConfigListener
 *
 */
@WebListener
public class AppConfigListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public AppConfigListener() {
        // TODO Auto-generated constructor stub
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Get the ServletContext to store global settings
        ServletContext context = sce.getServletContext();
        
        // Set global settings in the ServletContext
        Properties config = new Properties();
        try (InputStream input = Thread.currentThread()
                                       .getContextClassLoader()
                                       .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            config.load(input);

            // Set properties to ServletContext for access in Servlets/JSP
            for (String key : config.stringPropertyNames()) {
                context.setAttribute(key, config.getProperty(key));
            }

            System.out.println("Config loaded and set to ServletContext.");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
        
        // Optional: log the initialization
        System.out.println("ServletContext Initialized: Global settings set.");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed when the application stops
        System.out.println("ServletContext Destroyed: Cleanup.");
    }
	
}
