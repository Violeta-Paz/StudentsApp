package com.example.studentsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String STUDENT_RUT = "net.simplifiedcoding.firebasedatabaseexample.studentRut";
    public static final String STUDENT_RUTid = "net.simplifiedcoding.firebasedatabaseexample.studentRutId";
    public static final String STUDENT_NAME = "net.simplifiedcoding.firebasedatabaseexample.studentName";
    public static final String STUDENT_EMAIL = "net.simplifiedcoding.firebasedatabaseexample.studentEmail";
    public static final String STUDENT_CLASS = "net.simplifiedcoding.firebasedatabaseexample.studentClass";

    //view objects
    EditText idAddRut;
    EditText idAddName;
    EditText idAddEmail;
    Spinner spinnerClass;
    Button buttonAddStudent;
    ListView listViewStudents;

    //a list to store all the artist from firebase database
    List<Students> students;

    DatabaseReference databaseStudents;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseStudents = firebaseDatabase.getReference("student");

        //getting the reference of artists node
        //databaseStudents = FirebaseDatabase.getInstance().getReference("students");

        //getting views
        idAddRut= (EditText) findViewById(R.id.idAddRut);
        idAddName= (EditText) findViewById(R.id.idAddName);
        idAddEmail= (EditText) findViewById(R.id.idAddEmail);
        spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
        listViewStudents = (ListView) findViewById(R.id.listViewStudent);
        buttonAddStudent = (Button) findViewById(R.id.buttonAddStudent);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Option_Class, android.R.layout.simple_spinner_item);

        //spinnerClass.setAdapter(adapter);

        //list to store artists
        students = new ArrayList<>();


        //adding an onClickListener to button
        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addStudent();
            }
        });

        //attaching listener to listview
        listViewStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected artist
                Students student = students.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), NotesMainActivity.class);

                //putting artist name and id to intent
                intent.putExtra(STUDENT_RUTid, student.getStudentRut().substring(0,8));
                intent.putExtra(STUDENT_RUT, student.getStudentRut());
                intent.putExtra(STUDENT_NAME, student.getStudentName());
                intent.putExtra(STUDENT_EMAIL, student.getStudentEmail());
                intent.putExtra(STUDENT_CLASS, student.getStudentClass());

                //starting the activity with intent
                startActivity(intent);
            }
        });


        listViewStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Students student = students.get(i);
                showUpdateDeleteDialog(student.getStudentRut(), student.getStudentName());
                return true;
            }
        });

    }

    private void addStudent() {


        //getting the values to save
        String rut = idAddRut.getText().toString().trim();
        String name = idAddName.getText().toString().trim();
        String email = idAddEmail.getText().toString().trim();
        String sClass = spinnerClass.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) &&  !TextUtils.isEmpty(sClass) ) {

            //int intRut = Integer.parseInt(noteTest);
           //getting a unique id using push().getKey() method
            // it will create a unique id and we will use it as the Primary Key for our Artist
            //String id = databaseStudents.push().getKey();

            //creating an Artist Object
            Students students = new Students(rut, name, email, sClass);



            //Saving the Artist
            databaseStudents.child(rut.substring(0,8)).setValue(students);

            //setting edittext to blank again
            idAddRut.setText("");
            idAddName.setText("");
            idAddEmail.setText("");

            //displaying a success toast
            Toast.makeText(this, "student added", Toast.LENGTH_LONG).show();



        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show();


        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                students.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Students student = postSnapshot.getValue(Students.class);
                    //adding artist to the list
                    students.add(student);
                }

                //creating adapter
                StudentsList studentsAdapter = new StudentsList(MainActivity.this, students);
                //attaching adapter to the listview
                listViewStudents.setAdapter(studentsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private boolean updateStudent(String rut, String name, String email, String sClass) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("student").child(rut.substring(0,8));

        //updating artist
        Students students = new Students(rut.substring(0,8), name, email, sClass);
        dR.setValue(students);
        Toast.makeText(getApplicationContext(), "Student Updated", Toast.LENGTH_LONG).show();
        return true;

    }




    private void showUpdateDeleteDialog(final String studentId, String studentName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_student, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextEmail = (EditText) dialogView.findViewById(R.id.editTextEmail);
        final Spinner spinnerStudent = (Spinner) dialogView.findViewById(R.id.spinnerClass);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateStudent);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteStudent);

        dialogBuilder.setTitle(studentName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String sClass = spinnerStudent.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)  ) {
                    updateStudent(studentId.substring(0,8), name, email, sClass);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                deleteStudent(studentId.substring(0,8));
                b.dismiss();


            }
        });



    }

    private boolean deleteStudent(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("student").child(id);

        //removing artist
        dR.removeValue();

        //getting the tracks reference for the specified artist
        DatabaseReference drNotes = FirebaseDatabase.getInstance().getReference("notes").child(id);

        //removing all tracks
        drNotes.removeValue();
        Toast.makeText(getApplicationContext(), "Student Deleted", Toast.LENGTH_LONG).show();

        return true;
    }


}