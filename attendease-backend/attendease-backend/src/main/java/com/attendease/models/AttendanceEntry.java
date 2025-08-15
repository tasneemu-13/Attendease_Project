package com.attendease.models;



public class AttendanceEntry {
    private String enrollmentId;
    private boolean present;

    // Default constructor is needed for Gson to work correctly
    public AttendanceEntry() {
    }

    // Parameterized constructor (optional, but good practice)
    public AttendanceEntry(String enrollmentId, boolean present) {
        this.enrollmentId = enrollmentId;
        this.present = present;
    }

    // Getters
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public boolean isPresent() {
        return present;
    }

    // Setters
    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
