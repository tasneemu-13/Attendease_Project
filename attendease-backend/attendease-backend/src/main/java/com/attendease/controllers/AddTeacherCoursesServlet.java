package com.attendease.controllers;

import com.attendease.services.TeachServices;
import com.attendease.services.UserServices;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AddTeacherCoursesServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        // UserServices userServices = new UserServices(); // This is no longer needed

        try {
            // Parse JSON body
            Map<String, String> body = gson.fromJson(request.getReader(), Map.class);
            // 🐛 FIX: Change from teacherName to teacherId
            String teacherIdStr = body.get("teacherId");
            String courseCode = body.get("courseCode");
            String className = body.get("className");

            // 🐛 FIX: Update the validation check to use the correct variable
            if (teacherIdStr == null || courseCode == null || className == null || teacherIdStr.trim().isEmpty() || courseCode.trim().isEmpty() || className.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Missing required fields")));
                return;
            }

            // 🐛 FIX: Convert the teacherId string to an integer
            int teacherId;
            try {
                teacherId = Integer.parseInt(teacherIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Invalid teacherId format")));
                return;
            }

            // The code to get userId by name is no longer necessary as we are getting the ID directly.
            // No need to call userServices.getUserIdByName.

            // Add course for teacher
            TeachServices teachServices = new TeachServices();
            boolean success = teachServices.addCoursesTeaching(teacherId, courseCode , className);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(Map.of("message", "Course assigned to teacher successfully")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message", "Failed to assign course. It may already be assigned.")));
            }

        } catch (Exception e) {
            System.err.println("Error in AddTeacherCoursesServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("message", "Server error: " + e.getMessage())));
        } finally {
            out.flush();
        }
    }
}