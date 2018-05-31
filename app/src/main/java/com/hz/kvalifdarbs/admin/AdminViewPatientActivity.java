package com.hz.kvalifdarbs.admin;

import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.hz.kvalifdarbs.ListAdaptors.PatientExamAdapter;
import com.hz.kvalifdarbs.ListAdaptors.PatientMovementAdapter;
import com.hz.kvalifdarbs.Objects.Examination;
import com.hz.kvalifdarbs.Objects.Movement;
import com.hz.kvalifdarbs.Objects.smallDoctor;
        import com.hz.kvalifdarbs.utils.Intents;
        import com.hz.kvalifdarbs.Objects.Patient;
        import com.hz.kvalifdarbs.ListAdaptors.PatientDoctorSmallAdapter;
        import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;

import java.util.ArrayList;


public class AdminViewPatientActivity extends AppCompatActivity {
    DatabaseReference allDocRef, patientRef, doctorRef;
    PatientDoctorSmallAdapter testAdapter;
    PatientExamAdapter testAdapter2;
    PatientMovementAdapter testAdapter3;
    Context context;
    ArrayList<String> patientDocList;
    String valueType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_patient);
        final Intents intents = new Intents(this);
        context = getApplicationContext();
        Intent i = getIntent();
        final Patient thisPatient = (Patient)i.getSerializableExtra("thisPatient");
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Firebase References
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        allDocRef = rootRef.child("Doctors");
        patientRef = rootRef.child("Patients").child(thisPatient.getId());

        patientDocList = new ArrayList<>();
        //TextViews, ListViews, Buttons
        TextView name_surname = findViewById(R.id.name_surname);
        TextView room = findViewById(R.id.room);
        TextView brought_in = findViewById(R.id.brought_in);
        TextView birthDate = findViewById(R.id.birthDate);
        TextView patientId = findViewById(R.id.patientId_field);
        Button addDoctorTo = findViewById(R.id.btnAddDoc);
        Button deleteUser = findViewById(R.id.btnDeleteUser);
        Button examinationsBtn = findViewById(R.id.btnExamination);
        Button movementsBtn = findViewById(R.id.btnMovements);
        ListView patientDoctors = findViewById(R.id.patientDoctorList);
        TextView moveEvery = findViewById(R.id.moveEvery);


        //Fill TextViews
        name_surname.setText(thisPatient.getFullName());
        brought_in.append(thisPatient.getAddedToSystem());
        birthDate.append(thisPatient.getBirthDate());
        patientId.append(thisPatient.getId());
        room.append(thisPatient.getRoom());
        moveEvery.append(thisPatient.getMoveEveryTime() + " mins");

        //ArrayAdapter for small doctor list
        testAdapter = new PatientDoctorSmallAdapter(this);
        patientDoctors.setAdapter(testAdapter);

        //Patient Examination list view setup
        ListView patientValueList = findViewById(R.id.patientExamList);

        testAdapter2 = new PatientExamAdapter(this);
        testAdapter3 = new PatientMovementAdapter(this);
        valueType = "Examinations";
        getChildren(valueType);
        patientValueList.setAdapter(testAdapter2);


        TextView emptyText = findViewById(android.R.id.empty);
        patientValueList.setEmptyView(emptyText);



        examinationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examinationsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                movementsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                valueType = "Examinations";
                testAdapter2.clear();
                testAdapter3.clear();
                getChildren(valueType);
                patientValueList.setAdapter(testAdapter2);
            }
        });
        movementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examinationsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                movementsBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                valueType = "Movements";
                testAdapter2.clear();
                testAdapter3.clear();
                getChildren(valueType);
                patientValueList.setAdapter(testAdapter3);
            }
        });

        addDoctorTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allDocList = intents.availableDoctorList;
                allDocList.putExtra("thisPatient", thisPatient);
                startActivity(allDocList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });


        patientDoctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final smallDoctor clicked = (smallDoctor) parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewPatientActivity.this);
                builder.setMessage("Delete doctor from patient?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        patientRef.child("Doctors").child(clicked.getId()).removeValue();
                        doctorRef = rootRef.child("Doctors").child(clicked.getId());
                        doctorRef.child("Patients").child(thisPatient.getId()).removeValue();
                        testAdapter.remove(testAdapter.getItem(position));
                        testAdapter.notifyDataSetChanged();
                        MethodHelper.showToast(context, "Doctor removed from patient");
                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog deleteAlert = builder.create();
                deleteAlert.show();
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewPatientActivity.this);
                builder.setMessage("Delete patient from system?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(String docId : patientDocList){
                            doctorRef = allDocRef.child(docId);
                            doctorRef.child("Patients").child(thisPatient.getId()).removeValue();
                        }
                        patientRef.removeValue();
                        startActivity(intents.allPatientList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        MethodHelper.showToast(context, "User deleted");

                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog deleteAlert = builder.create();
                deleteAlert.show();
            }
        });

        getPatientDoctors();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                startActivity(intents.allPatientList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }
    public void getChildren(String valueType){
        patientRef.child(valueType).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(valueType.equals("Examinations")){
                    Examination exam = dataSnapshot.getValue(Examination.class);
                    testAdapter2.insert(exam, 0);
                } else if(valueType.equals("Movements")) {
                    Movement move = dataSnapshot.getValue(Movement.class);
                    testAdapter3.insert(move, 0);
                }
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
    public void getPatientDoctors(){
        patientRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String doctorId = dataSnapshot.getKey();
                String doctorName = dataSnapshot.getValue().toString();
                smallDoctor newSmallDoc = new smallDoctor(doctorId, doctorName);

                testAdapter.add(newSmallDoc);
                patientDocList.add(newSmallDoc.getId());
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
}
