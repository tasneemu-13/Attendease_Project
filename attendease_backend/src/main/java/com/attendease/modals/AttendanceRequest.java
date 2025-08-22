package com.attendease.modals;

public class AttendanceRequest {
    private String courseCode;
    private String className;
    private String markedBy;
    private String date;
    private AttendanceEntry[] attendanceEntries;

    // Default constructor is needed for Gson
    public AttendanceRequest() {
    }

    // Parameterized constructor (optional)
    public AttendanceRequest(String courseCode, String className, String markedBy, String date, AttendanceEntry[] attendanceEntries) {
        this.courseCode = courseCode;
        this.className = className;
        this.markedBy = markedBy;
        this.date = date;
        this.attendanceEntries = attendanceEntries;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getClassName() {
        return className;
    }

    public String getMarkedBy() {
        return markedBy;
    }

    public String getDate() {
        return date;
    }

    public AttendanceEntry[] getAttendanceEntries() {
        return attendanceEntries;
    }

    // Setters
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMarkedBy(String markedBy) {
        this.markedBy = markedBy;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAttendanceEntries(AttendanceEntry[] attendanceEntries) {
        this.attendanceEntries = attendanceEntries;
    }
}
