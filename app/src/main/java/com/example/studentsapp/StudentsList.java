package com.example.studentsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentsList extends ArrayAdapter<Students> {

    private Activity context;
    List<Students> studentsLists;

    public StudentsList(Activity context, List<Students> students) {
        super(context, R.layout.layout_students_list, students);
        this.context = context;
        this.studentsLists= students;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_students_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRut = (TextView) listViewItem.findViewById(R.id.textViewRut);

        Students students = studentsLists.get(position);
        textViewName.setText(students.getStudentName());
        textViewRut.setText(students.getStudentRut());

        return listViewItem;
    }

}
