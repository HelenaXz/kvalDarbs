package com.hz.kvalifdarbs.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;


public class DoctorViewPatientActivity extends AppCompatActivity {
    TextView name_surname, room, brought_in, birthDate, patientId, patientRoom;
    String userId, patientIdString;
    Patient thisPatient;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        final Intents intents = new Intents(this);

        Intent i = getIntent();
        thisPatient = (Patient)i.getSerializableExtra("thisPatient");

        //TextViews, Buttons
        userId = PreferenceUtils.getId(context);
        name_surname = findViewById(R.id.name_surname);
        room = findViewById(R.id.room);
        brought_in = findViewById(R.id.brought_in);
        birthDate = findViewById(R.id.birthDate);
        patientId = findViewById(R.id.patientId_field);
        patientRoom = findViewById(R.id.patientRoom);
        //TODO Add new Examination button setup and onClick action

        //Set TextViews
        name_surname.setText(thisPatient.getFullName());
        brought_in.setText(thisPatient.getAddedToSystem());
        birthDate.setText(thisPatient.getBirthDate());
        patientIdString = "Patient nr. "+ thisPatient.getId();
        patientId.setText(patientIdString);
        patientRoom.setText(thisPatient.getRoom());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent intent = intents.doctorPatientList;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

    }
}
