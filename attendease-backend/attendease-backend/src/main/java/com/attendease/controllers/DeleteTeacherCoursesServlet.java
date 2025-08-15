package com.attendease.controllers;

import com.attendease.services.TeachServices; // Assuming delCoursesTeaching is here
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DeleteTeacherCoursesServlet extends  HttpServlet{
    private TeachServices teacherServices;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize your service. Replace TeacherServices with your actual service class if different.
        teacherServices = new TeachServices();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String courseCode = request.getParameter("courseCode");
        String userIdParam = request.getParameter("teacherId");

        if (courseCode == null || userIdParam == null || courseCode.trim().isEmpty() || userIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing courseCode or teacherId parameters.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            boolean isDeleted = teacherServices.delCoursesTeaching(courseCode, userId);

            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Course deleted successfully!");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Course not found or could not be deleted.");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid teacherId format.");
        } catch (SQLException e) {
            System.err.println("Database error during course deletion: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error occurred while deleting the course.");
        }
    }
}