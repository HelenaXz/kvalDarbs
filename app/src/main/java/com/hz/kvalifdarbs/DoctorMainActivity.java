package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class DoctorMainActivity extends AppCompatActivity {
    Button viewAllPat, btnLogout;
    String doctorId;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        doctorId = i.getStringExtra("userId");



        viewAllPat = findViewById(R.id.btnViewPatients);
        btnLogout = findViewById(R.id.btnLogout);

        viewAllPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = intents.doctorPatientList;
                intent.putExtra("userId", doctorId);
                startActivity(intent);
            }
        });

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
