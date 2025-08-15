/*package com.attendease.services;

import com.attendease.utils.DatabaseConnection;
import com.attendease.utils.ExcelUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminServices {


    private Connection connection;
    private PreparedStatement stmt;
    private ResultSet rs;

    public AdminServices(Connection connection) {
        try{
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    //add courses
    public String addCourses(String course_code , String course_name , String class_name, int year ){
        connection = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        try {
            connection = DatabaseConnection.getConnection();

            String addQuery = "INSERT INTO Courses (course_code, course_name, class_name, year) VALUES (?, ?, ?,?)";

            stmt = connection.prepareStatement(addQuery);
            stmt.setString(1, course_code);
            stmt.setString(2, course_name);
            stmt.setString(3, class_name);
            stmt.setInt(4,year);

            //executeUpdate is used for insertion query in sql through java
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                return "Added Successfully";
            } else {
                return "Failed";
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return "Error";
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Delete Courses

    public String delCourses(String course_code){
        connection = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        try{
            connection = DatabaseConnection.getConnection();

            String delQuery = "DELETE FROM Courses WHERE course_code = ?";
            stmt = connection.prepareStatement(delQuery);
            int rowsDeleted = stmt.executeUpdate();

            if(rowsDeleted > 0 ){
                return " Course Deleted Successfully"; }
            else{
                return"Course not found or could not be deleted";}

        }catch(SQLException e){
            System.out.println("SQL Error: " + e.getMessage());
            return "Error";
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    //Delete Student
    public String delStudent(String enrollmentId){
        connection = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        try{
            connection = DatabaseConnection.getConnection();

            String delStudentQuery = "DELETE FROM Student WHERE enrollmentId = ?";
            stmt = connection.prepareStatement(delStudentQuery);
            int rowsDeleted = stmt.executeUpdate();

            if(rowsDeleted > 0 ){
                return " Student Deleted Successfully"; }
            else{
                return"Student not found or could not be deleted";}

        }catch(SQLException e){
            System.out.println("SQL Error: " + e.getMessage());
            return "Error";
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    //Delete Teacher
    public String delTeacher(int userId){
        connection = null;
        PreparedStatement stmt = null;
        ResultSet rs;
        try{
            connection = DatabaseConnection.getConnection();

            String delTeacherQuery = "DELETE FROM Teacher WHERE user_id = ?";
            stmt = connection.prepareStatement(delTeacherQuery);
            int rowsDeleted = stmt.executeUpdate();

            return rowsDeleted > 0
                    ? "Teacher Deleted Successfully"
                    : "Teacher not found or could not be deleted";

        }catch(SQLException e){
            System.out.println("SQL Error: " + e.getMessage());
            return "Error";
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    //View all tables' data

    public void viewCourses(){
        PreparedStatement stmt = null;
        String viewQuery = "SELECT * FROM Courses";

        try{
            ResultSet rs = null;
            stmt = connection.prepareStatement(viewQuery);
            rs = stmt.executeQuery();
            System.out.println("Course Code | Course Name | Class Name");
            System.out.println("---------------------------------------");
            while(rs.next()){
                String code = rs.getString("course_code");
                String cName = rs.getString("course_name") ;
                String clName = rs.getString("class_name") ;
                System.out.println(code + " | " + cName + " | " + clName);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());

        }finally{
            try{
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void viewStudents() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String viewQuery = "SELECT * FROM Student";

        try {

            stmt = connection.prepareStatement(viewQuery);
            rs = stmt.executeQuery();
            System.out.println(" enrollment_id |  user_id |  stu_name | class_name | year");
            System.out.println("-----------------------------------------------------------");
            while (rs.next()) {
                String eId = rs.getString("enrollment_id");
                int uId = rs.getInt("user_id");
                String stuName = rs.getString("stu_name");
                String clName = rs.getString("class_name");
                int year = rs.getInt("year");
                System.out.println(eId + " | " + uId + " | " + stuName + " | " + clName + " | " + year);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewTeacher(){

            PreparedStatement stmt = null;
            ResultSet rs = null;
            String viewTeachQuery = "SELECT * FROM Teacher";

            try{

                stmt = connection.prepareStatement(viewTeachQuery);
                rs = stmt.executeQuery();
                System.out.println(" TAC |  user_id |  name  |");
                System.out.println("--------------------------");
                while(rs.next()){
                    String TAC = rs.getString("TAC");
                    int uId = rs.getInt("user_id") ;
                    String Name = rs.getString("name") ;
                    System.out.println(TAC + " | " + uId + " | " + Name + " | ");

                };


            }catch(SQLException e){
                System.out.println(e.getMessage());

            }finally{
                try{
                    if (stmt != null) stmt.close();
                    if(rs!=null) rs.close();
                    if (connection != null) connection.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    }

    public void viewUser(){

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String viewUserQuery = "SELECT * FROM User";

        try{

            stmt = connection.prepareStatement(viewUserQuery);
            rs = stmt.executeQuery();
            System.out.println(" user_id |  email_id |  password  |");
            System.out.println("-----------------------------------");
            while(rs.next()){
                int uId = rs.getInt("user_id") ;
                String mail = rs.getString("email_id");
                String pass = rs.getString("password") ;
                System.out.println(uId + " | " + mail + " | " + pass + " | ");

            };


        }catch(SQLException e){
            System.out.println(e.getMessage());

        }finally{
            try{
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if (connection != null) connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public void printReport(String enrollmentId) {
        connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            // Get present count grouped by course_code
            String query = "SELECT course_code, COUNT(*) as present_count " +
                    "FROM attendance " +
                    "WHERE enrollment_id = ? AND Status = 'Present' " +
                    "GROUP BY course_code";

            stmt = connection.prepareStatement(query);
            stmt.setString(1, enrollmentId);
            rs = stmt.executeQuery();

            // Create dataset
            DefaultPieDataset dataset = new DefaultPieDataset();
            int totalPresent = 0;

            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                int presentCount = rs.getInt("present_count");
                dataset.setValue(courseCode, presentCount);
                totalPresent += presentCount;
            }

            if (dataset.getItemCount() == 0) {
                System.out.println("No attendance data found for this student.");
                return;
            }

            // Create Pie Chart
            JFreeChart chart = ChartFactory.createPieChart(
                    "Overall Attendance Distribution for " + enrollmentId,
                    dataset,
                    true, true, false
            );

            // Show chart in a window
            ChartFrame frame = new ChartFrame("Attendance Report", chart);
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //exproting attendance

    public void exportAttendanceForAdmin(String filePath ,String courseCode,String courseName ){

        connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            connection = DatabaseConnection.getConnection();
            List<Map<String,String>> attendanceData = new ArrayList<>();
            String query = "SELECT  * FROM  attendance where course_code = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1,courseCode);
            rs = stmt.executeQuery();

            while(rs.next()){
                Map<String , String> records = new HashMap<>();
                records.put("enrollment_id", rs.getString("enrollment_id"));
                records.put("stu_name", rs.getString("stu_name"));
                records.put("date", rs.getString("date"));
                records.put("status", rs.getString("status"));
                records.put("marked_by", rs.getString("marked_by"));
                attendanceData.add(records);

                if(attendanceData.isEmpty()){
                    System.out.println("No attendance records found for this course.");
                }
                else{
                    ExcelUtil.exportAttendanceToExcel(filePath,courseCode,courseName,attendanceData);
                }
            }

        }
        catch (SQLException e) {
            System.out.println("Error while exporting attendance: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(rs!=null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}*/

