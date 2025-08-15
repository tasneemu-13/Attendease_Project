package com.attendease.controllers;
import com.google.gson.Gson;
import com.attendease.services.AdminServices; // Import your AdminServices
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AdminAddCoursesServlet extends HttpServlet {

    private AdminServices adminServices;
    @Override
    public void init() throws ServletException {
        super.init();
        adminServices = new AdminServices();
    }

    /*@Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // These headers are CRITICAL for CORS preflight
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // Exact match for your React app's origin
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // Include ALL methods you allow
        response.setHeader("Access-Control-Allow-Headers", "Content-Type"); // Necessary for JSON bodies (like what React sends)
        response.setHeader("Access-Control-Allow-Credentials", "true"); // If you're using cookies/sessions
        response.setStatus(HttpServletResponse.SC_OK); // Must return 200 OK for OPTIONS
    }
    @Override
    protected void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException{

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            // Read the JSON request body
            Map<String, String> requestBody = gson.fromJson(request.getReader(), Map.class);

            // Extract parameters from the request body.
            // These keys must match what your frontend sends.
            // Example: { "courseCode": "...", "courseName": "...", "className": "...", "year": "..." }
            String courseCode = requestBody.get("courseCode"); // Make sure your frontend sends 'courseCode'
            String courseName = requestBody.get("courseName");
            String className = requestBody.get("className");
            String yearString = String.valueOf(requestBody.get("year"));; // Year will come as a String from JSON

            // Basic server-side validation
            if (courseCode == null || courseCode.trim().isEmpty() ||
                    courseName == null || courseName.trim().isEmpty() ||
                    className == null || className.trim().isEmpty() ||
                    yearString == null || yearString.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "All fields (Course Code, Course Name, Class Name, Year) are required.")));
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearString);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Year must be a valid number.")));
                return;
            }

            //call addCourses method from adminservices

            String result = adminServices.addCourse(courseCode,courseName,className,year);

            //handle the result from service method
            if(result.equals("Added Successfully")){
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(Map.of("message","Course added successfully!")));
            }else if (result.startsWith("Error")){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message",result)));
            }else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message","Failed to add course:"+result)));
            }
        } catch (Exception e) {
            System.err.println("Error in AdminAddCoursesServlet:"+e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("message", "An unexpected error occurred: " + e.getMessage())));
        } finally {
            out.flush();
        }
        }*/@Override
    protected void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException{

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        // Set response headers for JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // Ensure CORS is handled for actual requests too
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");


        try {
            // Read the JSON request body
            // It's safer to use Map<String, Object> because Gson might parse numbers as Double/Integer, not just String
            Map<String, Object> requestBody = gson.fromJson(request.getReader(), Map.class); // Changed to Object

            // Extract parameters from the request body.
            String courseCode = (String) requestBody.get("courseCode");
            String courseName = (String) requestBody.get("courseName");
            String className = (String) requestBody.get("className");
            Object yearObject = requestBody.get("year"); // Get year as an Object first

            // --- Robust validation and type conversion for 'year' ---
            int year;
            if (yearObject == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Year field is missing.")));
                return;
            }

            try {
                if (yearObject instanceof Number) {
                    year = ((Number) yearObject).intValue();
                } else if (yearObject instanceof String) {
                    String yearString = (String) yearObject;
                    if (yearString.trim().isEmpty()) { // Check for empty string
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print(gson.toJson(Map.of("message", "Year cannot be empty.")));
                        return;
                    }
                    year = Integer.parseInt(yearString);
                } else { // Handle other unexpected types
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(Map.of("message", "Year must be a valid number or number string.")));
                    return;
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Year must be a valid integer number.")));
                return;
            }

            // Basic server-side validation for other fields
            if (courseCode == null || courseCode.trim().isEmpty() ||
                    courseName == null || courseName.trim().isEmpty() )
                    { // Year already handled
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Course Code, and  Course Name are required.")));
                return;
            }

            // Call addCourses method from adminServices
            String result = adminServices.addCourse(courseCode, courseName, year);

            // Handle the result from service method
            if (result.equals("Added Successfully")) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(Map.of("message", "Course added successfully!")));
            } else if (result.startsWith("Error")) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message", result)));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message", "Failed to add course: " + result)));
            }
        } catch (Exception e) {
            System.err.println("Error in AdminAddCoursesServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("message", "An unexpected error occurred: " + e.getMessage())));
        } finally {
            out.flush();
        }
    }


}
