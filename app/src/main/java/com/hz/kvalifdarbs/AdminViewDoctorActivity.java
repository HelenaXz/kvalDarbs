package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.Objects.Patient;

import java.util.ArrayList;

public class AdminViewDoctorActivity extends AppCompatActivity {
    Context context;
    DatabaseReference rootRef, docRef, allPatientRef, patientRef;
    TextView name, surname, phone;
    Button deleteUser;
    ArrayList<String> doctorPatients;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_doctor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intents intents = new Intents(this);
        context = getApplicationContext();
        rootRef = FirebaseDatabase.getInstance().getReference();
        doctorPatients = new ArrayList<>();

        Intent i = getIntent();
        final Doctor thisDoctor = (Doctor)i.getSerializableExtra("thisDoctor");
        docRef = rootRef.child("Doctors").child(thisDoctor.getId());
        allPatientRef = rootRef.child("Patients");

        //Get list of Doctor patients

        deleteUser = findViewById(R.id.btnDeleteUser);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.phone_number);

        //Fill text views
        name.setText(thisDoctor.getName());
        surname.setText(thisDoctor.getSurname());
        String phoneString = thisDoctor.getPhone().toString();
        phone.setText(phoneString);

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewDoctorActivity.this);
                builder.setMessage("Delete doctor from system?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(String patId : doctorPatients){
                            patientRef = allPatientRef.child(patId);
                            patientRef.child("Doctors").child(thisDoctor.getId()).removeValue();
                        }
                        docRef.removeValue();
                        startActivity(intents.allDoctorList);

                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog deleteAlert = builder.create();
                deleteAlert.show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.allDoctorList);
            }
        });

        docRef.child("Patients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String patientId = dataSnapshot.getValue().toString();
                doctorPatients.add(patientId);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
