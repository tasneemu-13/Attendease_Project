package com.attendease.controllers;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewTimeTableServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enrollment = request.getParameter("enrollment");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();


        if (enrollment == null || enrollment.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Enrollment number is required\"}");
            return;
        }


        try {
            // Connection setup
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/attendease", "root", "yourpassword");


            // Get batch of student
            PreparedStatement ps1 = con.prepareStatement("SELECT batch FROM students WHERE enrollment_no = ?");
            ps1.setString(1, enrollment);
            ResultSet rs1 = ps1.executeQuery();


            if (!rs1.next()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\":\"Student not found\"}");
                return;
            }


            String batch = rs1.getString("batch");


            // Get timetable of batch
            PreparedStatement ps2 = con.prepareStatement("SELECT day, time_slot, subject FROM timetable WHERE batch = ?");
            ps2.setString(1, batch);
            ResultSet rs2 = ps2.executeQuery();


            ArrayList<HashMap<String, String>> timetable = new ArrayList<>();
            while (rs2.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("day", rs2.getString("day"));
                row.put("time_slot", rs2.getString("time_slot"));
                row.put("subject", rs2.getString("subject"));
                timetable.add(row);
            }


            // Return as JSON
            Gson gson = new Gson();
            out.print(gson.toJson(timetable));


            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Something went wrong\"}");
        }
    }
}



