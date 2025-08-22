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


public class StudentExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // --- ADDED LOGGING ---
        System.out.println("StudentExportServlet: Starting doGet method.");

        String enrollmentId = request.getParameter("enrollmentId");
        String courseCode = request.getParameter("courseCode");
        String studentName = request.getParameter("studentName");
        String className = request.getParameter("className");

        // --- ADDED LOGGING ---
        System.out.println("StudentExportServlet: Received parameters:");
        System.out.println("  - enrollmentId: " + enrollmentId);
        System.out.println("  - courseCode: " + courseCode);
        System.out.println("  - studentName: " + studentName);
        System.out.println("  - className: " + className);

        if (enrollmentId == null || enrollmentId.trim().isEmpty() || courseCode == null || courseCode.trim().isEmpty()) {
            // --- ADDED LOGGING ---
            System.err.println("StudentExportServlet: ERROR - Required parameters are null or empty. Sending 400 Bad Request.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Enrollment ID and Course Code are required.");
            return;
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> studentAttendanceData = new ArrayList<>();
        Map<String, Object> studentSummaryData = new HashMap<>();

        try {
            connection = DatabaseConnection.getConnection();
            System.out.println("StudentExportServlet: Database connection established.");

            // Query to get date-wise records for a single student and course
            String datewiseQuery = "SELECT a.date, a.status, a.marked_by " +
                    "FROM attendance a " +
                    "WHERE a.enrollment_id = ? AND a.course_code = ? " +
                    "ORDER BY a.date";

            // --- ADDED LOGGING ---
            System.out.println("StudentExportServlet: Preparing date-wise query: " + datewiseQuery);
            stmt = connection.prepareStatement(datewiseQuery);
            stmt.setString(1, enrollmentId);
            stmt.setString(2, courseCode);

            rs = stmt.executeQuery();
            System.out.println("StudentExportServlet: Date-wise query executed. Processing results.");

            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("date", rs.getString("date"));
                record.put("status", rs.getString("status"));
                record.put("marked_by", rs.getString("marked_by"));
                studentAttendanceData.add(record);
                // --- ADDED LOGGING ---
                System.out.println("StudentExportServlet: Found attendance record for date: " + rs.getString("date"));
            }
            rs.close();
            stmt.close();

            // Query to get total lectures and attended lectures for percentage calculation
            String summaryQuery = "SELECT " +
                    "    (SELECT COUNT(DISTINCT date) FROM attendance WHERE course_code = ?) AS total_lectures, " +
                    "    COUNT(CASE WHEN a.status = 'Present' THEN 1 END) AS attended_lectures " +
                    "FROM " +
                    "    attendance a " +
                    "WHERE " +
                    "    a.enrollment_id = ? AND a.course_code = ?";

            // --- ADDED LOGGING ---
            System.out.println("StudentExportServlet: Preparing summary query: " + summaryQuery);
            stmt = connection.prepareStatement(summaryQuery);
            stmt.setString(1, courseCode);
            stmt.setString(2, enrollmentId);
            stmt.setString(3, courseCode);
            rs = stmt.executeQuery();
            System.out.println("StudentExportServlet: Summary query executed. Processing results.");


            if (rs.next()) {
                studentSummaryData.put("total_lectures", rs.getInt("total_lectures"));
                studentSummaryData.put("attended_lectures", rs.getInt("attended_lectures"));
                // --- ADDED LOGGING ---
                System.out.println("StudentExportServlet: Summary data found. Total Lectures: " + rs.getInt("total_lectures") + ", Attended: " + rs.getInt("attended_lectures"));
            } else {
                System.out.println("StudentExportServlet: No summary data found.");
            }

            if (studentAttendanceData.isEmpty()) {
                // --- ADDED LOGGING ---
                System.err.println("StudentExportServlet: ERROR - No attendance records found. Sending 404 Not Found.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No attendance records found for student " + enrollmentId + " in course " + courseCode);
                return;
            }

            // --- ADDED LOGGING ---
            System.out.println("StudentExportServlet: Setting response headers and content type.");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"Student_Report_" + enrollmentId + "_" + courseCode + ".xlsx\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                // --- ADDED LOGGING ---
                System.out.println("StudentExportServlet: Calling ExcelUtil to generate report.");
                ExcelUtil.exportStudentAttendanceReport(
                        outputStream,
                        studentName,
                        enrollmentId,
                        className,
                        studentAttendanceData,
                        studentSummaryData
                );
                // --- ADDED LOGGING ---
                System.out.println("StudentExportServlet: Excel report generated and written to output stream.");
            }
        } catch (SQLException e) {
            // --- ADDED LOGGING ---
            System.err.println("StudentExportServlet: Database error occurred.");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred while exporting attendance.");
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("StudentExportServlet: An unexpected error occurred.");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected server error occurred.");
        } finally {
            try {
                // --- ADDED LOGGING ---
                System.out.println("StudentExportServlet: Closing database resources.");
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                // --- ADDED LOGGING ---
                System.err.println("StudentExportServlet: Error closing resources.");
                e.printStackTrace();
            }
            System.out.println("StudentExportServlet: Exiting doGet method.");
        }
    }
}
