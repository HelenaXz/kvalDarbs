package com.hz.kvalifdarbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Patient;


public class DoctorViewPatientActivity extends AppCompatActivity {
    TextView name_surname, room, brought_in, birthDate, patientId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);

        Intent i = getIntent();
        Patient thisPatient = (Patient)i.getSerializableExtra("thisPatient");
        name_surname = findViewById(R.id.name_surname);
        room = findViewById(R.id.room);
        brought_in = findViewById(R.id.brought_in);
        birthDate = findViewById(R.id.birthDate);
        patientId = findViewById(R.id.patientId_field);

        String fullName = thisPatient.getName()+" "+thisPatient.getSurname();
        name_surname.setText(fullName);
        brought_in.setText(thisPatient.getAddedToSystem());
        birthDate.setText(thisPatient.getBirthDate());
        String patientIdString = "Patient nr. "+ thisPatient.getId();
        patientId.setText(patientIdString);






    }
}