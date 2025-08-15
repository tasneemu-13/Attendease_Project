package com.attendease.controllers;

import com.google.gson.Gson;
import com.attendease.services.TeachServices;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // Ensure this import is present
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


public class CourseViewingServlet extends HttpServlet {

    private TeachServices teachServices;

    @Override
    public void init() throws ServletException {
        super.init(); // Always call super.init()
        teachServices = new TeachServices();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String teacherIdParam = request.getParameter("teacherId");
        if (teacherIdParam == null || teacherIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing teacherId parameter");
            return;
        }

        try {
            int teacherId = Integer.parseInt(teacherIdParam);
            // Call the service method to get courses by ID

            List<Map<String, String>> courses = teachServices.getCoursesTeachingById(teacherId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(courses));
            out.flush();
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid teacherId format");
        } catch (Exception e) {
            System.err.println("Error retrieving courses in CourseViewingServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving courses: " + e.getMessage());
        }
    }
}