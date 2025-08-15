package com.attendease.models;

public class Attendance {

    private Integer attendanceId;

    private String courseCode;

    private String date;

    private String markedBy;

    private Student student;

    //default constructor
    public Attendance(){}

    //parameterized constructor
    public Attendance(Integer attendanceId, String courseCode, String date , String markedBy , Student student1){
        this.attendanceId = attendanceId;
        this.courseCode = courseCode;
        this.date = date;
        this.markedBy = markedBy;
        this.student = student1;
    };
    //getters
    public Integer getAttendanceId(){
        return attendanceId;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public String getDate(){
        return date;
    }

    public String getMarkedBy(){
        return markedBy;
    }

    public Student getStudent(){
        return student;
    }

    //setters

    public void setAttendanceId(Integer attendanceId){
        this.attendanceId = attendanceId;
    }

    public void setCourseCode(String cc){
        this.courseCode = cc;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setMarkedBy(String name){
        this.markedBy = name;
    }

    public void setStudent(Student student1){
        this.student = student1;
    }

}
