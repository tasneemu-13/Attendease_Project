package com.attendease.modals;

/**
 * A modal class to represent the attendance data for a single course.
 * This object is used by the backend to prepare a structured report
 * for the frontend, which will then display it in a chart and summary table.
 */
public class StudentAttendanceReport {
    private String course;
    private String teacherName; // Added field for the teacher's name
    private int present;
    private int absent;
    private int total;

    /**
     * Constructor for creating a StudentAttendanceReport object.
     *
     * @param course      The code or name of the course (e.g., "MIC").
     * @param teacherName The name of the teacher for the course.
     * @param present     The number of times the student was present.
     * @param absent      The number of times the student was absent.
     * @param total       The total number of classes held for the course.
     */
    public StudentAttendanceReport(String course, String teacherName, int present, int absent, int total) {
        this.course = course;
        this.teacherName = teacherName;
        this.present = present;
        this.absent = absent;
        this.total = total;
    }

    // Getters and setters for all fields.
    // These are essential for serialization (converting the object to JSON).

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}