// File: src/main/java/com/attendease/util/ExcelUtil.java
package com.attendease.utils;

import com.attendease.models.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Utility class for creating and exporting Excel files for attendance.
 * This class demonstrates two different methods for handling Excel data:
 * 1. Writing to a file on the server.
 * 2. Writing directly to an OutputStream for a web response.
 */
public class ExcelUtil {

    /**
     * Creates an attendance template Excel file and writes it to a specified file path.
     * This method is useful for generating a file on the server.
     *
     * @param filePath The full path where the file will be saved.
     * @param teacherName Name of the teacher.
     * @param courseCode Code of the course.
     * @param courseName Name of the course.
     * @param className The name of the class.
     * @param year The academic year.
     * @param date The date for the attendance.
     * @param students A list of Student objects to include in the template.
     */
    public static void createAttendanceTemplate(String filePath , String teacherName , String courseCode, String courseName ,
                                                String className , int year , String date , List<Student>students){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance");
        int rowNum = 0;

        //Meta Data
        sheet.createRow(rowNum++).createCell(0).setCellValue("Teacher Name: "+teacherName);
        sheet.createRow(rowNum++).createCell(0).setCellValue("Course Code: "+courseCode);
        sheet.createRow(rowNum++).createCell(0).setCellValue("Course Name: "+courseName);
        sheet.createRow(rowNum++).createCell(0).setCellValue("Class:" +className);
        sheet.createRow(rowNum++).createCell(0).setCellValue("Year: "+year);
        rowNum++; // Blank Row

        //Header Row
        Row headerRow  = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("EnrollmentId");
        headerRow.createCell(1).setCellValue("Student Name");
        headerRow.createCell(2).setCellValue("Status(P/A)");

        // Student Data Rows
        for(Student student : students){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getEnrollmentId());
            row.createCell(1).setCellValue(student.getStudentName());
            row.createCell(2).setCellValue(""); //Filled by teacher
        }

        //Auto size column
        for(int i =0; i<3; i++){
            sheet.autoSizeColumn(i);
        }

        try(FileOutputStream fileOut = new FileOutputStream(filePath)){
            workbook.write(fileOut);
            System.out.println("Attendance Template Created Successfully At"+filePath);
        } catch (IOException e) {
            System.out.println("Error while Writing into an Excel sheet");
            throw new RuntimeException(e);
        }finally{
            try{
                workbook.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * Exports attendance data to an Excel file by writing directly to an OutputStream.
     * This is the method used by the servlet to send the file to the browser.
     *
     * @param outputStream The stream to write the Excel data to.
     * @param courseCode The code of the course.
     * @param courseName The name of the course.
     * @param attendanceData The list of attendance records to export.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public static void exportAttendanceToExcel(OutputStream outputStream, String courseCode, String courseName,
                                               List<Map<String, String>> attendanceData) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance Export");
        int rowNum = 0;

        // Title Row
        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("Exported Attendance Record ");
        rowNum++;

        sheet.createRow(rowNum++).createCell(0).setCellValue("Course Code: " + courseCode);
        sheet.createRow(rowNum++).createCell(0).setCellValue("Course Name: " + courseName);

        // Table Header
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Enrollment Id");
        header.createCell(1).setCellValue("Student Name");
        header.createCell(2).setCellValue("Date");
        header.createCell(3).setCellValue("Status");
        header.createCell(4).setCellValue("Marked By");

        // Populate the sheet with data from the list
        for (Map<String, String> records : attendanceData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(records.get("enrollment_id"));
            row.createCell(1).setCellValue(records.get("stu_name"));
            row.createCell(2).setCellValue(records.get("date"));
            // The original commented-out code had `records.get("Status")`, but the correct key is lowercase "status".
            row.createCell(3).setCellValue(records.get("status"));
            row.createCell(4).setCellValue(records.get("marked_by"));
        }

        // Auto size columns to fit the content
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            // Write the workbook to the provided OutputStream
            workbook.write(outputStream);
            System.out.println("Attendance exported successfully.");
        } finally {
            // Close the workbook in a finally block to ensure it's always closed
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Exports a summary of attendance data to an Excel file, including total lectures,
     * attended lectures, and a percentage for each student.
     *
     * @param outputStream The stream to write the Excel data to.
     * @param courseCode The code of the course.
     * @param courseName The name of the course.
     * @param teacherName The name of the teacher.
     * @param attendanceData A list of maps, where each map contains a student's summary data.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public static void exportAttendanceSummaryToExcel(OutputStream outputStream,
                                                      String courseCode,
                                                      String courseName,
                                                      String teacherName,
                                                      List<Map<String, Object>> attendanceData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance Summary");

            // Create styles for headers and content
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            // --- Course and Teacher Info Header (Merged Cells) ---
            Row row0 = sheet.createRow(0);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("Course Code: " + courseCode);
            cell0.setCellStyle(headerStyle);

            Row row1 = sheet.createRow(1);
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue("Course Name: " + courseName);
            cell1.setCellStyle(headerStyle);

            Row row2 = sheet.createRow(2);
            Cell cell2 = row2.createCell(0);
            cell2.setCellValue("Teacher Name: " + teacherName);
            cell2.setCellStyle(headerStyle);

            // --- Table Header ---
            String[] headers = {"Enrollment Number", "Name", "CH", "CA", "Percentage"};
            Row headerRow = sheet.createRow(4);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- Data Rows ---
            int rowNum = 5;
            for (Map<String, Object> record : attendanceData) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;

                row.createCell(colNum++).setCellValue((String) record.get("enrollment_id"));
                row.createCell(colNum++).setCellValue((String) record.get("stu_name"));

                int totalLectures = (int) record.get("total_lectures");
                int attendedLectures = (int) record.get("attended_lectures");

                row.createCell(colNum++).setCellValue(totalLectures);
                row.createCell(colNum++).setCellValue(attendedLectures);

                // Calculate and set the percentage
                double percentage = (totalLectures > 0) ? (double) attendedLectures / totalLectures * 100 : 0;
                // Round to 2 decimal places
                double roundedPercentage = Math.round(percentage * 100.0) / 100.0;
                row.createCell(colNum).setCellValue(roundedPercentage);
            }

            // Corrected: Move auto-sizing outside the data loop.
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Corrected: Move the workbook write operation outside the data loop.
            workbook.write(outputStream);
            System.out.println("Attendance summary exported successfully.");

        } finally {
            // The try-with-resources block for `XSSFWorkbook` automatically handles closing the workbook.
        }
    }
}