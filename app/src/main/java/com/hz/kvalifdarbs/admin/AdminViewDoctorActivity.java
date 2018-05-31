package com.hz.kvalifdarbs.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.ListAdaptors.DoctorPatientAdapter;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;

import java.util.ArrayList;

public class AdminViewDoctorActivity extends AppCompatActivity {
    Context context;
    DatabaseReference rootRef, docRef, allPatientRef, patientRef;
    ArrayList<String> doctorPatients;
    ListView patientList;
    DoctorPatientAdapter testAdapter;
    TextView emptyElement;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_doctor);
        context = getApplicationContext();
        final Intents intents = new Intents(this);

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                testAdapter.notifyDataSetChanged();
                handler.postDelayed( this, 30 * 1000 );
            }
        }, 30 * 1000 );

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase reference
        rootRef = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        final Doctor thisDoctor = (Doctor)i.getSerializableExtra("thisDoctor");
        docRef = rootRef.child("Doctors").child(thisDoctor.getId());
        allPatientRef = rootRef.child("Patients");

        //Strings
        String phoneString = thisDoctor.getPhone().toString();

        //TextViews, Buttons
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone_number);
        TextView idTV = findViewById(R.id.doctorId_field);
        Button deleteUser = findViewById(R.id.btnDeleteUser);

        doctorPatients = new ArrayList<>();

        //Fill text views
        name.setText(thisDoctor.getFullName());
        phone.append(phoneString);
        idTV.append(thisDoctor.getId());

        //Set up Patient ListView
        patientList = findViewById(R.id.doctorPatientList);
        doctorPatients = new ArrayList<>(); //list of doctor patient id's
        testAdapter = new DoctorPatientAdapter(this);
        patientList.setAdapter(testAdapter);

        emptyElement = findViewById(R.id.emptyElement);
        TextView emptyText = findViewById(android.R.id.empty);
        patientList.setEmptyView(emptyText);

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(thisDoctor);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.allDoctorList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });


        getDoctorPatients();
        showDoctorPatients();


    }
    public void getDoctorPatients(){
        docRef.child("Patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                    String patientId = uniqueKeySnapshot.getKey();
                    doctorPatients.add(patientId);
                }
                Integer testAdapterSize = doctorPatients.size();
                if(testAdapterSize==0){
                    String emptyText = "There are no patients assigned to you.";
                    emptyElement.setText(emptyText);
                    patientList.setEmptyView(emptyElement);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showDoctorPatients(){
        rootRef.child("Patients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (String curr : doctorPatients) if (curr.equals(dataSnapshot.getKey())){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    testAdapter.add(patient);
                }
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

    public void deleteUser(final Doctor thisDoctor){
        final Intents intents = new Intents(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewDoctorActivity.this);
        builder.setMessage("Delete doctor from system?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for(String patId : doctorPatients){
                    patientRef = allPatientRef.child(patId);
                    patientRef.child("Doctors").child(thisDoctor.getId()).removeValue();
                }
                docRef.removeValue();
                startActivity(intents.allDoctorList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                MethodHelper.showToast(context, "User deleted");

            }
        }).setNegativeButton("Cancel", null);

        AlertDialog deleteAlert = builder.create();
        deleteAlert.show();
    }
}
