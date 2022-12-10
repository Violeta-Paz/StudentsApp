package com.example.studentsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotesList extends ArrayAdapter<Notes> {

    private Activity context;
    List<Notes> notesList;

    public NotesList(Activity context, List<Notes> notes) {
            super(context, R.layout.layout_notes_lists, notes);
            this.context = context;
            this.notesList = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_notes_lists, null, true);

        TextView textViewNotes = (TextView) listViewItem.findViewById(R.id.textViewNotes);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);


        Notes notes = notesList.get(position);
        textViewNotes.setText( String.valueOf(notes.getNote()) );
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d 'at' hh:mm a yyyy");
        String formattedDateString = formatter.format(notes.getDateTest());
        textViewDate.setText(formattedDateString);

        //notes.getDateTest()
        return listViewItem;
    }


}
