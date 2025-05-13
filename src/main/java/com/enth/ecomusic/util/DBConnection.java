package com.enth.ecomusic.util;

import java.sql.*;

import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {
    private static final HikariDataSource dataSource;

    static {
    	try {
            Class.forName("oracle.jdbc.OracleDriver");

            dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
            dataSource.setUsername("ecomusic");
            dataSource.setPassword("ecomusic");
            dataSource.setMaximumPoolSize(10);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load Oracle JDBC Driver", e);
        }
    }

    public static Connection getConnection() {
    	try {
            return dataSource.getConnection();
        } catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
    }
}