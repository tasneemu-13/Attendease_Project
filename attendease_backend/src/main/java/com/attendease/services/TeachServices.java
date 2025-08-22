/*package com.attendease.services;

import com.attendease.models.Student;
import com.attendease.utils.DatabaseConnection;
import com.attendease.utils.ExcelUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeachServices {

      private Connection connection;
      private PreparedStatement stmt;
      private ResultSet rs;


      public TeachServices(){
          try{
              this.connection = DatabaseConnection.getConnection();
          } catch (SQLException e) {
              throw new RuntimeException(e);
          }

      }

       // 1. To mark whole batch attendance
       public boolean markAttendance(String[] enrollmentId, String courseCode, String date, String status, String markedBy) {
           Connection connection = null;
           PreparedStatement stmt = null;
           int insertRows = 0;
           try {
               connection = DatabaseConnection.getConnection();
               connection.setAutoCommit(false); // Start a transaction

               String attendQuery = "INSERT INTO attendance (enrollment_id, course_code, date, Status, marked_by) VALUES (?, ?, ?, ?, ?)";
               stmt = connection.prepareStatement(attendQuery);

               for (String s : enrollmentId) {
                   stmt.setString(1, s);
                   stmt.setString(2, courseCode);
                   stmt.setString(3, date);
                   stmt.setString(4, status);
                   stmt.setString(5, markedBy);
                   stmt.addBatch(); // Add statement to the batch
               }

               int[] batchResults = stmt.executeBatch(); // Execute the batch

               // Check if all updates were successful
               for (int result : batchResults) {
                   if (result > 0) {
                       insertRows++;
                   }
               }

               if (insertRows == enrollmentId.length) {
                   connection.commit(); // Commit the transaction
                   return true;
               } else {
                   connection.rollback(); // Rollback if not all were successful
                   return false;
               }

           } catch (SQLException e) {
               if (connection != null) {
                   try {
                       connection.rollback();
                   } catch (SQLException ex) {
                       ex.printStackTrace();
                   }
               }
               System.err.println("SQL ERROR: " + e.getMessage());
               return false;
           } finally {
               try {
                   if (stmt != null) stmt.close();
                   if (connection != null) connection.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
       }


       //2. Retrieving enrollment id of a particular batch
       public String[] enrollmentIdFetching(String className){

           PreparedStatement stmt = null;
           ResultSet rs = null;
        //for storing array  of enrollment ids
           List<String> enrollmentIds = new ArrayList<>();

              try{
                  connection = DatabaseConnection.getConnection();
                  String query = "SELECT enrollment_id FROM Student WHERE class_name = ?";
                  stmt = connection.prepareStatement(query);
                  stmt.setString(1,className);
                  rs = stmt.executeQuery();

                  while(rs.next()){
                      enrollmentIds.add(rs.getString("enrollment_id"));
                  }

              } catch (SQLException e) {
                  throw new RuntimeException(e);
              }finally {
                  try{
                      if(stmt!=null)stmt.close();
                      if(rs!=null) rs.close();
                      if(connection!=null)connection.close();
                  } catch (SQLException e) {
                      throw new RuntimeException(e);
                  }
              }

           // returning all enrollment id in the form of ["en..","en..",..]
              return enrollmentIds.toArray(new String[0]);
       }


       //WEBBASED

    public List<Student> getStudentsByClass(String className) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();

        try {
            connection = DatabaseConnection.getConnection();
            String query = "SELECT enrollment_id, name, class_name FROM Student WHERE class_name = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, className);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setEnrollmentId(rs.getString("enrollment_id"));
                student.setStudentName(rs.getString("name"));
                student.setClassName(rs.getString("class_name"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while fetching students: " + e.getMessage());
            e.printStackTrace();
            // You might want to throw a custom exception here for better error handling.
            return null;
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return students;
    }
       //3. To view attendance marked based on class and course as it would be unique

    public void viewAttendance(String course_code , String className){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            connection = DatabaseConnection.getConnection();

            String query = " select s.stu_name , a.course_code , a.date , a.status" +
                    "    from attendance a" +
                    "    join student s on a.enrollment_id = s.enrollment_id" +
                    "    where a.course_code = ? AND s.class_name = ?";

            stmt = connection.prepareStatement(query);
            stmt.setString(1,course_code);
            stmt.setString(2,className);
            rs = stmt.executeQuery();
            boolean found = false;
            System.out.println("Student Name | Course Code | Date | Status");
            System.out.println("------------------------------------------------");

            while(rs.next()){
                found = true;
                String stuName = rs.getString("stu_name");
                String cc = rs.getString("course_code");
                String date = rs.getString("date");
                String status = rs.getString("Status");

                System.out.println(stuName + " | " + cc + " | " + date + " | " + status);
            }
            if(!found){
                System.out.println("No attendance record found for given course code and class");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally{
            try{
                if (stmt!=null)stmt.close();
                if (rs!=null)rs.close();
                if(connection!=null)connection.close();
            }catch(SQLException e ){
                throw new RuntimeException(e);
            }
        }
    }


    //4. A teacher after registeration must provide list of courses he / she will be teaching

    public boolean addCoursesTeaching(int user_id, String course_code , String className) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DatabaseConnection.getConnection();
            String addQuery = "INSERT INTO teacher_courses (user_id, course_code , class_name ) VALUES (?, ? ,?)";
            stmt = connection.prepareStatement(addQuery);
            stmt.setInt(1, user_id);
            stmt.setString(2, course_code);
            stmt.setString(3,className);

            int insertRows = stmt.executeUpdate();
            if (insertRows > 0) {
                System.out.println("Courses Added Successfully! HAPPY TEACHING");
                return true;
            } else {
                System.out.println("Sorry! Adding Courses Failed");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (connection != null) connection.close(); } catch (SQLException ignored) {}
        }
    }



    //5.Teacher can view no of courses teaching -> console based
    public void viewCoursesTeaching(String teachName , String courseCode , String courseName){

        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean found = false;
        try{
            connection = DatabaseConnection.getConnection();
            String viewQuery = "SELECT t.name, c.course_code, c.course_name\n" +
                    "FROM teacher_courses tc\n" +
                    "JOIN teacher t ON tc.user_id = t.user_id\n" +
                    "JOIN courses c ON tc.course_code = c.course_code;\n";
            stmt = connection.prepareStatement(viewQuery);
            stmt.setString(1,teachName);
            stmt.setString(2,courseCode);
            stmt.setString(3,courseName);
            rs = stmt.executeQuery();

            while(rs.next()){
                System.out.println("Teacher Name | Course Code | Course Name");
                System.out.println("------------------------------------------------");
                String name = rs.getString("name");
                String cc = rs.getString("course_code");
                String cName = rs.getString("course_name");
            }

            if(!found){
                System.out.println("OOPS !! Sorry .. You have no matching records");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // web based
    public List<Map<String,String>> getCoursesTeaching(String teacherName)throws SQLException{
           List<Map<String,String>> courseList = new ArrayList<>();

        String viewQuery = "SELECT t.name, c.course_code, c.course_name, tc.class_name " +
                "FROM teacher_courses tc " +
                "JOIN users t ON tc.user_id = t.user_id " +
                "JOIN courses c ON tc.course_code = c.course_code " +
                "WHERE t.name = ?";
           try(Connection connection = DatabaseConnection.getConnection();
               PreparedStatement stmt = connection.prepareStatement(viewQuery)){

               stmt.setString(1,teacherName);
               try(ResultSet rs = stmt.executeQuery()){
                   while (rs.next()){
                       Map<String,String> record = new HashMap<>();
                       record.put("teacher_name",rs.getString("name"));
                       record.put("course_code",rs.getString("course_code"));
                       record.put("course_name",rs.getString("course_name"));
                       record.put("class_name", rs.getString("class_name")); // Add class_name here
                       courseList.add(record);
                   }
               }
               catch(SQLException e){
                   e.printStackTrace();
               }
               return courseList;
           }
    }
    public List<Map<String, String>> getCoursesTeachingById(int userId) throws SQLException {
        List<Map<String, String>> courseList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // SQL query to select course details and class name for a given user_id.
        // It joins 'teacher_courses' with 'courses' to get the course name.
        String viewQuery = "SELECT c.course_code, c.course_name, tc.class_name " +
                "FROM teacher_courses tc " +
                "JOIN courses c ON tc.course_code = c.course_code " +
                "WHERE tc.user_id = ?";

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(viewQuery);
            stmt.setInt(1, userId); // Set the user ID parameter

            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> record = new HashMap<>();
                record.put("course_code", rs.getString("course_code"));
                record.put("course_name", rs.getString("course_name"));
                record.put("class_name", rs.getString("class_name"));
                courseList.add(record);
            }
        } finally {
            // Ensure all JDBC resources are closed in the finally block
            try {
                if (rs != null) rs.close();
            } catch (SQLException ignored) {
                // Log or handle the exception if needed, but often ignored for closing
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {}
            try {
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
        return courseList;
    }
    //view profile
    public void viewTeacherProfile(int user_id){

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            connection = DatabaseConnection.getConnection();
            String viewQuery = "SELECT * FROM Teacher where user_id = ?";
            stmt = connection.prepareStatement(viewQuery);
            stmt.setInt(1,user_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String teacherName = rs.getString("name");
                System.out.println("User ID: " + userId);
                System.out.println("Teacher Name: " + teacherName);
            }

            else{
                System.out.println("Teacher Profile not found!");
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

    //webabsed


    public Map<String, Object> getTeacherProfileById(int userId) throws SQLException {
        Map<String, Object> profile = new HashMap<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // Use a placeholder (?) for the user_id to make the query dynamic
        String query = "SELECT t.name, u.email_id, td.phone, td.department, td.qualification, td.experience\n" +
                "FROM teacher t\n" +
                "LEFT JOIN teacher_details td ON t.user_id = td.user_id\n" +
                "LEFT JOIN user u ON t.user_id = u.user_id\n" +
                "WHERE t.user_id = ?";

        try {
            connection = DatabaseConnection.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId); // Correctly sets the dynamic user ID

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Corrected: Matching column names from the SELECT statement
                profile.put("teacherName", rs.getString("name"));
                profile.put("email", rs.getString("email_id"));
                profile.put("phone", rs.getString("phone"));
                profile.put("department", rs.getString("department"));
                profile.put("qualification", rs.getString("qualification"));
                profile.put("experience", rs.getInt("experience"));
                profile.put("teacherId", userId);
            }
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* Log or handle  }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* Log or handle  }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* Log or handle  }
            }
        }
        return profile;
    }


    //6. If incase teacher wants to update courses he will be teaching

    public boolean updateCourseTeaching(int userId, String oldCourseCode, String newCourseCode, String newCourseName, int newYear) {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();

            // 1. Update teacher_courses table (change course code)
            String updateTeacherCourses = "UPDATE teacher_courses SET course_code = ? WHERE user_id = ? AND course_code = ?";
            stmt = connection.prepareStatement(updateTeacherCourses);
            stmt.setString(1, newCourseCode);
            stmt.setInt(2, userId);
            stmt.setString(3, oldCourseCode);
            int rowsUpdatedTC = stmt.executeUpdate();

            // 2. Update courses table (change course name and year for new course code)
            String updateCourses = "UPDATE courses SET course_name = ?, year = ? WHERE course_code = ?";
            stmt = connection.prepareStatement(updateCourses);
            stmt.setString(1, newCourseName);
            stmt.setInt(2, newYear);
            stmt.setString(3, newCourseCode);
            int rowsUpdatedCourses = stmt.executeUpdate();

            if (rowsUpdatedTC > 0 && rowsUpdatedCourses > 0) {
                System.out.println("Course teaching details updated successfully.");
                return true;
            } else {
                System.out.println("Failed to update course teaching details.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //7. A particular teacher can delete courses he/she is teaching

public boolean delCoursesTeaching(String courseCode, int userId){
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

        int delRows = 0;
        try{
            connection = DatabaseConnection.getConnection();
            String delQuery = "DELETE FROM teacher_courses WHERE course_code = ?  AND user_id = ?";
            stmt = connection.prepareStatement(delQuery);
            stmt.setString(1,courseCode);
            stmt.setInt(2,userId);
            delRows = stmt.executeUpdate();

            if(delRows>0){
                System.out.println("Course deleted from your profile successfully!");
                return true;
            }else{
                System.out.println("Course not found / Course cannot be deleted !");
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try{
                if(stmt!=null)stmt.close();
                if(connection!=null)connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //8. Exporting Attendance

    public void exportAttendanceForTeacher(String courseCode , String courseName , String filePath){
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, String>> attendanceData = new ArrayList<>();
        try{
            connection = DatabaseConnection.getConnection();
            String query = "SELECT s.enrollment_id, s.stu_name, a.date, a.Status, a.marked_by " +
                    "FROM attendance a JOIN student s ON a.enrollment_id = s.enrollment_id " +
                    "WHERE a.course_code = ?";

            stmt = connection.prepareStatement(query);
            stmt.setString(1,courseCode);
            rs = stmt.executeQuery();

            while(rs.next()){
                Map<String,String> records = new HashMap<>();
                records.put("enrollemnt_id",rs.getString("enrollment_id"));
                records.put("stu_name",rs.getString("stu_name"));
                records.put("Status",rs.getString("Status"));
                records.put("marked_by",rs.getString("marked_by"));
                attendanceData.add(records);
                ExcelUtil.exportAttendanceToExcel(filePath,courseCode,courseName,attendanceData);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try {
                if (stmt != null) stmt.close();
                if (rs != null) rs.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean updateTeacherProfile(int userId, Map<String, String> profileData) throws SQLException {
        String sql = "INSERT INTO teacher_details (user_id, phone, department, qualification, experience) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "phone = VALUES(phone), " +
                "department = VALUES(department), " +
                "qualification = VALUES(qualification), " +
                "experience = VALUES(experience)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, profileData.get("phone"));
            stmt.setString(3, profileData.get("department"));
            stmt.setString(4, profileData.get("qualification"));

            // Safely parse the experience value, defaulting to 0 if invalid
            String experienceStr = profileData.get("experience");
            try {
                int experience = (experienceStr != null && !experienceStr.isEmpty()) ? Integer.parseInt(experienceStr) : 0;
                stmt.setInt(5, experience); // This line needs to be inside the try block
            } catch (NumberFormatException e) {
                stmt.setInt(5, 0); // This should be inside a try-catch block as you have it.
            }

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL error during teacher profile update: " + e.getMessage());
            throw e;
        }
    }
}*/



