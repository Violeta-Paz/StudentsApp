package com.example.studentsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotesMainActivity extends AppCompatActivity {

    Button buttonAddNote;
    EditText idAddNotes;
    //EditText idAddDateTest;
    TextView textViewName, textViewEmail, textViewRut, textViewClass, textViewAverage;
    ListView listViewNotes;

    DatabaseReference databaseNotes;

    List<Notes> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);

        Intent intent = getIntent();

        databaseNotes = FirebaseDatabase.getInstance().getReference("notes").child(intent.getStringExtra(MainActivity.STUDENT_RUTid));



        buttonAddNote = (Button) findViewById(R.id.buttonAddNote);
        idAddNotes = (EditText) findViewById(R.id.idAddNotes);
        //idAddDateTest = (EditText) findViewById(R.id.idAddDateTest);

        //idAddDateTest = Calendar.getInstance();
        textViewAverage = (TextView) findViewById(R.id.textViewAverage);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewRut = (TextView) findViewById(R.id.textViewRut);
        textViewClass = (TextView) findViewById(R.id.textViewClass);
        listViewNotes = (ListView) findViewById(R.id.listViewNotes);

        notes = new ArrayList<>();
        textViewRut.setText(intent.getStringExtra(MainActivity.STUDENT_RUT));
        textViewName.setText(intent.getStringExtra(MainActivity.STUDENT_NAME));
        textViewEmail.setText(intent.getStringExtra(MainActivity.STUDENT_EMAIL));
        textViewClass.setText(intent.getStringExtra(MainActivity.STUDENT_CLASS));



        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseNotes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Notes note = postSnapshot.getValue(Notes.class);
                    notes.add(note);


                    int average = 0;

                    for (int i = 0; i < notes.size(); i++){

                        average = average + notes.get(i).getNote();

                    }

                    double totalAverage = average / notes.size();
                    textViewAverage.setText(String.valueOf(totalAverage) );



                }
                NotesList notesListAdapter = new NotesList(NotesMainActivity.this, notes);
                listViewNotes.setAdapter(notesListAdapter);

                //averageNotes.add(averageNote);


                //List<Notes>[] notesArray = {notes};


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveNotes() {
        String noteTest = idAddNotes.getText().toString().trim();
        //String  noteTestDate = idAddDateTest.getText().toString().trim();
        Date anotherCurDate = new Date();
        //SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d 'at' hh:mm a 'in the year' yyyy G");
        //String formattedDateString = formatter.format(anotherCurDate);
        Date noteTestDate = anotherCurDate;
        String sRut = textViewRut.getText().toString().trim();
        int intNote = Integer.parseInt(noteTest);
        if (!TextUtils.isEmpty(noteTest) ) {


            String id  = databaseNotes.push().getKey();
            Notes notes = new Notes(id, intNote , noteTestDate, sRut);
            databaseNotes.child(id).setValue(notes);
            Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show();
            idAddNotes.setText("");
            //idAddDateTest.setText("");
        } else {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_LONG).show();
        }

    }



}