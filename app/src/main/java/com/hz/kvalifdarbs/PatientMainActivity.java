package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class PatientMainActivity extends AppCompatActivity {
    DatabaseReference rootRef, patientRef;
    Button connectDevice, btnLogout;
    String patientId, patientNrString;
    TextView patientNr;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        patientId = i.getStringExtra("userId");

        rootRef = FirebaseDatabase.getInstance().getReference();
        patientRef = rootRef.child("Patients").child(patientId);

        patientNr = findViewById(R.id.patientId);
        patientNrString = "Patient Nr. " + patientId;
        patientNr.setText(patientNrString);

        //TODO set action on button click
        connectDevice = findViewById(R.id.btnConnectDevice);
        btnLogout = findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveId("", context);
                PreferenceUtils.savePassword("", context);
                PreferenceUtils.saveUserType("", context);
                startActivity(intents.userSelect);
            }
        });

    }
}
