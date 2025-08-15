package com.attendease.controllers;

import com.attendease.services.AdminServices;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ViewAllUsersServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException {
        response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods","GET,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers","Content-Type");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        try{
            AdminServices adminServices = new AdminServices();
            List<Map<String,String>> users = adminServices.viewAllUsers();

            if(users.isEmpty()){
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                out.print(gson.toJson(Map.of("message","No users available")));
            }else{
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(users));
            }
        }catch(Exception e){
            System.err.println("Error in ViewAllUsersServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("message", "Server error: " + e.getMessage())));
        } finally {
            out.flush();
        }
        }

}