package com.attendease.services;

import com.attendease.utils.DatabaseConnection;
import com.attendease.utils.ExcelUtil;

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

public class TeachServices {

    // Removed stateful connection fields and the constructor.
    // Each method will now handle its own connection.

    // 1. To mark whole batch attendance
    public boolean markAttendance(String[] enrollmentId, String courseCode, String date, String status, String markedBy) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            String attendQuery = "INSERT INTO attendance (enrollment_id, course_code, date, Status, marked_by) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(attendQuery);

            for (String s : enrollmentId) {
                stmt.setString(1, s);
                stmt.setString(2, courseCode);
                stmt.setString(3, date);
                stmt.setString(4, status);
                stmt.setString(5, markedBy);
                stmt.addBatch();
            }

            int[] batchResults = stmt.executeBatch();
            int insertRows = 0;
            for (int result : batchResults) {
                if (result > 0) {
                    insertRows++;
                }
            }

            if (insertRows == enrollmentId.length) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("SQL ERROR: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // 2. Retrieving enrollment id of a particular batch
    public String[] enrollmentIdFetching(String className) throws SQLException {
        List<String> enrollmentIds = new ArrayList<>();
        String query = "SELECT enrollment_id FROM Student WHERE class_name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, className);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollmentIds.add(rs.getString("enrollment_id"));
                }
            }
        }
        return enrollmentIds.toArray(new String[0]);
    }


    // 3. Web-based: Fetch students by class
    public List<Map<String, String>> getStudentsByClassYearSem(String className, int year, int semster) {
        List<Map<String, String>> students = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT enrollment_id, stu_name, class_name FROM Student WHERE class_name = ? AND year = ? AND semster = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, className);
                stmt.setInt(2, year);
                stmt.setInt(3, semster);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> student = new HashMap<>();
                        student.put("enrollmentId", rs.getString("enrollment_id"));
                        student.put("studentName", rs.getString("stu_name"));
                        student.put("className", rs.getString("class_name"));
                        students.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // 4. Console-based: View attendance
    public void viewAttendance(String course_code, String className) {
        String query = "SELECT s.name AS stu_name, a.course_code, a.date, a.status " +
                "FROM attendance a JOIN Student s ON a.enrollment_id = s.enrollment_id " +
                "WHERE a.course_code = ? AND s.class_name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, course_code);
            stmt.setString(2, className);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                System.out.println("Student Name | Course Code | Date | Status");
                System.out.println("------------------------------------------------");
                while (rs.next()) {
                    found = true;
                    String stuName = rs.getString("stu_name");
                    String cc = rs.getString("course_code");
                    String date = rs.getString("date");
                    String status = rs.getString("status");
                    System.out.println(stuName + " | " + cc + " | " + date + " | " + status);
                }
                if (!found) {
                    System.out.println("No attendance record found for the given course code and class.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL ERROR: " + e.getMessage());
        }
    }


    // 5. Add courses a teacher is teaching
    public boolean addCoursesTeaching(int userId, String courseCode, String className) {
        String addQuery = "INSERT INTO teacher_courses (user_id, course_code, class_name) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(addQuery)) {
            stmt.setInt(1, userId);
            stmt.setString(2, courseCode);
            stmt.setString(3, className);
            int insertRows = stmt.executeUpdate();
            return insertRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 6. Web-based: Get courses a teacher is teaching
    public List<Map<String, String>> getCoursesTeaching(String teacherName) throws SQLException {
        List<Map<String, String>> courseList = new ArrayList<>();
        String viewQuery = "SELECT t.name, c.course_code, c.course_name, tc.class_name " +
                "FROM teacher_courses tc JOIN users t ON tc.user_id = t.user_id " +
                "JOIN courses c ON tc.course_code = c.course_code WHERE t.name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(viewQuery)) {
            stmt.setString(1, teacherName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> record = new HashMap<>();
                    record.put("teacher_name", rs.getString("name"));
                    record.put("course_code", rs.getString("course_code"));
                    record.put("course_name", rs.getString("course_name"));
                    record.put("class_name", rs.getString("class_name"));
                    courseList.add(record);
                }
            }
        }
        return courseList;
    }


    // 7. Web-based: Get courses by teacher ID
    public List<Map<String, String>> getCoursesTeachingById(int userId) throws SQLException {
        List<Map<String, String>> courseList = new ArrayList<>();
        String viewQuery = "SELECT c.course_code, c.course_name, tc.class_name " +
                "FROM teacher_courses tc JOIN courses c ON tc.course_code = c.course_code " +
                "WHERE tc.user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(viewQuery)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> record = new HashMap<>();
                    record.put("course_code", rs.getString("course_code"));
                    record.put("course_name", rs.getString("course_name"));
                    record.put("class_name", rs.getString("class_name"));
                    courseList.add(record);
                }
            }
        }
        return courseList;
    }


    // 8. Web-based: Get teacher profile by ID
    public Map<String, Object> getTeacherProfileById(int userId) throws SQLException {
        Map<String, Object> profile = new HashMap<>();
        String query = "SELECT t.name, u.email_id, td.phone, td.department, td.qualification, td.experience " +
                "FROM teacher t LEFT JOIN teacher_details td ON t.user_id = td.user_id " +
                "LEFT JOIN user u ON t.user_id = u.user_id WHERE t.user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    profile.put("teacherName", rs.getString("name"));
                    profile.put("email", rs.getString("email_id"));
                    profile.put("phone", rs.getString("phone"));
                    profile.put("department", rs.getString("department"));
                    profile.put("qualification", rs.getString("qualification"));
                    profile.put("experience", rs.getInt("experience"));
                    profile.put("teacherId", userId);
                }
            }
        }
        return profile;
    }


    // 9. Update teacher profile
    public boolean updateTeacherProfile(int userId, Map<String, Object> profileData) throws SQLException {
        String sql = "INSERT INTO teacher_details (user_id, phone, department, qualification, experience) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "phone = VALUES(phone), " +
                "department = VALUES(department), " +
                "qualification = VALUES(qualification), " +
                "experience = VALUES(experience)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Check if connection is null
            if (connection == null) {
                System.err.println("Database connection is null. Check connection pool configuration.");
                return false;
            }

            stmt.setInt(1, userId);
            stmt.setString(2, (String) profileData.get("phone"));
            stmt.setString(3, (String) profileData.get("department"));
            stmt.setString(4, (String) profileData.get("qualification"));

            // Safely handle the experience field, which might be a Double or Integer
            Object experienceObj = profileData.get("experience");
            int experience = 0; // Default value
            if (experienceObj instanceof Double) {
                experience = ((Double) experienceObj).intValue();
            } else if (experienceObj instanceof String) {
                try {
                    experience = Integer.parseInt((String) experienceObj);
                } catch (NumberFormatException e) {
                    // Keep the default value of 0
                }
            }

            stmt.setInt(5, experience);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL error during teacher profile update: " + e.getMessage());
            throw e;
        }
    }


    // 10. Delete courses a teacher is teaching
    public boolean delCoursesTeaching(String courseCode, int userId) throws SQLException {
        String delQuery = "DELETE FROM teacher_courses WHERE course_code = ? AND user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(delQuery)) {
            stmt.setString(1, courseCode);
            stmt.setInt(2, userId);
            int delRows = stmt.executeUpdate();
            return delRows > 0;
        }
    }


    // 11. Exporting Attendance
    public void exportAttendanceForTeacher(OutputStream outputStream, String courseCode, String courseName) throws SQLException, IOException {
        String query = "SELECT s.enrollment_id, s.name AS stu_name, a.date, a.status, a.marked_by " +
                "FROM attendance a JOIN Student s ON a.enrollment_id = s.enrollment_id " +
                "WHERE a.course_code = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseCode);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, String>> attendanceData = new ArrayList<>();
                while (rs.next()) {
                    Map<String, String> records = new HashMap<>();
                    records.put("enrollment_id", rs.getString("enrollment_id"));
                    records.put("stu_name", rs.getString("stu_name"));
                    records.put("date", rs.getString("date")); // Added back the date key
                    records.put("status", rs.getString("status"));
                    records.put("marked_by", rs.getString("marked_by"));
                    attendanceData.add(records);
                }
                ExcelUtil.exportAttendanceToExcel(outputStream, courseCode, courseName, attendanceData);
            }
        }
    }

    // New helper method to fix viewCoursesTeaching and similar methods with incorrect queries
    public void viewCoursesTeaching(String teachName, String courseCode, String courseName) {
        // This method was non-functional and console-based. It's best to remove it or replace it with a functional web-based method.
    }
}



