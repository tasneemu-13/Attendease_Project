package com.attendease.controllers;

import com.attendease.services.StuService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;


public class EditStudentProfileServlet extends HttpServlet {
    private StuService stuService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        stuService = new StuService();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();

            String enrollmentId = jsonObject.has("enrollmentId") ? jsonObject.get("enrollmentId").getAsString() : null;
            String className = jsonObject.has("className") ? jsonObject.get("className").getAsString() : null;
            String program = jsonObject.has("program") ? jsonObject.get("program").getAsString() : null;
            String branch = jsonObject.has("branch") ? jsonObject.get("branch").getAsString() : null;

            int year = -1;
            if (jsonObject.has("year") && jsonObject.get("year").isJsonPrimitive()) {
                year = jsonObject.get("year").getAsInt();
            }

            // Correctly parse semester as an integer
            int semester = -1;
            if (jsonObject.has("semster") && jsonObject.get("semster").isJsonPrimitive()) {
                // The input might be a String like "6" or a number.
                try {
                    semester = jsonObject.get("semster").getAsInt();
                } catch (NumberFormatException e) {
                    // Handle cases where the input is a String like "6th Semester"
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson(Map.of("message", "Semester must be a valid number.")));
                    return;
                }
            }


            if (enrollmentId == null || year == -1 || semester == -1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(Map.of("message", "Enrollment ID, Year, and Semester are required.")));
                return;
            }

            // Call the service method with the correct parameters
            boolean updated = stuService.updateStuProfile(enrollmentId, className, year, program, branch, semester);

            if (updated) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(Map.of("message", "Profile updated successfully.")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(gson.toJson(Map.of("message", "Failed to update profile. The student may not exist.")));
            }
        } catch (Exception e) {
            System.err.println("Error in EditStudentProfileServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("message", "An unexpected error occurred: " + e.getMessage())));
        }
    }
}