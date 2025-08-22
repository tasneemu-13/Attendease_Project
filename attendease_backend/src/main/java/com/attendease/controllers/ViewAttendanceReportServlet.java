package com.attendease.controllers;

import com.attendease.modals.ServerResponse;
import com.attendease.services.StuService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;


public class ViewAttendanceReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            // 1. Get request Parameters and correct typos
            String enrollmentId = request.getParameter("enrollmentId");
            String semsterStr = request.getParameter("semster");
            String yearStr = request.getParameter("year");

            // Basic Validation
            if (enrollmentId == null || enrollmentId.isEmpty() || semsterStr == null || semsterStr.isEmpty() || yearStr == null || yearStr.isEmpty()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(new ServerResponse(false, "Missing required parameters: enrollmentId, year, or semester.")));
                return;
            }

            int year = Integer.parseInt(yearStr);
            int semster = Integer.parseInt(semsterStr);
            StuService studentServices = new StuService();

            // 2. The StuService method now returns a Map<String, Object>
            // to hold all the data (student details and attendance report).
            Map<String, Object> fullReportData = studentServices.getAttendanceReport(enrollmentId, year, semster);

            // 3. Set the status code and send the complete JSON response.
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(fullReportData));

        } catch(NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ServerResponse(false, "Invalid year or semester format. Please provide valid integers.")));
            e.printStackTrace();
        } catch(SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ServerResponse(false, "Database error: " + e.getMessage())));
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ServerResponse(false, "An unexpected error occurred: " + e.getMessage())));
            e.printStackTrace();
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Specify the origins allowed to access this resource.
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 2. Specify the HTTP methods allowed for this resource.
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");

        // 3. Specify which custom headers are allowed in the request.
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");

        // 4. Set the cache duration for the preflight request.
        response.setHeader("Access-Control-Max-Age", "3600");

        // 5. Send a 200 OK status code.
        response.setStatus(HttpServletResponse.SC_OK);
    }
}