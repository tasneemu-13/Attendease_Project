package com.attendease.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/attendease_db";
    private static final String USER = "root"; //
    private static final String PASSWORD = "admin"; //

    private static Connection connection;

    // private constructor to prevent instantiation
    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connected to MySQL successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ MySQL JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("❌ Connection failed.");
                e.printStackTrace();
                throw e;
            }
        }
        return connection; // ensure this is always returned
    }
}
