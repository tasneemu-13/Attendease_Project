package com.attendease.controllers;

import com.attendease.services.TeachServices;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class StudentListServlet extends HttpServlet {

    private TeachServices teachServices;

    @Override
    public void init()throws ServletException {
        teachServices = new TeachServices();
    }
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String className = request.getParameter("className");
        String yearStr = request.getParameter("year");
        String semStr = request.getParameter("semster");

        if (className == null || yearStr == null || semStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required parameters");
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            int semster = Integer.parseInt(semStr);

            List<Map<String, String>> students = teachServices.getStudentsByClassYearSem(className, year, semster);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(students));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving students: " + e.getMessage());
        }
    }

}



