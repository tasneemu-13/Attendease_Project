package com.attendease.models;

public class Student {
    private String enrollmentId;
    private String userId;
    private String studentName;
    private String className;
    private Integer year;
    private Integer semster; // New field for semester

    public Student(){}

    public Student(String enrollmentId, String userId, String studentName, String className, Integer year, Integer semester) {
        this.enrollmentId = enrollmentId;
        this.userId = userId;
        this.studentName = studentName;
        this.className = className;
        this.year = year;
        this.semster = semster;
    }

    // Getters
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getClassName() {
        return className;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getSemester() {
        return semster;
    }

    // Setters
    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setSemester(Integer semster) {
        this.semster = semster;
    }

    @Override
    public String toString() {
        return "Student{" +
                "enrollmentId='" + enrollmentId + '\'' +
                ", userId='" + userId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", className='" + className + '\'' +
                ", year=" + year +
                ", semester=" + semster +
                '}';
    }
}