package com.attendease.services;

import com.attendease.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserServices {

    private Connection connection;
    private PreparedStatement stmt;
    private ResultSet rs;


    public UserServices(){
        try{
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int getUserIdByName(String teacherName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1; // default not found
        try {
            connection = DatabaseConnection.getConnection();
            String getQuery = "SELECT user_id from Teacher WHERE name = ?";

            stmt = connection.prepareStatement(getQuery);
            stmt.setString(1, teacherName);
            rs = stmt.executeQuery();
            if(rs.next()){
                userId = rs.getInt("user_id");
            }
        } catch (SQLException e) {

            System.err.println("Error fetching user_id"+e.getMessage());
            e.printStackTrace();;
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing Statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing Connection: " + e.getMessage());
            }
        }
        return userId;
    }

}

