package com.attendease.controllers;

import com.google.gson.Gson;
import com.attendease.services.TeachServices;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;


public class UpdateTeacherProfileServlet extends HttpServlet {

    private TeachServices teachServices;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        teachServices = new TeachServices();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Failed to read request body.\"}");
            return;
        }

        try {
            // Parse the JSON request body into a Map
            Map<String, Object> data = gson.fromJson(sb.toString(), Map.class);
            // Assuming teacherId is a number in the JSON
            double teacherIdDouble = ((Double) data.get("teacherId"));
            int teacherId = (int) teacherIdDouble;

            // Call the service method to update the profile
            boolean success = teachServices.updateTeacherProfile(teacherId, data);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"message\":\"Profile updated successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"Failed to update profile.\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid teacherId format.\"}");
        } catch (SQLException e) {
            System.err.println("Database error in UpdateTeacherProfileServlet: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Database error while updating profile.\"}");
        } catch (Exception e) {
            System.err.println("Unexpected error in UpdateTeacherProfileServlet: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"Internal server error.\"}");
        }
    }
}
