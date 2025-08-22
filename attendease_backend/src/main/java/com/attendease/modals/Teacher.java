package com.attendease.modals;

public class Teacher {
    private String teacherAccessCode;

   //userId can be extracted from User class

    private String teacherName;

    private User user; //object user class

    //default constructor
    public Teacher(){

    }

    //parametarized constructor
    public Teacher(String teacherAccessCode , String teacherName, User user1){
        this.teacherAccessCode = teacherAccessCode;
        this.teacherName = teacherName;
        this.user = user1;
    }

    //getters
    public String getTeacherAccessCode(){
        return teacherAccessCode;
    }

    public String getTeacherName(){
        return teacherName;
    }

    public Integer getUserId(){
        return user.getUserId(); // User class called
    }
    //setters
    public void setTeacherAccessCode(String TAC){
        this.teacherAccessCode = TAC;
    }

    public void setTeacherName(String Name){
        this.teacherName = Name;
    }

}
