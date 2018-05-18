package com.hz.kvalifdarbs.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.ListAdaptors.DoctorPatientAdapter;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.util.ArrayList;

public class DoctorPatientListActivity extends AppCompatActivity {
    ListView patientList;
    ArrayList<String> doctorPatients;
    DoctorPatientAdapter testAdapter;
    Context context;
    DatabaseReference rootRef, childRef;
    String doctorId;
    TextView emptyElement;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intents intents = new Intents(this);
        context = getApplicationContext();
        rootRef = FirebaseDatabase.getInstance().getReference();
        doctorId  = PreferenceUtils.getId(context);
        childRef = rootRef.child("Doctors").child(doctorId);
        emptyElement = findViewById(R.id.emptyElement);


        patientList = findViewById(R.id.patientList);
        doctorPatients = new ArrayList<>(); //list of doctor patient id's
        testAdapter = new DoctorPatientAdapter(this);
        patientList.setAdapter(testAdapter);


        TextView emptyText = findViewById(android.R.id.empty);
        patientList.setEmptyView(emptyText);



        childRef.child("Patients").addValueEventListener(new ValueEventListener() {
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

        patientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient clicked = ((Patient) parent.getItemAtPosition(position));
                Intent seePatient = intents.doctorPatientView;
                seePatient.putExtra("thisPatient", clicked);
                startActivity(seePatient.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent intent = intents.doctorMainMenu;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        });

    }

}