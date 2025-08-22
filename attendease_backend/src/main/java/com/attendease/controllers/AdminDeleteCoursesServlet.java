package com.attendease.controllers;

import com.attendease.services.AdminServices;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AdminDeleteCoursesServlet extends HttpServlet{
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

            String courseCode = null;
            try{
                // For DELETE requests, the body might not always be directly available
                // or preferred for simple deletions. Often, ID is in path or query param.
                // However, if sending JSON body via fetch DELETE, this works.

                Map<String,String> requestBody = gson.fromJson(request.getReader(),Map.class);
                courseCode = requestBody.get("courseCode");

                //Basic Validation
                if(courseCode == null|| courseCode.trim().isEmpty()){
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(Map.of("message","Course Code is required for deletion")));
                    return;
                }
                String result = adminServices.deleteCourse(courseCode);

                if(result.equals("Course Deleted Successfully")){
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(gson.toJson(Map.of("message","Course deleted Successfully!")));
                }else if (result.startsWith("Error")) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print(gson.toJson(Map.of("message", result)));
                } else { // "Course not found or could not be deleted"
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(Map.of("message", result)));
                }
            }catch (Exception e) {
                System.err.println("Error in AdminDeleteCoursesServlet (doDelete) for course code: " + (courseCode != null ? courseCode : "N/A") + " - " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("message", "An unexpected error occurred while deleting the course: " + e.getMessage())));
            } finally {
                out.flush();
            }
        }


}


