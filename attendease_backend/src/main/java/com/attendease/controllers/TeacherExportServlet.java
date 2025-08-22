package com.attendease.controllers;

import com.attendease.utils.DatabaseConnection;
import com.attendease.utils.ExcelUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@WebServlet("/TeacherExportServlet") // Add the WebServlet annotation
public class TeacherExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        String teacherName = request.getParameter("teacherName");

        if (courseCode == null || courseCode.trim().isEmpty() || courseName == null || courseName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course Code and Course Name are required.");
            return;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> studentAttendanceSummary = new ArrayList<>();
        String firstAttendanceDate = null;
        String currentDate = LocalDate.now().toString();

        try {
            connection = DatabaseConnection.getConnection();

            // Query to find the earliest attendance date for the course
            String firstDateQuery = "SELECT MIN(date) AS first_date FROM attendance WHERE course_code = ?";
            stmt = connection.prepareStatement(firstDateQuery);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();

            if (rs.next()) {
                firstAttendanceDate = rs.getString("first_date");
            }
            if (firstAttendanceDate == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No attendance records found for course: " + courseCode);
                return;
            }

            // Main query to get student attendance summary
            String query = "SELECT " +
                    "    s.enrollment_id, " +
                    "    s.stu_name, " +
                    "    (SELECT COUNT(DISTINCT date) FROM attendance WHERE course_code = ?) AS total_lectures, " +
                    "    COUNT(CASE WHEN a.status = 'Present' THEN 1 END) AS attended_lectures " +
                    "FROM " +
                    "    student s " +
                    "JOIN " +
                    "    attendance a ON s.enrollment_id = a.enrollment_id " +
                    "WHERE " +
                    "    a.course_code = ? " +
                    "GROUP BY " +
                    "    s.enrollment_id, s.stu_name " +
                    "ORDER BY " +
                    "    s.enrollment_id";

            stmt = connection.prepareStatement(query);
            stmt.setString(1, courseCode);
            stmt.setString(2, courseCode);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("enrollment_id", rs.getString("enrollment_id"));
                record.put("stu_name", rs.getString("stu_name"));
                record.put("total_lectures", rs.getInt("total_lectures"));
                record.put("attended_lectures", rs.getInt("attended_lectures"));
                studentAttendanceSummary.add(record);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Attendance_Summary_" + courseCode + ".xlsx\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                ExcelUtil.exportAttendanceSummaryToExcel(outputStream, courseCode, courseName, teacherName, studentAttendanceSummary, firstAttendanceDate, currentDate);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred while exporting attendance.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}