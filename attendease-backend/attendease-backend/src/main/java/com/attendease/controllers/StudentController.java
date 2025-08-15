package com.attendease.controllers;

import com.attendease.models.Student;
import com.attendease.services.StuService;
import com.attendease.utils.ExcelUtil;

import java.util.Scanner;

public class StudentController {

    private StuService stuServices ;
    Scanner sc = new Scanner(System.in);

    private Student student;
    public StudentController(){
        this.stuServices = new StuService();
    }

   public void studentMenu(){
       int choice;
       String enrollmentId;

        while(true){
            System.out.println("\n========= Student Panel =========");
            System.out.println("1.View Your Attendance\n 2.View Your Profile\n 3.Upate Your Profile\n 4.Exit");
            System.out.println("Enter your choice :");
            choice = sc.nextInt();
            sc.nextLine();
            switch(choice){

                case 1:
                    System.out.println("Enter your enrollment number : ");
                    enrollmentId = student.getEnrollmentId();
                    stuServices.viewAttendance(enrollmentId);
                    break;

                case 2:
                    System.out.println("Enter your enrollment number : ");
                    enrollmentId = sc.nextLine();
                    stuServices.viewProfile(enrollmentId);
                    break;

                case 3:
                    System.out.println("Enter your enrollment number : ");
                    enrollmentId = sc.nextLine();
                    System.out.println("Enter your course code :");
                    String cc = sc.nextLine();
                    System.out.println("Enter your course name : ");
                    String courseName= sc.nextLine();
                    System.out.println("Enter your class name : ");
                    String className= sc.nextLine();
                    System.out.println("Enter your year of study : ");
                    int year = sc.nextInt();
                    stuServices.updateProfile(enrollmentId,cc,courseName,className,year);
                    break;

                case 4:
                    System.out.println("Exiting the menu .....");
                    return;

                default:
                    System.out.println("Invalid Choice!");
                    break;
            }
        }
   }

}
