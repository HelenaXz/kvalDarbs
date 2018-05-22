package com.hz.kvalifdarbs.admin;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.hz.kvalifdarbs.ListAdaptors.AvaiableDoctorAdapter;
        import com.hz.kvalifdarbs.utils.Intents;
        import com.hz.kvalifdarbs.Objects.Doctor;
        import com.hz.kvalifdarbs.Objects.Patient;
        import com.hz.kvalifdarbs.R;

        import java.util.ArrayList;

public class AvailableDoctorListActivity extends AppCompatActivity {
    DatabaseReference rootRef, childRef;
    ArrayList<Doctor> allDoctors;
    AvaiableDoctorAdapter testAdapter;
    DatabaseReference patientRef, doctorRef;
    Patient thisPatient;
    Context context;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        context  = getApplicationContext();
        final Intents intents = new Intents(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        thisPatient = (Patient)i.getSerializableExtra("thisPatient");
        //Firebase references
        rootRef = FirebaseDatabase.getInstance().getReference();
        patientRef = rootRef.child("Patients").child(thisPatient.getId());

        //Set up listView
        ListView listView = findViewById(R.id.allDoctors);
        allDoctors = new ArrayList<>();
        testAdapter = new AvaiableDoctorAdapter(this, allDoctors);

        final ArrayList<String> patientDoctors = new ArrayList();

        listView.setAdapter(testAdapter);
        listView.setClickable(true);


        patientRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                patientDoctors.add(dataSnapshot.getKey());
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


        rootRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!patientDoctors.contains(dataSnapshot.getKey())){
                    doctor = dataSnapshot.getValue(Doctor.class);
                    childRef = rootRef.child(dataSnapshot.getKey());
                    testAdapter.add(doctor);
                    testAdapter.notifyDataSetChanged();
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor clicked = ((Doctor) parent.getItemAtPosition(position));
                patientRef = rootRef.child("Patients").child(thisPatient.getId());
                doctorRef = rootRef.child("Doctors").child(clicked.getId());
                patientRef.child("Doctors").child(clicked.getId()).setValue(clicked.getFullName());
                doctorRef.child("Patients").child(thisPatient.getId()).setValue(thisPatient.getId());
                Intent patientView = intents.adminPatientView;
                patientView.putExtra("thisPatient", thisPatient);
                context.startActivity(patientView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                Toast toast = Toast.makeText(context, "Doctor added to patient", Toast.LENGTH_SHORT);
                toast.show();
                finish();

            }
        });

    }


}
