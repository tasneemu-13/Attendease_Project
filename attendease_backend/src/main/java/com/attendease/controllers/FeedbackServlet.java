package com.attendease.controllers;

import com.attendease.utils.DatabaseConnection;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class FeedbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Use Gson for JSON parsing
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Read the JSON data from the request body
            FeedbackData feedbackData = gson.fromJson(request.getReader(), FeedbackData.class);
            String feedbackText = feedbackData.getFeedback();

            if (feedbackText == null || feedbackText.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Feedback text cannot be empty.\"}");
                return;
            }

            connection = DatabaseConnection.getConnection();
            // UPDATED: The SQL query now only inserts into the feedback_text column
            String sql = "INSERT INTO feedback (feedback_text) VALUES (?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, feedbackText);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"message\": \"Feedback submitted successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Failed to submit feedback.\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"An unexpected error occurred: " + e.getMessage() + "\"}");
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Simple class to model the incoming JSON data
    private static class FeedbackData {
        private String feedback;

        public String getFeedback() {
            return feedback;
        }
    }
}
