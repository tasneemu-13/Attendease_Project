package com.attendease.controllers;

import com.attendease.modals.AttendanceEntry;
import com.attendease.modals.AttendanceRequest;
import com.attendease.modals.ServerResponse;
import com.attendease.services.TeachServices;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class MarkAttendanceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            // Parse JSON from request body
            String requestData = request.getReader().lines().collect(Collectors.joining());
            AttendanceRequest attendanceRequest = gson.fromJson(requestData, AttendanceRequest.class);

            // Extract data from the request
            String courseCode = attendanceRequest.getCourseCode();
            String className = attendanceRequest.getClassName();
            String markedBy = attendanceRequest.getMarkedBy(); // Consider getting this from session
            String date = attendanceRequest.getDate();

            TeachServices teachServices = new TeachServices();

            // Use a Set for present student IDs for efficient lookup
            Set<String> presentStudentIds = new HashSet<>();
            if (attendanceRequest.getAttendanceEntries() != null) {
                for (AttendanceEntry entry : attendanceRequest.getAttendanceEntries()) {
                    if (entry.isPresent()) {
                        presentStudentIds.add(entry.getEnrollmentId());
                    }
                }
            }

            // Get all students for the class
            String[] allStudentIds = teachServices.enrollmentIdFetching(className);

            String[] presentStudentsArray = new String[presentStudentIds.size()];
            String[] absentStudentsArray = new String[allStudentIds.length - presentStudentIds.size()];

            int presentIndex = 0;
            int absentIndex = 0;

            // Loop through all students to separate them into present and absent arrays
            for (String studentId : allStudentIds) {
                if (presentStudentIds.contains(studentId)) {
                    presentStudentsArray[presentIndex++] = studentId;
                } else {
                    absentStudentsArray[absentIndex++] = studentId;
                }
            }

            // ... (rest of your logic to mark attendance and send the response) ...
            boolean presentSuccess = teachServices.markAttendance(presentStudentsArray, courseCode, date, "Present", markedBy);
            boolean absentSuccess = teachServices.markAttendance(absentStudentsArray, courseCode, date, "Absent", markedBy);

            if (presentSuccess && absentSuccess) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(new ServerResponse(true, "Attendance marked successfully!")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(new ServerResponse(false, "Failed to mark attendance.")));
            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ServerResponse(false, "Database error: " + e.getMessage())));
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ServerResponse(false, "Invalid request format: " + e.getMessage())));
            e.printStackTrace();
        }
    }

}