package com.example.studentsapp;

import com.google.firebase.database.IgnoreExtraProperties;

public class Students {

    private String studentRut;
    private String studentName;
    private String studentEmail;
    private String studentClass;

    public Students(){
        //this constructor is required
    }

    public Students(String studentRut, String studentName, String studentEmail, String studentClass) {
        this.studentRut = studentRut;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentClass = studentClass;
    }

    public String getStudentRut() {
        return studentRut;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentClass() {
        return studentClass;
    }

}
