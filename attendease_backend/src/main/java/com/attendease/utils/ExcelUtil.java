package com.attendease.utils;

import com.attendease.modals.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                                          List<Map<String, Object>> attendanceData,
                                                          String startDate,
                                                          String endDate) throws IOException {
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

                // --- Course and Teacher Info Header ---
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

                // --- Date Range Header ---
                Row row3 = sheet.createRow(3);
                Cell cell3 = row3.createCell(0);
                cell3.setCellValue("Date Range: " + startDate + " to " + endDate);
                cell3.setCellStyle(headerStyle);

                // --- Table Header ---
                String[] headers = {"Enrollment Number", "Name", "CH", "CA", "Percentage"};
                Row headerRow = sheet.createRow(5); // Start on row 5 to leave space for the date range
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                // --- Data Rows ---
                int rowNum = 6;
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

                // Auto-size columns for better readability
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(outputStream);
                System.out.println("Attendance summary exported successfully.");

            } finally {
                // The try-with-resources block for `XSSFWorkbook` automatically handles closing the workbook.
            }
    }

    /**
     * Creates an Excel file with a student's attendance report.
     * This method is useful for generating a file to be sent to a web response.
     *
     * @param studentData A Map containing the student's personal information.
     * @return A ByteArrayOutputStream containing the Excel file data.
     * @throws IOException If an I/O error occurs while writing the workbook.
     */
    public static ByteArrayOutputStream createDateWiseAttendanceReport(Map<String, Object> studentData, List<Map<String, String>> attendanceData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Date-Wise Attendance");
        int rowNum = 0;

        // --- Student Info Header ---
        sheet.createRow(rowNum++).createCell(0).setCellValue("Student Date-Wise Attendance Report");
        sheet.createRow(rowNum++).createCell(0).setCellValue("Name: " + studentData.get("studentName"));
        sheet.createRow(rowNum++).createCell(0).setCellValue("Enrollment ID: " + studentData.get("enrollmentId"));
        sheet.createRow(rowNum++).createCell(0).setCellValue("Class: " + studentData.get("className"));
        sheet.createRow(rowNum++).createCell(0).setCellValue("Year: " + studentData.get("year"));
        rowNum++; // Blank row for spacing

        // --- Table Header ---
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Course Code");
        headerRow.createCell(2).setCellValue("Status");
        headerRow.createCell(3).setCellValue("Marked By");

        // --- Data Rows ---
        for (Map<String, String> record : attendanceData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(record.get("date"));
            row.createCell(1).setCellValue(record.get("course_code"));
            row.createCell(2).setCellValue(record.get("status"));
            row.createCell(3).setCellValue(record.get("marked_by"));
        }

        // --- Auto-size columns ---
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }

        return outputStream;
    }

    /**
     * Exports a detailed and summary attendance report to a single Excel file.
     * This method combines date-wise attendance with a per-student summary.
     *
     * @param outputStream The stream to write the Excel data to.
     * @param courseCode The code of the course.
     * @param courseName The name of the course.
     * @param teacherName The name of the teacher.
     * @param dateWiseAttendance A map of dates to a list of attendance records for that date.
     * @param studentAttendanceSummary A list of maps, where each map contains a student's summary data.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public static void exportCombinedAttendanceToExcel(
            OutputStream outputStream,
            String courseCode,
            String courseName,
            String teacherName,
            Map<String, List<Map<String, String>>> dateWiseAttendance,
            List<Map<String, Object>> studentAttendanceSummary
    ) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance Report");
            int rowNum = 0;

            // --- 1. Report Header Section ---
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            Row header1 = sheet.createRow(rowNum++);
            header1.createCell(0).setCellValue("Course Code: " + courseCode);
            header1.getCell(0).setCellStyle(boldStyle);

            Row header2 = sheet.createRow(rowNum++);
            header2.createCell(0).setCellValue("Course Name: " + courseName);
            header2.getCell(0).setCellStyle(boldStyle);

            Row header3 = sheet.createRow(rowNum++);
            header3.createCell(0).setCellValue("Teacher: " + teacherName);
            header3.getCell(0).setCellStyle(boldStyle);

            rowNum += 2; // Add blank rows for spacing

            // --- 2. Date-wise Attendance Table ---
            Row datewiseTitle = sheet.createRow(rowNum++);
            datewiseTitle.createCell(0).setCellValue("Date-Wise Attendance Details");
            datewiseTitle.getCell(0).setCellStyle(boldStyle);

            Row datewiseHeader = sheet.createRow(rowNum++);
            datewiseHeader.createCell(0).setCellValue("Enrollment Number");
            datewiseHeader.createCell(1).setCellValue("Student Name");

            int colNum = 2;
            List<String> dates = new ArrayList<>(dateWiseAttendance.keySet());
            for (String date : dates) {
                datewiseHeader.createCell(colNum++).setCellValue(date);
            }

            // Collect all unique student records to ensure all students are included
            Map<String, String> studentNames = new HashMap<>();
            for (List<Map<String, String>> records : dateWiseAttendance.values()) {
                for (Map<String, String> record : records) {
                    studentNames.put(record.get("enrollment_id"), record.get("stu_name"));
                }
            }

            // Fill in the data for the date-wise table
            for (String enrollmentId : studentNames.keySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(enrollmentId);
                row.createCell(1).setCellValue(studentNames.get(enrollmentId));

                int dataColNum = 2;
                for (String date : dates) {
                    String status = "N/A";
                    // Find the attendance status for the current student and date
                    List<Map<String, String>> dailyRecords = dateWiseAttendance.get(date);
                    if (dailyRecords != null) {
                        for (Map<String, String> record : dailyRecords) {
                            if (record.get("enrollment_id").equals(enrollmentId)) {
                                status = record.get("status");
                                break;
                            }
                        }
                    }
                    row.createCell(dataColNum++).setCellValue(status);
                }
            }

            rowNum += 2; // Add blank rows for spacing

            // --- 3. Attendance Summary Table ---
            Row summaryTitle = sheet.createRow(rowNum++);
            summaryTitle.createCell(0).setCellValue("Attendance Summary");
            summaryTitle.getCell(0).setCellStyle(boldStyle);

            String[] headers = {"Enrollment Number", "Name", "CH", "CA", "Percentage"};
            Row summaryHeader = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = summaryHeader.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(boldStyle);
            }

            // Fill in the data for the summary table
            for (Map<String, Object> record : studentAttendanceSummary) {
                Row row = sheet.createRow(rowNum++);
                int summaryColNum = 0;

                row.createCell(summaryColNum++).setCellValue((String) record.get("enrollment_id"));
                row.createCell(summaryColNum++).setCellValue((String) record.get("stu_name"));

                int totalLectures = (int) record.get("total_lectures");
                int attendedLectures = (int) record.get("attended_lectures");

                row.createCell(summaryColNum++).setCellValue(totalLectures);
                row.createCell(summaryColNum++).setCellValue(attendedLectures);

                double percentage = (totalLectures > 0) ? (double) attendedLectures / totalLectures * 100 : 0;
                double roundedPercentage = Math.round(percentage * 100.0) / 100.0;
                row.createCell(summaryColNum).setCellValue(roundedPercentage);
            }

            // Auto-size all columns to fit the content
            for (int i = 0; i < datewiseHeader.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to the provided OutputStream
            workbook.write(outputStream);
            System.out.println("Combined attendance report exported successfully.");
        }
    }


    /**
     * Exports a single student's attendance report, including date-wise details and a summary.
     * This method is specifically for student-facing reports.
     *
     * @param outputStream The stream to write the Excel data to.
     * @param studentName The name of the student.
     * @param enrollmentId The enrollment ID of the student.
     * @param className The name of the student's class.
     * @param dateWiseData The list of date-wise attendance records for the student.
     * @param summaryData A map containing the student's summary data (total and attended lectures).
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public static void exportStudentAttendanceReport(
            OutputStream outputStream,
            String studentName,
            String enrollmentId,
            String className,
            List<Map<String, String>> dateWiseData,
            Map<String, Object> summaryData
    ) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Attendance Report");
            int rowNum = 0;

            // --- 1. Report Header Section ---
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            Row header1 = sheet.createRow(rowNum++);
            header1.createCell(0).setCellValue("Student Name: " + studentName);
            header1.getCell(0).setCellStyle(boldStyle);

            Row header2 = sheet.createRow(rowNum++);
            header2.createCell(0).setCellValue("Enrollment ID: " + enrollmentId);
            header2.getCell(0).setCellStyle(boldStyle);

            Row header3 = sheet.createRow(rowNum++);
            header3.createCell(0).setCellValue("Class: " + className);
            header3.getCell(0).setCellStyle(boldStyle);

            rowNum += 2; // Add blank rows for spacing

            // --- 2. Date-Wise Attendance Table ---
            Row datewiseTitle = sheet.createRow(rowNum++);
            datewiseTitle.createCell(0).setCellValue("Date-Wise Attendance Details");
            datewiseTitle.getCell(0).setCellStyle(boldStyle);

            String[] datewiseHeaders = {"Date", "Status", "Marked By"};
            Row datewiseHeaderRow = sheet.createRow(rowNum++);
            for (int i = 0; i < datewiseHeaders.length; i++) {
                Cell cell = datewiseHeaderRow.createCell(i);
                cell.setCellValue(datewiseHeaders[i]);
                cell.setCellStyle(boldStyle);
            }

            // Fill in the data for the date-wise table
            for (Map<String, String> record : dateWiseData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.get("date"));
                row.createCell(1).setCellValue(record.get("status"));
                row.createCell(2).setCellValue(record.get("marked_by"));
            }

            rowNum += 2; // Add blank rows for spacing

            // --- 3. Attendance Summary Section ---
            Row summaryTitle = sheet.createRow(rowNum++);
            summaryTitle.createCell(0).setCellValue("Attendance Summary");
            summaryTitle.getCell(0).setCellStyle(boldStyle);

            int totalLectures = (int) summaryData.get("total_lectures");
            int attendedLectures = (int) summaryData.get("attended_lectures");
            double percentage = (totalLectures > 0) ? (double) attendedLectures / totalLectures * 100 : 0;
            double roundedPercentage = Math.round(percentage * 100.0) / 100.0;

            sheet.createRow(rowNum++).createCell(0).setCellValue("Lectures Held: " + totalLectures);
            sheet.createRow(rowNum++).createCell(0).setCellValue("Lectures Attended: " + attendedLectures);
            sheet.createRow(rowNum++).createCell(0).setCellValue("Attendance Percentage: " + roundedPercentage + "%");

            // Auto-size columns to fit content
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to the provided OutputStream
            workbook.write(outputStream);
            System.out.println("Student attendance report exported successfully.");
        }
    }
}