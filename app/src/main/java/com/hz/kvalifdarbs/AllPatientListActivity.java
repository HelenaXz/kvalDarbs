package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.Objects.Patient;

import java.util.ArrayList;

public class AllPatientListActivity extends AppCompatActivity {
    DatabaseReference rootRef, childRef;
    ArrayList<Patient> allPatients;
    private ListView listView;
    AdminPatientAdapter testAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patient_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intents intents = new Intents(this);
        listView = findViewById(R.id.allPatients);
        allPatients = new ArrayList<>();
//        patientArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allPatients);
        testAdapter = new AdminPatientAdapter(this, allPatients);

        context = getApplicationContext();


        rootRef = FirebaseDatabase.getInstance().getReference();


        rootRef.child("Patients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                childRef = rootRef.child(dataSnapshot.getKey());
//                allPatients.add(patient);
                testAdapter.add(patient);
//                patientArrayAdapter.notifyDataSetChanged();
                testAdapter.notifyDataSetChanged();

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


//        listView.setAdapter(patientArrayAdapter);
        listView.setAdapter(testAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Patient clicked = ((Patient) parent.getItemAtPosition(position));

//                Toast toast = Toast.makeText(getApplicationContext(), clicked.getId(), Toast.LENGTH_SHORT);
//                toast.show();
                Intent seePatient = intents.adminPatientView;
                seePatient.putExtra("thisPatient", clicked);
                startActivity(seePatient);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                startActivity(intents.adminMainMenu);
                finish();
            }
        });
    }
}