package com.attendease.services;

import com.attendease.utils.DatabaseConnection; // Assuming this provides static getConnection()
import com.attendease.utils.ExcelUtil; // Assuming this class exists
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminServices {

    // No need for class-level Connection, PreparedStatement, ResultSet fields
    // as connections should be opened and closed per method call.

    // Constructor - now empty or can be removed if not needed for dependency injection
    public AdminServices() {
        // No connection initialization here.
        // Connections will be obtained when a method needs them.
    }

    // --- Course Management ---

    /**
     * Adds a new course to the database.
     * @param course_code The unique code for the course.
     * @param course_name The name of the course.
     * @param year The year of the course.
     * @return A status message (e.g., "Added Successfully", "Failed", "Error").
     */
    public String addCourse(String course_code, String course_name, int year) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DatabaseConnection.getConnection(); // Get connection
            String addQuery = "INSERT INTO Courses (course_code, course_name, year) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(addQuery);
            stmt.setString(1, course_code);
            stmt.setString(2, course_name);
            stmt.setInt(3, year);

            int rowsInserted = stmt.executeUpdate();
            return (rowsInserted > 0) ? "Added Successfully" : "Failed";

        } catch (SQLException e) {
            System.err.println("SQL Error adding course: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
            return "Error: " + e.getMessage(); // Return specific error message
        } finally {
            // Ensure resources are closed in reverse order of creation
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes a course from the database by course code.
     * @param course_code The code of the course to delete.
     * @return A status message.
     */
    public String deleteCourse(String course_code) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DatabaseConnection.getConnection();
            String delQuery = "DELETE FROM Courses WHERE course_code = ?";
            stmt = connection.prepareStatement(delQuery);
            stmt.setString(1, course_code);
            int rowsDeleted = stmt.executeUpdate();

            return (rowsDeleted > 0) ? "Course Deleted Successfully" : "Course not found or could not be deleted";

        } catch (SQLException e) {
            System.err.println("SQL Error deleting course: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // --- Student Management ---

    /**
     * Deletes a student from the database by enrollment ID.
     * @param enrollmentId The enrollment ID of the student to delete.
     * @return A status message.
     */
    public String deleteStudent(String enrollmentId) {
        Connection connection = null;
        PreparedStatement delAttendanceStmt = null;
        PreparedStatement delStudentStmt = null;
        PreparedStatement findUserIdStmt = null; // New statement to find the user_id
        PreparedStatement delUserStmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Start a transaction

            // Step 1: Find the user_id associated with the enrollmentId
            String findUserIdQuery = "SELECT user_id FROM Student WHERE enrollment_id = ?";
            findUserIdStmt = connection.prepareStatement(findUserIdQuery);
            findUserIdStmt.setString(1, enrollmentId);
            ResultSet rs = findUserIdStmt.executeQuery();
            String userId = null;
            if (rs.next()) {
                userId = rs.getString("user_id");
            }

            // If no student is found with the given enrollmentId, we can't proceed
            if (userId == null) {
                connection.rollback();
                return "Student not found or could not be deleted";
            }

            // Step 2: Delete records from the child table (e.g., Attendance)
            String delAttendanceQuery = "DELETE FROM Attendance WHERE enrollment_id = ?";
            delAttendanceStmt = connection.prepareStatement(delAttendanceQuery);
            delAttendanceStmt.setString(1, enrollmentId);
            delAttendanceStmt.executeUpdate();

            // Step 3: Delete the student record from the Student table
            String delStudentQuery = "DELETE FROM Student WHERE enrollment_id = ?";
            delStudentStmt = connection.prepareStatement(delStudentQuery);
            delStudentStmt.setString(1, enrollmentId);
            int studentRowsDeleted = delStudentStmt.executeUpdate();

            // Step 4: Delete the student's user record from the User table using the retrieved userId
            String delUserQuery = "DELETE FROM User WHERE user_id = ?";
            delUserStmt = connection.prepareStatement(delUserQuery);
            delUserStmt.setString(1, userId);
            delUserStmt.executeUpdate();

            // All deletions were successful, commit the transaction
            connection.commit();
            return "Student and related data deleted successfully";

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    System.err.println("Transaction is being rolled back.");
                    connection.rollback(); // Rollback if any part of the transaction fails
                } catch (SQLException excep) {
                    System.err.println("Error rolling back transaction: " + excep.getMessage());
                }
            }
            System.err.println("SQL Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (delAttendanceStmt != null) delAttendanceStmt.close();
                if (delStudentStmt != null) delStudentStmt.close();
                if (findUserIdStmt != null) findUserIdStmt.close();
                if (delUserStmt != null) delUserStmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statements: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    // --- Teacher Management ---

    /**
     * Deletes a teacher from the database by user ID.
     * @param userId The user ID of the teacher to delete.
     * @return A status message.
     */
    public String deleteTeacher(int userId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DatabaseConnection.getConnection();
            String delTeacherQuery = "DELETE FROM Teacher WHERE user_id = ?";
            stmt = connection.prepareStatement(delTeacherQuery);
            stmt.setInt(1, userId);
            int rowsDeleted = stmt.executeUpdate();

            return (rowsDeleted > 0) ? "Teacher Deleted Successfully" : "Teacher not found or could not be deleted";

        } catch (SQLException e) {
            System.err.println("SQL Error deleting teacher: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // --- View Data Methods (Returning List of Maps for Frontend) ---

    /**
     * Retrieves all courses from the database.
     * @return A list of maps, where each map represents a course.
     */
    public List<Map<String, String>> viewAllCourses() {
        List<Map<String, String>> courses = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String viewQuery = "SELECT course_code, course_name, year FROM Courses"; // Include year

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(viewQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> course = new HashMap<>();
                course.put("course_code", rs.getString("course_code"));
                course.put("course_name", rs.getString("course_name"));
                course.put("year", String.valueOf(rs.getInt("year"))); // Convert int to String
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error viewing courses: " + e.getMessage());
            e.printStackTrace();
            // Consider throwing a custom exception or returning an empty list
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return courses;
    }


    /**
     * Retrieves all teachers from the database.
     * @return A list of maps, where each map represents a teacher.
     */
    public List<Map<String, String>> viewAllTeachers() {
        List<Map<String, String>> teachers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String viewTeachQuery = "SELECT teacher_id, TAC, user_id, name FROM Teacher";

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(viewTeachQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> teacher = new HashMap<>();
                teacher.put("teacher_id",rs.getString("teacher_id"));
                teacher.put("TAC", rs.getString("TAC"));
                teacher.put("user_id", String.valueOf(rs.getInt("user_id")));
                teacher.put("name", rs.getString("name"));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error viewing teachers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return teachers;
    }

    /**
     * Retrieves all users from the database (excluding password for security).
     * @return A list of maps, where each map represents a user.
     */
    public List<Map<String, String>> viewAllUsers() {
        List<Map<String, String>> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        // IMPORTANT: Never retrieve or expose hashed passwords to the frontend
        String viewUserQuery = "SELECT user_id, email_id, role FROM User"; // Assuming a 'role' column

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(viewUserQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("user_id", String.valueOf(rs.getInt("user_id")));
                user.put("email_id", rs.getString("email_id"));
                user.put("role", rs.getString("role")); // Include role if it exists
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error viewing users: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return users;
    }


    // --- Reporting ---

    /**
     * Generates and displays a pie chart report for student attendance.
     * This method directly interacts with UI (JFreeChart) and might be better separated
     * if you plan a purely backend API or a web-based charting solution.
     * @param enrollmentId The enrollment ID of the student.
     */
    public void printReport(String enrollmentId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT course_code, COUNT(*) as present_count " +
                    "FROM attendance " +
                    "WHERE enrollment_id = ? AND Status = 'Present' " +
                    "GROUP BY course_code";

            stmt = connection.prepareStatement(query);
            stmt.setString(1, enrollmentId);
            rs = stmt.executeQuery();

            DefaultPieDataset dataset = new DefaultPieDataset();
            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                int presentCount = rs.getInt("present_count");
                dataset.setValue(courseCode, presentCount);
            }

            if (dataset.getItemCount() == 0) {
                System.out.println("No attendance data found for this student.");
                // Potentially throw an exception or return a status to the caller
                return;
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Overall Attendance Distribution for " + enrollmentId,
                    dataset,
                    true, true, false
            );

            ChartFrame frame = new ChartFrame("Attendance Report", chart);
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            System.err.println("SQL Error generating report: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // --- Exporting Data ---

    /**
     * Exports attendance data for a specific course to an Excel file.
     * @param courseCode The code of the course to export attendance for.
     * @param courseName The name of the course (used for file naming/sheet name).
     */
    public void exportAttendanceForAdmin(OutputStream outputStream, String courseCode, String courseName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> attendanceData = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT enrollment_id, stu_name, date, status, marked_by FROM attendance WHERE course_code = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> records = new HashMap<>();
                records.put("enrollment_id", rs.getString("enrollment_id"));
                records.put("stu_name", rs.getString("stu_name"));
                records.put("date", rs.getString("date"));
                records.put("status", rs.getString("status"));
                records.put("marked_by", rs.getString("marked_by"));
                attendanceData.add(records);
            }

            // Export only if data exists, and outside the loop
            if (attendanceData.isEmpty()) {
                System.out.println("No attendance records found for this course: " + courseCode);
            } else {
                // This is the corrected line.
                ExcelUtil.exportAttendanceToExcel(outputStream, courseCode, courseName, attendanceData);
                System.out.println("Attendance exported successfully."); // Simplified message
            }

        } catch (SQLException | IOException e) {
            System.err.println("Error while exporting attendance: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    //View All Students
    public List<Map<String, String>> viewAllStudents() {
        List<Map<String, String>> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String viewQuery = "SELECT  enrollment_id , user_id , stu_name , class_name , year , program , branch , semster FROM Student"; // Include year

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(viewQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> student = new HashMap<>();
                student.put("enrollment_id", rs.getString("enrollment_id"));
                student.put("user_id", String.valueOf(rs.getInt("user_id")));
                student.put("stu_name", rs.getString("stu_name"));
                student.put("class_name", rs.getString("class_name"));
                student.put("year", String.valueOf(rs.getInt("year")));
                student.put("program", rs.getString("program"));
                student.put("branch",rs.getString("branch"));
                student.put("semster",String.valueOf(rs.getInt("semster")));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error viewing students " + e.getMessage());
            e.printStackTrace();
            // Consider throwing a custom exception or returning an empty list
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return students;
    }



}


