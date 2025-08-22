package com.attendease.controllers;

import com.attendease.services.AuthService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class RegisterServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().write("Register endpoint is alive, but use POST method.");
    }



    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


        // Read JSON body from request
        String requestBody = request.getReader().lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(requestBody);

        AuthService authService = new AuthService();

        String email = json.optString("email");
        String password = json.optString("password");
        String role = json.optString("role");

        boolean result = false;

        if ("teacher".equalsIgnoreCase(role)) {
            String tac = json.optString("tac");
            String name = json.optString("name");
            result = authService.registerUser(email, password, tac, role, null, name, null, 0,null,null,null);
        }
        else if ("student".equalsIgnoreCase(role)) {
            String enrollmentId = json.optString("enrollmentId");
            String name = json.optString("name");
            String className = json.optString("className");
            int year = json.optInt("year");
            String program = json.optString("program");
            String branch = json.optString("branch");
            int semster = json.getInt("semster");

            result = authService.registerUser(email, password, null, role, enrollmentId, name, className, year,program,branch,semster);
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if (result) {
            out.print("{\"status\":\"success\"}");
        } else {
            out.print("{\"status\":\"fail\"}");
        }
    }
}
