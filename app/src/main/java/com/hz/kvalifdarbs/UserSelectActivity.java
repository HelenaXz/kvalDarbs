package com.hz.kvalifdarbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UserSelectActivity extends AppCompatActivity {
    Button admin, doctor, patient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user_type);
        final Intents intents = new Intents(this);


        admin = findViewById(R.id.adminBtn);
        doctor = findViewById(R.id.doctorBtn);
        patient = findViewById(R.id.patientBtn);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginAdmin = intents.loginView;
                loginAdmin.putExtra("UserType", "Administrator");
                startActivity(loginAdmin);
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginAdmin = intents.loginView;
                loginAdmin.putExtra("UserType", "Doctor");
                startActivity(loginAdmin);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginAdmin = intents.loginView;
                loginAdmin.putExtra("UserType", "Patient");
                startActivity(loginAdmin);
            }
        });




    }
}
