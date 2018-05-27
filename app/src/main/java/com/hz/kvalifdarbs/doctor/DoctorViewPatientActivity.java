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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.ListAdaptors.PatientExamAdapter;
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
    String valueType;
    Patient thisPatient;
    Context context;
    PatientExamAdapter testAdapter;
    DatabaseReference rootRef, childRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        final Intents intents = new Intents(this);

        //Strings
        String userId = PreferenceUtils.getId(context);
        valueType = "Examinations";

        Intent i = getIntent();
        thisPatient = (Patient)i.getSerializableExtra("thisPatient");

        //Firebase reference
        rootRef = FirebaseDatabase.getInstance().getReference();
        childRef = rootRef.child("Patients").child(thisPatient.getId());

        //TextViews, Buttons
        TextView  name_surname = findViewById(R.id.name_surname);
        TextView patientId = findViewById(R.id.patientId_field);
        TextView patientRoom = findViewById(R.id.patientRoom);
        TextView birthDate = findViewById(R.id.birthDate);
        TextView brought_in = findViewById(R.id.brought_in);
        TextView moveEvery = findViewById(R.id.moveEvery);
        Button addExam = findViewById(R.id.addExamBtn);
        Button examinationsBtn = findViewById(R.id.btnExamination);
        Button movementsBtn = findViewById(R.id.btnMovements);


        //Set TextViews
        name_surname.setText(thisPatient.getFullName());
        patientId.append(thisPatient.getId());

        patientRoom.append(thisPatient.getRoom());
        birthDate.append(thisPatient.getBirthDate());
        brought_in.append(thisPatient.getAddedToSystem());
        moveEvery.append(String.valueOf(thisPatient.getMoveEveryTime()) + " mins");

        //ListView set up
        ListView patientValueList = findViewById(R.id.patientExamList);

        testAdapter = new PatientExamAdapter(this);
        patientValueList.setAdapter(testAdapter);

        TextView emptyElement = findViewById(R.id.emptyElement);
        TextView emptyText = findViewById(android.R.id.empty);
        patientValueList.setEmptyView(emptyText);

        getChildren(valueType);

        examinationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examinationsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                movementsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                valueType = "Examinations";
                testAdapter.clear();
                getChildren(valueType);
            }
        });
        movementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examinationsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                movementsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                valueType = "Movements";
                testAdapter.clear();
                getChildren(valueType);
            }
        });

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
                        addExam(examComment, time);
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


    public void getChildren(String valueType){
        childRef.child(valueType).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Examination exam = dataSnapshot.getValue(Examination.class);
                testAdapter.insert(exam, 0);
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
    }

    public void addExam(EditText examComment, String time ){
        String doctorId = PreferenceUtils.getId(context);
        String doctorName = PreferenceUtils.getUserName(context) + " " + PreferenceUtils.getUserSurname(context);
        String commentString = examComment.getText().toString();
        Examination newExam = new Examination(doctorId, doctorName, time, commentString);
        thisPatient.setLastExam(newExam);

        DatabaseReference thisPatientRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(thisPatient.getId());
        thisPatientRef.child("Examinations").child(time).setValue(newExam);
        thisPatientRef.child("lastExam").setValue(newExam);
        MethodHelper.showToast(getApplicationContext(), "Examination added");
    }
}
