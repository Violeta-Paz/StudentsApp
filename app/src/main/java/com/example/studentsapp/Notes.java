package com.example.studentsapp;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;
import java.util.Date;

public class Notes {

    private String id;
    private int note;
    private Date dateTest;
    private String sRut;

    public Notes() {

    }

    public Notes(String id, int note, Date dateTest, String sRut  ) {
        this.id = id;
        this.note = note;
        this.dateTest = dateTest;
        this.sRut = sRut;

    }

    public String getId() {
        return id;
    }

    public Date getDateTest() {
        return dateTest;
    }

    public int getNote() {
        return note;
    }

    public String getSRut() {
        return sRut;
    }





}
