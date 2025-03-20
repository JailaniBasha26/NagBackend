package com.example.demo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseHealthCheckService {

    @Autowired
    private DataSource dataSource; // Use DataSource for better connection pooling
    @Value("${spring.us.datasource.jdbc-url}")
    private String dbUrl;

    @Value("${spring.us.datasource.username}")
    private String dbUsername;

    @Value("${spring.us.datasource.password}")
    private String dbPassword;

//    public boolean isDatabaseUp() {
//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            return connection.isValid(2);  // Checks if the connection is valid within 2 seconds
//        } catch (SQLException e) {
//            return false; // Database is down
//        }
//    }

//    public boolean isDatabaseUp() {
//        try (Connection connection = dataSource.getConnection();
//             Statement stmt = connection.createStatement()) {
//
//            stmt.executeQuery("SELECT 1"); // Lightweight query to check DB status
//            return true;  // DB is up
//        } catch (SQLException e) {
//            return false; // DB is down
//        }
//    }

    public boolean isDatabaseUp() {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = connection.createStatement()) {

            stmt.executeQuery("SELECT 1"); // Fast query to check if DB is responsive
            return true;
        } catch (SQLException e) {
            return false; // Database is down
        }
    }
}
