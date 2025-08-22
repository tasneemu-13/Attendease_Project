package com.attendease.controllers;/*package com.attendease.controllers;

import com.attendease.services.TeachServices;

import java.util.Scanner;

public class TeacherController {

    private TeachServices teachServices;
     Scanner sc = new Scanner(System.in);
    public TeacherController(){
        this.teachServices = new TeachServices();
    }

    public void teacherMenu(){
        String className;
        String courseCode;
        String courseName;
        int year;
        int userId;
        String Status;
        String markedBy;
        String date;

        while (true) {
            System.out.println("\n========= Teacher Panel =========");
            System.out.println("1. Add Courses");
            System.out.println("2. Mark Attendance");
            System.out.println("3. View Student's Attendance Record");
            System.out.println("4. View Courses");
            System.out.println("5. Delete Courses");
            System.out.println("6. View Your Teacher Profile");
            System.out.println("7. Update Your Teacher Profile");
            System.out.println("8. Export Attendance to Excel");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    System.out.println("Enter Your User Id Professor: ");
                    userId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Enter Number of courses you would be teaching: ");
                    int n = sc.nextInt();

                    System.out.println("Enter class Name :");
                    className = sc.nextLine();
                    sc.nextLine();
                    for(int i = 0; i < n; i++) {
                        System.out.println("Enter Course Code:");
                        courseCode = sc.nextLine();
                        teachServices.addCoursesTeaching(userId, courseCode,className);

                    }
                    break;

                case 2:
                    System.out.println("Enter the class name for which you want to mark attendance: ");
                    className = sc.nextLine();
                    String[] ids = teachServices.enrollmentIdFetching(className);

                    System.out.println("Enter course code of course you are teaching : ");
                    courseCode = sc.nextLine();

                    System.out.println("Enter today's date: ");
                    date = sc.nextLine();

                    System.out.println("Mark attendance 'P' as present as 'A' as absent : ");
                    Status = sc.nextLine();

                    System.out.println("Enter Your full name professor : ");
                    markedBy = sc.nextLine();

                    teachServices.markAttendance(ids,className,courseCode,date,Status,markedBy);
                    break;

                case 3:
                    System.out.println("Enter the class name for which you want to see attendance: ");
                    className = sc.nextLine();

                    System.out.println("Enter the course code for which you want to see attendance: ");
                    courseCode = sc.nextLine();

                    teachServices.viewAttendance(courseCode,className);
                    break;

                case 4:
                    System.out.println("Enter Your Name Professor: ");
                    String teachName = sc.nextLine();

                    System.out.println("Enter the course code you want to see: ");
                    courseCode = sc.nextLine();

                    System.out.println("Enter the Course name you want to see : ");
                    courseName = sc.nextLine();

                    teachServices.viewCoursesTeaching(teachName,courseCode,courseName);
                    break;

                case 5:
                    System.out.println("Enter course code you want to delete :");
                    courseCode = sc.nextLine();

                    System.out.println("Enter Your User id: ");
                    userId = sc.nextInt();
                    sc.nextLine();

                    teachServices.delCoursesTeaching(courseCode,userId);

                    break;

                case 6:
                    System.out.println("Enter Your User Id Professor: ");
                    userId = sc.nextInt();
                    sc.nextLine();
                    teachServices.viewTeacherProfile(userId);
                    break;

                case 7:
                    System.out.println("Enter user id :");
                    userId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Enter old course code u want to update: ");
                    courseCode= sc.nextLine();

                    System.out.println("Enter new course code to update: ");
                    String nCode = sc.nextLine();

                    System.out.println("Enter year to be updated:");
                    year = sc.nextInt();

                    System.out.println("Enter old course name:");
                    courseName = sc.nextLine();
                    teachServices.updateCourseTeaching(userId,courseCode,nCode,courseName,year);
                    break;

                case 8:

                    System.out.println("Enter course code : ");
                    courseCode = sc.nextLine();

                    System.out.println("Enter course name : ");
                    courseName = sc.nextLine();

                    System.out.println("Enter file path :");
                    String filePath = sc.nextLine();

                    teachServices.exportAttendanceForTeacher(courseCode,courseName,filePath);

                    break;

                case 9:
                    System.out.println("Exiting ...");
                    return;
            }
        }

   }
}*/
