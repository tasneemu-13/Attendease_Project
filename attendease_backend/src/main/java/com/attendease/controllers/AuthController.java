package com.attendease.controllers;

import com.attendease.services.AuthService;

import java.util.Scanner;

public class AuthController {

    private final AuthService authServices;

    boolean result = false;

    public AuthController(){
        this.authServices = new AuthService();
    }

    Scanner sc = new Scanner(System.in);
    public void register(){

        System.out.println("Enter your email:");
        String mail = sc.next();
        System.out.println("Enter your password: ");
        String pass = sc.next();
        System.out.println("Enter your role as Student || Teacher :");
        String role = sc.next();

        if(role.equalsIgnoreCase("teacher")){
            System.out.println("Enter Teacher Access Code :");
            String TAC = sc.next();
            System.out.println("Enter Your Name :");
            String teachName = sc.next();
            result = authServices.registerUser(mail,pass,TAC,role,null,null,teachName,0,null,null,null);
        }
        else if(role.equalsIgnoreCase("Student")){

            System.out.println("Enter Enrollment Number :");
            String enrollmentId = sc.next();
            System.out.println("Enter Your Name :");
            String stuName = sc.next();
            System.out.println("Enter Your Class  :");
            String className = sc.next();
            System.out.println("Enter your Year of Study :");
            Integer year = sc.nextInt();
            System.out.println("Enter your  Program of Study :");
            String program = sc.nextLine();
            System.out.println("Enter your Branch of Study :");
            String branch = sc.nextLine();
            System.out.println("Enter your Semester of Study :");
            Integer semster = sc.nextInt();

            result =  authServices.registerUser(mail,pass,null,role,enrollmentId,stuName,className,year,program,branch,semster);

        }
        else{
            System.out.println("Invalid entry");
        }
        if(result){
            System.out.println("Registeration Done!");
        }
        else{
            System.out.println("Registeration Failed!");
        }

    }

    public void login(){
        System.out.println("Enter your email id :");
        String mail = sc.next();
        System.out.println("Enter your password :");
        String password = sc.next();

        String role = authServices.login(mail,password);
        if(!role.equalsIgnoreCase("Invalid")){
            System.out.println("Login Successfull as "+role);
        }
        else {
            System.out.println("Login Failed !  Please first register succesfully");
        }

    }


}

