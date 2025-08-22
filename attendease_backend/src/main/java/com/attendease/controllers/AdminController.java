package com.attendease.controllers;

import com.attendease.services.AdminServices;

import java.sql.Connection;
import java.util.Scanner;

public class AdminController {

    private  AdminServices adminServices;


    public AdminController(Connection connection) {
        this.adminServices = new AdminServices(); // inject DB connection
    }

    public void adminMenu(){
        String code;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n========= Admin Panel =========");
            System.out.println("1. Add Courses");
            System.out.println("2. Delete Courses");
            System.out.println("3. View Courses");
            System.out.println("4. View Students Data");
            System.out.println("5. View Teachers Data");
            System.out.println("6. View Users");
            System.out.println("7. Delete Student");
            System.out.println("8. Delete Teacher");
            System.out.println("9. Export Attendance to Excel");
            System.out.println("10. Display Attendance Report");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                     System.out.print("Enter Course Code: ");
                     code = sc.nextLine();
                     System.out.print("Enter Course Name: ");
                     String name = sc.nextLine();
                     System.out.println("Enter your current year of study :");
                     int year = sc.nextInt();
                     System.out.println(adminServices.addCourse(code, name,year));
                     break;
                case 2 :
                    System.out.print("Enter Course Code to delete: ");
                    code = sc.nextLine();
                    System.out.println(adminServices.deleteCourse(code));
                    break;
                case 3:
                    adminServices.viewAllCourses();
                    break;

                case 4:
                    adminServices. viewAllStudents();
                    break;

                case 5:
                    adminServices.viewAllTeachers();
                    break;

                case 6:
                    adminServices.viewAllUsers();
                    break;

                case 7:
                    System.out.print("Enter enrollment Id of Student to delete: ");
                    String enrollmentId = sc.nextLine();
                    System.out.println(adminServices.deleteStudent(enrollmentId));
                    break;

                case 8:
                    System.out.print("Enter user Id of Teacher to delete: ");
                    int userId = sc.nextInt();
                    System.out.println(adminServices.deleteTeacher(userId));
                    break;

                case 9:
                    System.out.print("Enter course code: ");
                    String courseCode = sc.nextLine();

                    System.out.print("Enter course name: ");
                    String courseName = sc.nextLine();

                    System.out.print("Enter file to upload :");
                    String filePath = sc.nextLine();
                    break;

                case 10 :
                    System.out.println("Enter your enrollment number :");
                    String eId = sc.nextLine();
                    adminServices.printReport(eId);
                    break;

                case 11:
                    System.out.println("Exiting Admin Panel...");
                    return;

                default:
                    System.out.println("Invalid Choice!");
             }

       }
    }
}
