package com.enth.ecomusic.model.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import com.enth.ecomusic.util.DBConnection;

public class TransactionTemplate implements AutoCloseable {
    private final Connection connection;
    private boolean committed = false;
    
    public TransactionTemplate() throws SQLException {
        this.connection = DBConnection.getConnection();
        this.connection.setAutoCommit(false);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void commit() throws SQLException {
        connection.commit();
        committed = true;
    }
    
    public void rollback() throws SQLException {
        connection.rollback();
    }
    
    @Override
    public void close() throws SQLException {
        if (!committed) {
            try {
                rollback();
            } catch (SQLException e) {
                // Log rollback failure
            }
        }
        connection.close();
    }
}