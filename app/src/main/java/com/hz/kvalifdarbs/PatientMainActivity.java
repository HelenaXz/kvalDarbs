package com.hz.kvalifdarbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientMainActivity extends AppCompatActivity {
    DatabaseReference rootRef, patientRef;
    Button connectDevice;
    String patientId, patientNrString;
    TextView patientNr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        patientId = i.getStringExtra("patientId");

        rootRef = FirebaseDatabase.getInstance().getReference();
        patientRef = rootRef.child("Patients").child(patientId);

        patientNr = findViewById(R.id.patientId);
        patientNrString = "Patient Nr. " + patientId;
        patientNr.setText(patientNrString);

        connectDevice = findViewById(R.id.btnConnectDevice);
        //TODO set action on button click

    }
}
