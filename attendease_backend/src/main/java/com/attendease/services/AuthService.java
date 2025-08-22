package com.attendease.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.attendease.utils.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    public Boolean isValidTAC(String TAC) {
        return "TEACH2025".equals(TAC);
    }

    public boolean registerUser(String emailId, String password, String TAC, String role, String enrollmentId, String name, String className, Integer year, String program, String branch , Integer semster) {
        Connection connection = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertUserStmt = null;
        PreparedStatement insertRoleStmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction

            String checkQuery = "SELECT * FROM User WHERE email_id = ?";
            checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, emailId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("User already registered.");
                connection.rollback(); // Rollback transaction
                return false;
            }

            if (role.equalsIgnoreCase("teacher")) {
                if (!isValidTAC(TAC)) {
                    System.out.println("Invalid Teacher Access Code.");
                    connection.rollback(); // Rollback transaction
                    return false;
                }
            }

            // ✅ Corrected password hashing using Favre BCrypt
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            String insertUserQuery = "INSERT INTO User (email_id, password, role) VALUES (?, ?, ?)";
            insertUserStmt = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
            insertUserStmt.setString(1, emailId);
            insertUserStmt.setString(2, hashedPassword);
            insertUserStmt.setString(3, role);

            int userRows = insertUserStmt.executeUpdate();
            if (userRows == 0) {
                System.out.println("User Registration Failed!");
                connection.rollback();
                return false;
            }

            int userId;
            ResultSet generatedKeys = insertUserStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                System.out.println("User ID retrieval failed.");
                connection.rollback();
                return false;
            }

            if (role.equalsIgnoreCase("teacher")) {
                String insertTeacherQuery = "INSERT INTO Teacher (TAC, user_id, name) VALUES (?, ?, ?)";
                insertRoleStmt = connection.prepareStatement(insertTeacherQuery);
                insertRoleStmt.setString(1, TAC);
                insertRoleStmt.setInt(2, userId);
                insertRoleStmt.setString(3, name);
            } else if (role.equalsIgnoreCase("student")) {
                String insertStudentQuery = "INSERT INTO Student (enrollment_id, user_id,  stu_name,  class_name , year ,  program , branch , semster) VALUES (?, ?, ?, ?, ?,?,?,?)";
                insertRoleStmt = connection.prepareStatement(insertStudentQuery);
                insertRoleStmt.setString(1, enrollmentId);
                insertRoleStmt.setInt(2, userId);
                insertRoleStmt.setString(3, name);
                insertRoleStmt.setString(4, className);
                insertRoleStmt.setInt(5, year);
                insertRoleStmt.setString(6, program);
                insertRoleStmt.setString(7, branch);
                insertRoleStmt.setInt(8, semster);
            } else {
                System.out.println("Invalid user role specified.");
                connection.rollback();
                return false;
            }

            int roleRows = insertRoleStmt.executeUpdate();

            if (roleRows > 0) {
                connection.commit(); // Commit transaction if all inserts succeed
                return true;
            } else {
                connection.rollback(); // Rollback if the role insert failed
                return false;
            }

        } catch (SQLException e) {
            System.out.println("SQL Error in registerUser: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback on any exception
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (checkStmt != null) checkStmt.close();
                if (insertUserStmt != null) insertUserStmt.close();
                if (insertRoleStmt != null) insertRoleStmt.close();
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset to default auto-commit mode
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String login(String emailId, String password) {
        Connection connection = null;
        PreparedStatement loginStmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            String loginQuery = "SELECT password, role FROM User WHERE email_id = ?";
            loginStmt = connection.prepareStatement(loginQuery);
            loginStmt.setString(1, emailId);

            rs = loginStmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String userRole = rs.getString("role");

                // ✅ Update password check using Favre's BCrypt
                if (BCrypt.verifyer().verify(password.toCharArray(), storedHashedPassword).verified) {
                    System.out.println("Login Successful!");
                    return userRole;
                } else {
                    System.out.println("Invalid Login! Passwords do not match.");
                    return "invalid";
                }
            } else {
                System.out.println("Invalid Login! User not found.");
                return "invalid";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        } finally {
            try {
                if (loginStmt != null) loginStmt.close();
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //webbased

    public Map<String, Object> loginWithDetails(String emailId, String password) {
        Map<String, Object> result = new HashMap<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT user_id, password, role FROM User WHERE email_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, emailId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String role = rs.getString("role");
                int userId = rs.getInt("user_id");

                if (BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified) {
                    result.put("role", role);
                    result.put("userId", userId);

                    if ("teacher".equalsIgnoreCase(role)) {
                        PreparedStatement stmt2 = connection.prepareStatement("SELECT name FROM Teacher WHERE user_id = ?");
                        stmt2.setInt(1, userId);
                        ResultSet rs2 = stmt2.executeQuery();
                        if (rs2.next()) {
                            result.put("name", rs2.getString("name"));
                        }
                    } else if ("student".equalsIgnoreCase(role)) {
                        PreparedStatement stmt3 = connection.prepareStatement("SELECT enrollment_id, stu_name FROM Student WHERE user_id = ?");
                        stmt3.setInt(1, userId);
                        ResultSet rs3 = stmt3.executeQuery();
                        if (rs3.next()) {
                            result.put("enrollmentId", rs3.getString("enrollment_id"));
                            result.put("name", rs3.getString("stu_name"));
                        }
                    }

                    return result; // ✅ Success
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null; // Invalid credentials
    }

}
