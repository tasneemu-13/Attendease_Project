/*package com.attendease.utils;
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
}*/
package com.attendease.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Read environment variables for production deployment
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (URL == null || USER == null || PASSWORD == null) {
                // This block is useful for local testing where env vars might not be set
                String localUrl = "jdbc:mysql://localhost:3306/attendease_db";
                String localUser = "root";
                String localPassword = "admin";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection(localUrl, localUser, localPassword);
                    System.out.println("✅ Connected to local MySQL successfully!");
                } catch (ClassNotFoundException e) {
                    System.err.println("❌ MySQL JDBC Driver not found.");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.err.println("❌ Local connection failed.");
                    e.printStackTrace();
                    throw e;
                }
            } else {
                // Use environment variables for production
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("✅ Connected to production MySQL successfully!");
                } catch (ClassNotFoundException e) {
                    System.err.println("❌ MySQL JDBC Driver not found.");
                    e.printStackTrace();
                } catch (SQLException e) {
                    System.err.println("❌ Production connection failed.");
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return connection;
    }
}

