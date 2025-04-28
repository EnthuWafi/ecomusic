package com.enth.ecomusic.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Get the ServletContext to store global settings
        ServletContext context = sce.getServletContext();
        
        // Set global settings in the ServletContext
        context.setAttribute("websiteName", "Eco Music");
        context.setAttribute("websiteDescription", "A place for all your music needs.");
        
        // Optional: log the initialization
        System.out.println("ServletContext Initialized: Global settings set.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup code if needed when the application stops
        System.out.println("ServletContext Destroyed: Cleanup.");
    }
}
