package com.enth.ecomusic.controller.common;

import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.DBConnection;
import com.stripe.Stripe;

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
        
        //App Context
        context.setAttribute("appContext", new AppContext());  
        
        //stripe
        Stripe.apiKey = AppConfig.get("stripeSecretKey");
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
