package com.hz.kvalifdarbs.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.Objects.Examination;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DoctorViewPatientActivity extends AppCompatActivity {
    TextView name_surname, room, brought_in, birthDate, patientId, patientRoom;
    String userId, patientIdString;
    Patient thisPatient;
    Context context;
    Button addExam;

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
        patientId = findViewById(R.id.patientId_field);
        patientRoom = findViewById(R.id.patientRoom);
        birthDate = findViewById(R.id.birthDate);
        brought_in = findViewById(R.id.brought_in);
        
        addExam = findViewById(R.id.addExamBtn);

        //Set TextViews
        name_surname.setText(thisPatient.getFullName());
        patientId.append(thisPatient.getId());

        patientRoom.append(thisPatient.getRoom());
        birthDate.append(thisPatient.getBirthDate());
        brought_in.append(thisPatient.getAddedToSystem());

        //TODO set up exam list

        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorViewPatientActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_add_examination, null);
                TextView patientInfo, dateTime;
                final EditText examComment;
                patientInfo = mView.findViewById(R.id.patientName);
                dateTime = mView.findViewById(R.id.addTime);
                examComment = mView.findViewById(R.id.examComment);

                String s = thisPatient.getFullName() + " : " + thisPatient.getId();
                patientInfo.append(s);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currentTime = Calendar.getInstance().getTime();
                final String time  = sdf.format(currentTime);
                dateTime.append(time);

                Button addExamBtn = mView.findViewById(R.id.btnAddExam);
                Button cancel = mView.findViewById(R.id.btnCancel);

                builder.setView(mView);
                final AlertDialog addExamDialog = builder.create();

                addExamBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Save new examination
                        String doctorId = PreferenceUtils.getId(context);
                        String doctorName = PreferenceUtils.getUserName(context) + " " + PreferenceUtils.getUserSurname(context);
                        String commentString = examComment.getText().toString();
                        Examination newExam = new Examination(doctorId, doctorName, time, commentString);

                        DatabaseReference thisPatientRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(thisPatient.getId()).child("Examinations");
                        thisPatientRef.child(time).setValue(newExam);
                        Toast toast = Toast.makeText(getApplicationContext(), "Examination added", Toast.LENGTH_SHORT);
                        toast.show();
                        addExamDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addExamDialog.dismiss();
                    }
                });
                addExamDialog.show();
            }
        });

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
