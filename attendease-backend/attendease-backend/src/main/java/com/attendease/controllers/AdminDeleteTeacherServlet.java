package com.attendease.controllers;

import com.google.gson.Gson;
import com.attendease.services.AdminServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // Important: Add this annotation
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AdminDeleteTeacherServlet extends HttpServlet {

    private AdminServices adminServices;


    @Override
    public void init() throws ServletException {
        super.init();
        adminServices = new AdminServices();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        // Declare userIdString and userId outside the try block
        // to make them accessible in the catch block for logging.
        String userIdString = null;
        Integer userId = null; // Use Integer object to allow null for initial check

        try {
            // Parse the request body as JSON
            // Assuming the frontend sends a JSON object like:
            // { "userId": 123 }  (where 123 is an integer, but JSON parsers often give it as Double/Long if not specified)
            // It's safer to read it as a Map<String, Object> or directly parse an Integer.
            Map<String, Object> requestBody = gson.fromJson(req.getReader(), Map.class); // Use Object for value
            Object idObject = requestBody.get("userId"); // Get the object associated with "userId"

            if (idObject == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "User ID is required for teacher deletion.")));
                return;
            }

            // The GSON default deserialization for numbers often results in Double for JSON numbers.
            // Convert it to String first, then parse to Integer.
            userIdString = String.valueOf(idObject);
            userId = Integer.parseInt(userIdString);


            String result = adminServices.deleteTeacher(userId);

            // Handle the result from the service method
            if (result.equals("Teacher Deleted Successfully")) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(Map.of("message", "Teacher deleted successfully!")));
            } else if (result.startsWith("Error")) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message", result)));
            } else { // "Teacher not found or could not be deleted"
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // If not found, 404 is appropriate
                out.print(gson.toJson(Map.of("message", result)));
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(Map.of("message", "Invalid User ID format. Please provide a valid number.")));
            System.err.println("Invalid number format for userId: " + userIdString + " - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in AdminDeleteTeacherServlet (doDelete) for user Id: " + (userId != null ? userId : "N/A") + " - " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("message", "An unexpected error occurred while deleting the teacher: " + e.getMessage())));
        } finally {
            out.flush();
        }
    }
}