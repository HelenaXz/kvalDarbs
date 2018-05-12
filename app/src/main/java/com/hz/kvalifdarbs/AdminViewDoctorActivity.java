package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.Objects.Patient;

public class AdminViewDoctorActivity extends AppCompatActivity {
    Context context;
    DatabaseReference rootRef, docRef, patientRef;
    TextView name, surname, phone;


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
        Intent i = getIntent();
        final Doctor thisDoctor = (Doctor)i.getSerializableExtra("thisDoctor");

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.phone_number);

        //Fill text views
        name.setText(thisDoctor.getName());
        surname.setText(thisDoctor.getSurname());
        String phoneString = thisDoctor.getPhone().toString();
        phone.setText(phoneString);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
//                Intent seeDoctor = intents.adminDoctorView;
//                seeDoctor.putExtra("thisDoctor", thisDoctor);
                startActivity(intents.allDoctorList);
            }
        });

    }

}
