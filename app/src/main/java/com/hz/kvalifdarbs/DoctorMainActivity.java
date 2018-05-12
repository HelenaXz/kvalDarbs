package com.hz.kvalifdarbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DoctorMainActivity extends AppCompatActivity {
    Button viewAllPat;
    String doctorId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        doctorId = i.getStringExtra("doctorId");


        viewAllPat = findViewById(R.id.btnViewPatients);

        viewAllPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = intents.doctorPatientList;
                intent.putExtra("doctorId", doctorId);
                startActivity(intent);
            }
        });

    }
}
