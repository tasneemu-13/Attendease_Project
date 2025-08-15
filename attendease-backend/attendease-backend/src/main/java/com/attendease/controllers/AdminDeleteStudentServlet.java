package com.attendease.controllers;
import com.google.gson.Gson;
import com.attendease.services.AdminServices;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AdminDeleteStudentServlet extends HttpServlet {
    private AdminServices adminServices;

    @Override
    public void init() throws ServletException{
        super.init();
        adminServices = new AdminServices();
    }

    @Override
    protected void doDelete(HttpServletRequest request , HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String enrollmentId = null;

        try{
            // Parse the request body as JSON
            // Assuming the frontend sends a JSON object like:
            // { "enrollmentId": "12345" }
            Map<String , String> requestBody = gson.fromJson(request.getReader(),Map.class);
            enrollmentId = requestBody.get("enrollmentId"); // Get the enrollment ID to delete

            if(enrollmentId == null || enrollmentId.trim().isEmpty()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("message", "Enrollment ID is required for student deletion.")));
                return;
            }

           String result = adminServices.deleteStudent(enrollmentId);

            // Handle the result from the service method
            if (result.equals("Student Deleted Successfully")) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(Map.of("message", "Student deleted successfully!")));
            } else if (result.startsWith("Error")) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message", result)));
            } else { // "Student not found or could not be deleted"
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(Map.of("message", result)));
            }

        } catch (Exception e) {
            System.err.println("Error in AdminDeleteStudentServlet (doDelete) for enrollment ID: " + (enrollmentId != null ? enrollmentId : "N/A") + " - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("message", "An unexpected error occurred while deleting the student: " + e.getMessage())));
        } finally {
            out.flush();
        }
    }
}

