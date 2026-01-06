package com.taskmanager.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton class quản lý kết nối database
 * Load cấu hình từ application.properties
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;
    private String driver;
    
    /**
     * Private constructor để implement Singleton pattern
     */
    private DatabaseConnection() {
        loadProperties();
    }
    
    /**
     * Load cấu hình database từ application.properties
     */
    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                System.err.println("Không tìm thấy file application.properties");
                return;
            }
            
            props.load(input);
            
            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");
            
            // Load JDBC driver
            Class.forName(driver);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi load database properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy instance của DatabaseConnection (Singleton)
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Lấy connection đến database
     * Tự động tạo connection mới nếu connection cũ bị đóng
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }
    
    /**
     * Đóng connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection đã được đóng");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test kết nối database
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Lỗi khi test connection: " + e.getMessage());
            return false;
        }
    }
}
