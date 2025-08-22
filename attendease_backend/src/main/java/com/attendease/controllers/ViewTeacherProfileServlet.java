package com.attendease.controllers;

import com.attendease.services.TeachServices;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;


public class ViewTeacherProfileServlet extends HttpServlet {

    private TeachServices teachServices;

    @Override
    public void init() throws ServletException {
        super.init();
        teachServices = new TeachServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve the teacherId from the request URL parameter
        String teacherIdParam = request.getParameter("teacherId");

        if (teacherIdParam == null || teacherIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing teacherId parameter.");
            return;
        }

        try {
            int teacherId = Integer.parseInt(teacherIdParam);
            // Call the service method to get the teacher's profile details
            Map<String, Object> teacherProfile = teachServices.getTeacherProfileById(teacherId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            if (teacherProfile.isEmpty()) {
                // If no profile is found, send a 404 Not Found status
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Teacher profile not found.\"}");
            } else {
                // If a profile is found, convert it to JSON and send it
                out.print(new Gson().toJson(teacherProfile));
            }
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid teacherId format.");
        } catch (SQLException e) {
            // Log the error and send a generic internal server error response
            System.err.println("Database error in ViewTeacherProfileServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error retrieving profile.");
        } catch (Exception e) {
            System.err.println("Unexpected error in ViewTeacherProfileServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error.");
        }
    }

    // Add doOptions to handle preflight requests from the frontend
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}