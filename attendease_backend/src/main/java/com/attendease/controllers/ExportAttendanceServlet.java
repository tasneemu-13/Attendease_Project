/*package com.attendease.controllers;

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

/**
 * Servlet to handle the export of attendance data to an Excel file.
 * This class fetches attendance records from the database and uses the ExcelUtil
 * to generate and send an XLSX file to the client.

@WebServlet("/export-attendance")
public class ExportAttendanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Retrieve parameters from the GET request.
        // CORRECTED: The parameter name is now "courseCode" to match the frontend.
        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");

        // Validate that parameters are not null or empty
        if (courseCode == null || courseCode.trim().isEmpty() || courseName == null || courseName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course Code and Course Name are required.");
            return;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> attendanceData = new ArrayList<>();

        try {
            // Get the database connection
            connection = DatabaseConnection.getConnection();

            // CORRECTED SQL QUERY:
            // This query now performs an INNER JOIN on the 'attendance' and 'student' tables
            // using the common 'enrollment_id' column to get the student's name.
            String query = "SELECT a.enrollment_id, s.stu_name, a.date, a.status, a.marked_by " +
                    "FROM attendance a " +
                    "INNER JOIN student s ON a.enrollment_id = s.enrollment_id " +
                    "WHERE a.course_code = ?";

            stmt = connection.prepareStatement(query);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();

            // Populate the list with data from the ResultSet
            while (rs.next()) {
                Map<String, String> records = new HashMap<>();
                records.put("enrollment_id", rs.getString("enrollment_id"));
                // We can now correctly get the student name from the joined result set.
                records.put("stu_name", rs.getString("stu_name"));
                records.put("date", rs.getString("date"));
                records.put("status", rs.getString("status"));
                records.put("marked_by", rs.getString("marked_by"));
                attendanceData.add(records);
            }

            // Check if any data was found before proceeding
            if (attendanceData.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No attendance records found for course: " + courseCode);
                return;
            }

            // Set the HTTP headers to trigger a file download
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Attendance_" + courseCode + ".xlsx\"");

            // Get the output stream and write the Excel data to it
            try (OutputStream outputStream = response.getOutputStream()) {
                ExcelUtil.exportAttendanceToExcel(outputStream, courseCode, courseName, attendanceData);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            // Send an internal server error for database issues.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred while exporting attendance.");
        } finally {
            // Close all database resources in the finally block to prevent resource leaks
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}*/

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

public class ExportAttendanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String courseCode = request.getParameter("courseCode");
        String courseName = request.getParameter("courseName");
        String teacherName = request.getParameter("teacherName"); // Assuming this is now passed from the frontend

        if (courseCode == null || courseCode.trim().isEmpty() || courseName == null || courseName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course Code and Course Name are required.");
            return;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> studentAttendanceSummary = new ArrayList<>();
        String firstAttendanceDate = "N/A";
        String currentDate = LocalDate.now().toString();

        try {
            connection = DatabaseConnection.getConnection();

            // First, get the earliest attendance date for the course.
            String firstDateQuery = "SELECT MIN(date) AS first_date FROM attendance WHERE course_code = ?";
            stmt = connection.prepareStatement(firstDateQuery);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();

            if (rs.next() && rs.getString("first_date") != null) {
                firstAttendanceDate = rs.getString("first_date");
            }
            rs.close();
            stmt.close();

            // Next, get the student attendance summary.
            String summaryQuery = "SELECT " +
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

            stmt = connection.prepareStatement(summaryQuery);
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

            if (studentAttendanceSummary.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No attendance records found for course: " + courseCode);
                return;
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Attendance_Summary_" + courseCode + ".xlsx\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                ExcelUtil.exportAttendanceSummaryToExcel(
                        outputStream,
                        courseCode,
                        courseName,
                        teacherName,
                        studentAttendanceSummary,
                        firstAttendanceDate, // New parameter
                        currentDate // New parameter
                );
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


