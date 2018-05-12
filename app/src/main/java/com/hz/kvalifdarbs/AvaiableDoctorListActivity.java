package com.hz.kvalifdarbs;

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
        import com.hz.kvalifdarbs.Objects.Doctor;
        import com.hz.kvalifdarbs.Objects.Patient;

        import java.lang.reflect.Array;
        import java.util.ArrayList;

public class AvaiableDoctorListActivity extends AppCompatActivity {
    DatabaseReference rootRef, childRef;
    ArrayList<Doctor> allDoctors;
    private ListView listView;
    AvaiableDoctorAdapter testAdapter;
    DatabaseReference patientRef, doctorRef;
    Patient thisPatient;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        context  = getApplicationContext();
        thisPatient = (Patient)i.getSerializableExtra("thisPatient");
        rootRef = FirebaseDatabase.getInstance().getReference();
        patientRef = rootRef.child("Patients").child(thisPatient.getId());


        listView = findViewById(R.id.allDoctors);
        allDoctors = new ArrayList<>();
        testAdapter = new AvaiableDoctorAdapter(this, allDoctors, thisPatient);

        final ArrayList<String> patientDoctors = new ArrayList();



        patientRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                patientDoctors.add(dataSnapshot.getKey());
//                for (String curr : patientDoctors) if (curr.equals(dataSnapshot.getKey())){
//                    Patient patient = dataSnapshot.getValue(Patient.class);
//                    testAdapter.add(patient);
//                }
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
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    childRef = rootRef.child(dataSnapshot.getKey());
//                allPatients.add(patient);
                    //TODO add doctor to list only if the current patient doesn't have him already
                    testAdapter.add(doctor);
//                patientArrayAdapter.notifyDataSetChanged();
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


        listView.setAdapter(testAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor clicked = ((Doctor) parent.getItemAtPosition(position));
                patientRef = rootRef.child("Patients").child(thisPatient.getId());
                doctorRef = rootRef.child("Doctors").child(clicked.getId());
                patientRef.child("Doctors").child(clicked.getId()).setValue(clicked.getName()+ " " +clicked.getSurname());
                doctorRef.child("Patients").child(thisPatient.getId()).setValue(thisPatient.getId());
                Intent patientView = intents.adminPatientView;
                patientView.putExtra("thisPatient", thisPatient);
                context.startActivity(patientView);
                Toast toast = Toast.makeText(context, "Doctor added to patient", Toast.LENGTH_SHORT);
                toast.show();
                finish();

            }
        });

    }


}
