package com.enth.ecomusic.controller.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.DBConnection;

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

        for (String key : AppConfig.getProperties().stringPropertyNames()) {
            context.setAttribute(key, AppConfig.get(key));
        }

        //RoleCache
        context.setAttribute("roleCacheService", new RoleCacheService());       
        // Optional: log the initialization
        System.out.println("ServletContext Initialized: Global settings set.");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed when the application stops
    	DBConnection.closeDataSource();
        System.out.println("ServletContext Destroyed: Cleanup.");
    }
	
}
