package com.attendease.services;

import com.attendease.modals.StudentAttendanceReport;
import com.attendease.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StuService {

    private Connection connection;
    private PreparedStatement stmt;
    private ResultSet rs;
    public StuService(){
        try{
            this.connection = DatabaseConnection.getConnection();
        }catch(SQLException e){
            throw  new RuntimeException();
        }
    }

    public Boolean viewAttendance(String enrollmentId){

        stmt = null;
        rs = null;
        try{
            connection = DatabaseConnection.getConnection();
            String viewQuery = "SELECT * FROM attendance where enrollment_id = ?";
            stmt = connection.prepareStatement(viewQuery);
            stmt.setString(1,enrollmentId);
            rs = stmt.executeQuery();
            if (rs.next()) {

                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        finally{
            try{
                if(stmt!=null) stmt.close();
                if (rs!=null) rs.close();
                if(connection!=null) connection.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    //console based
    public void viewProfile(String enrollmentId){

        stmt = null;
        rs = null;
        try{
            connection = DatabaseConnection.getConnection();
            String viewQuery = "SELECT * FROM Student where enrollment_id = ?";
            stmt = connection.prepareStatement(viewQuery);
            stmt.setString(1,enrollmentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                    String eId = rs.getString("enrollment_id");
                    int userId = rs.getInt("user_id");
                    String studentName = rs.getString("studentName");
                    String className = rs.getString("className");
                    int year = rs.getInt("year");

                    System.out.println("Enrollment ID: " + eId);
                    System.out.println("User ID: " + userId);
                    System.out.println("Student Name: " + studentName);
                    System.out.println("Class Name: " + className);
                    System.out.println("Year: " + year);
            }

            else{
                 System.out.println("Student Profile not found!");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        }
        finally{
            try{
                if(stmt!=null) stmt.close();
                if (rs!=null) rs.close();
                if(connection!=null) connection.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    // web based
    public Map<String, Object> getStudentProfile(String enrollmentId) {
        Map<String, Object> studentProfile = null;
        Connection localConnection = null;
        PreparedStatement localStmt = null;
        ResultSet localRs = null;

        try {
            localConnection = DatabaseConnection.getConnection();
            String viewQuery = "SELECT " +
                    "S.enrollment_id, " +
                    "S.user_id, " +
                    "S.stu_name AS studentName, " +
                    "S.class_name AS className, " +
                    "S.year, " +
                    "S.program, " +
                    "S.branch, " +
                    "S.semster , " +
                    "U.email_id AS email " +
                    "FROM Student S " +
                    "JOIN User U ON S.user_id = U.user_id " +
                    "WHERE S.enrollment_id = ?";
            localStmt = localConnection.prepareStatement(viewQuery);
            localStmt.setString(1, enrollmentId);
            localRs = localStmt.executeQuery();

            if (localRs.next()) {
                studentProfile = new HashMap<>();
                studentProfile.put("enrollmentId", localRs.getString("enrollment_id"));
                studentProfile.put("userId", localRs.getInt("user_id"));
                studentProfile.put("studentName", localRs.getString("studentName"));
                studentProfile.put("className", localRs.getString("className"));
                studentProfile.put("year", localRs.getInt("year"));
                studentProfile.put("email", localRs.getString("email"));
                studentProfile.put("program", localRs.getString("program"));
                studentProfile.put("branch", localRs.getString("branch"));
                studentProfile.put("semster", localRs.getInt("semster"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception in getStudentProfile: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (localRs != null) localRs.close();
                if (localStmt != null) localStmt.close();
                if (localConnection != null) localConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return studentProfile;
    }

    public void updateProfile(String enrollemntId , String course_code , String course_name , String class_name , Integer year) {
        Connection connection = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            String stuQuery = "UPDATE  Student SET class_name = ? , year = ? WHERE enrollment_id = ?";
            stmt1 = connection.prepareCall(stuQuery);
            stmt1.setString(1,class_name);
            stmt1.setInt(2,year);
            String courseQuery = "UPDATE  Courses SET course_code = ? , course_name = ?  WHERE class_name = ? ";
            stmt2 = connection.prepareStatement(courseQuery);
            stmt2.setString(1, course_code);
            stmt2.setString(2, class_name);
            stmt2.setString(3, course_code);
            int stuRows = stmt1.executeUpdate();
            int courRows = stmt2.executeUpdate();
            if (stuRows > 0 || courRows > 0) {
                System.out.println("Profile Updated Successfully");
            } else {
                System.out.println("No records updated. Please check the input data");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (stmt1 != null) stmt1.close();
                if( stmt2!= null) stmt2.close();
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //web based
    public boolean updateStuProfile(String enrollmentId, String className, int year, String program, String branch, int semster) {
        Connection localConnection = null;
        PreparedStatement stmt = null;
        boolean updated = false;

        try {
            localConnection = DatabaseConnection.getConnection();
            localConnection.setAutoCommit(false); // Start transaction

            String updateQuery = "UPDATE Student SET class_name = ?, year = ?, semster = ? WHERE enrollment_id = ?";
            stmt = localConnection.prepareStatement(updateQuery);

            stmt.setString(1, className);
            stmt.setInt(2, year);
            stmt.setInt(3, semster);
            stmt.setString(4, enrollmentId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                localConnection.commit(); // Commit if successful
                updated = true;
            } else {
                localConnection.rollback(); // Rollback if no rows were affected
                updated = false;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception in updateProfile: " + e.getMessage());
            e.printStackTrace();
            try {
                if (localConnection != null) {
                    localConnection.rollback(); // Rollback on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (localConnection != null) localConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return updated;
    }


    public Map<String, Object> getAttendanceReport(String enrollmentId, int year, int semster) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Map<String, Object> reportData = new HashMap<>();
        List<StudentAttendanceReport> attendanceReport = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();

            // Query 1: Fetch Student Details
            // This query is now simpler as it focuses only on student information.
            String studentDetailsSql = "SELECT s.stu_name, s.enrollment_id, s.class_name, s.year " +
                    "FROM student s " +
                    "WHERE s.enrollment_id = ?";
            stmt = connection.prepareStatement(studentDetailsSql);
            stmt.setString(1, enrollmentId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("studentName", rs.getString("stu_name"));
                student.put("enrollmentId", rs.getString("enrollment_id"));
                student.put("className", rs.getString("class_name"));
                student.put("year", rs.getInt("year"));

                reportData.put("student", student);
            }
            rs.close();
            stmt.close();

            // Query 2: Fetch Attendance Report with Subject Teacher's Name
            // This query joins with the teacher and teacher_courses tables to get the teacher's name for each course.
            // Corrected SQL query in your Java code
            String attendanceSql = "SELECT c.course_code AS courseCode, " +
                    "t.name AS teacherName, " +
                    "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_count, " +
                    "COUNT(a.attendance_id) AS total_count " +
                    "FROM attendance a " +
                    "JOIN student s ON a.enrollment_id = s.enrollment_id " +
                    "JOIN courses c ON a.course_code = c.course_code " +
                    "JOIN teacher_courses tc ON c.course_code = tc.course_code " +
                    "JOIN teacher t ON tc.user_id = t.user_id " + // <-- Fix
                    "WHERE a.enrollment_id = ? AND s.year = ? AND s.semster = ? " + // <-- The fix
                    "GROUP BY c.course_code, t.name"; // Corrected GROUP BY clause
            stmt = connection.prepareStatement(attendanceSql);
            stmt.setString(1, enrollmentId);
            stmt.setInt(2, year);
            stmt.setInt(3, semster);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String courseCode = rs.getString("courseCode");
                String teacherName = rs.getString("teacherName");
                int presentCount = rs.getInt("present_count");
                int totalCount = rs.getInt("total_count");
                int absentCount = totalCount - presentCount;

                attendanceReport.add(new StudentAttendanceReport(courseCode, teacherName, presentCount, absentCount, totalCount));
            }
            reportData.put("attendanceReport", attendanceReport);

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
        }

        return reportData;
    }



}


