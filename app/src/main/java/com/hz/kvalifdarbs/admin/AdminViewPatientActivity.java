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
        import com.hz.kvalifdarbs.utils.Intents;
        import com.hz.kvalifdarbs.Objects.Patient;
        import com.hz.kvalifdarbs.ListAdaptors.PatientDoctorAdapter;
        import com.hz.kvalifdarbs.R;

        import java.util.ArrayList;


public class AdminViewPatientActivity extends AppCompatActivity {
    TextView name_surname, room, brought_in, birthDate, patientId;
    ListView patientExams, patientDoctors;
    Button addDoctorTo, deleteUser;
    String patientIdString;
    DatabaseReference rootRef, patientRef, doctorRef;
    PatientDoctorAdapter testAdapter;
    Context context;
    ArrayList<String> patientDocList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intents intents = new Intents(this);
        context = getApplicationContext();
        rootRef = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        final Patient thisPatient = (Patient)i.getSerializableExtra("thisPatient");

        name_surname = findViewById(R.id.name_surname);
        room = findViewById(R.id.room);
        brought_in = findViewById(R.id.brought_in);
        birthDate = findViewById(R.id.birthDate);
        patientId = findViewById(R.id.patientId_field);
        addDoctorTo = findViewById(R.id.btnAddDoc);
        deleteUser = findViewById(R.id.btnDeleteUser);

        //Fill TextViews
        name_surname.setText(thisPatient.getFullName());
        brought_in.setText(thisPatient.getAddedToSystem());
        birthDate.setText(thisPatient.getBirthDate());
        patientIdString = "Patient nr. "+ thisPatient.getId();
        patientId.setText(patientIdString);
        room.setText(thisPatient.getRoom());


        patientExams = findViewById(R.id.patientExamList); //ListView
        patientDoctors = findViewById(R.id.patientDoctorList); //ListView

        patientRef = rootRef.child("Patients").child(thisPatient.getId());

        patientDocList = new ArrayList<>();

        addDoctorTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allDocList = intents.availableDoctorList;
                allDocList.putExtra("thisPatient", thisPatient);
                startActivity(allDocList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });


        testAdapter = new PatientDoctorAdapter(this);
        patientDoctors.setAdapter(testAdapter);

        patientDoctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String clicked =  parent.getItemAtPosition(position).toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewPatientActivity.this);
                builder.setMessage("Delete doctor from patient?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        patientRef.child("Doctors").child(clicked).removeValue();
                        //doctor reference
                        doctorRef = rootRef.child("Doctors").child(clicked);
                        doctorRef.child("Patients").child(thisPatient.getId()).removeValue();
                        testAdapter.remove(testAdapter.getItem(position));
                        testAdapter.notifyDataSetChanged();
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
                            doctorRef = rootRef.child("Doctors").child(docId);
                            doctorRef.child("Patients").child(thisPatient.getId()).removeValue();
                        }
                        patientRef.removeValue();
                        startActivity(intents.allPatientList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog deleteAlert = builder.create();
                deleteAlert.show();
            }
        });

        patientRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String doctorId = dataSnapshot.getValue().toString();
//                Doctor doctorObj = dataSnapshot.getValue(Doctor.class);
//                doctorIds.add(doctor);
                testAdapter.add(doctorId);
                patientDocList.add(doctorId);
//                testAdapter.add(doctorName);
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                startActivity(intents.allPatientList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }
}
