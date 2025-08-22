/*package com.attendease.controllers;

import com.attendease.services.AuthService;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    // Inner class to represent the expected JSON request body.
    private static class LoginRequest {
        String email;
        String password;
    }

    // Handles the preflight OPTIONS request from the browser for CORS.
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // Main logic for handling the POST login request.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Set CORS headers for the POST response.


        PrintWriter out = res.getWriter();
        Map<String, String> responseMap = new HashMap<>();

        try {
            // Read the JSON request body.
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // Parse the JSON into the LoginRequest object.
            LoginRequest loginRequest = gson.fromJson(sb.toString(), LoginRequest.class);

            // Check for missing data before calling the service. This prevents NullPointerExceptions.
            if (loginRequest == null || loginRequest.email == null || loginRequest.password == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("message", "Invalid request format. Missing email or password.");
                out.print(gson.toJson(responseMap));
                return; // Stop execution here
            }

            // Call the service to perform login logic.
            String role = authService.login(loginRequest.email, loginRequest.password);

            // Handle the response from the service.
            if ("teacher".equals(role) || "student".equals(role) || "admin".equals(role)) {
                responseMap.put("message", "Login Successful");
                responseMap.put("role", role);
                res.setStatus(HttpServletResponse.SC_OK);
            } else if ("invalid".equals(role)) {
                responseMap.put("message", "Invalid Credentials");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                // Catch any other unexpected roles from the service.
                responseMap.put("message", "Internal Server Error: Unexpected role returned.");
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            // This is the crucial part: catch any exception and return a structured error response.
            System.err.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("message", "Internal Server Error: " + e.getMessage());
        }

        out.print(gson.toJson(responseMap));
        out.flush();
    }
}*/

package com.attendease.controllers;

import com.attendease.services.AuthService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    // Inner class to represent the expected JSON request body.
    private static class LoginRequest {
        String email;
        String password;
    }

    // Handles the preflight OPTIONS request from the browser for CORS.
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // Main logic for handling the POST login request.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        PrintWriter out = res.getWriter();
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // Read request body
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // Parse request JSON into LoginRequest object
            LoginRequest loginRequest = gson.fromJson(sb.toString(), LoginRequest.class);

            // Validate request
            if (loginRequest == null || loginRequest.email == null || loginRequest.password == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMap.put("message", "Invalid request format. Missing email or password.");
                out.print(gson.toJson(responseMap));
                return;
            }

            // ✅ Get full user details including role, name, id
            Map<String, Object> userDetails = authService.loginWithDetails(loginRequest.email, loginRequest.password);

            if (userDetails != null) {
                responseMap.put("message", "Login Successful");
                responseMap.putAll(userDetails); // Merge user details
                res.setStatus(HttpServletResponse.SC_OK);
            } else {
                responseMap.put("message", "Invalid Credentials");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } catch (Exception e) {
            System.err.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseMap.put("message", "Internal Server Error: " + e.getMessage());
        }

        out.print(gson.toJson(responseMap));
        out.flush();
    }
}
