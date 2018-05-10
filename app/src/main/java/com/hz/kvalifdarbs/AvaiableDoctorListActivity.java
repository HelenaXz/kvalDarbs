

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
        setContentView(R.layout.activity_all_doctor_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        context  = getApplicationContext();
        thisPatient = (Patient)i.getSerializableExtra("thisPatient");


        listView = findViewById(R.id.allDoctors);
        allDoctors = new ArrayList<>();
        testAdapter = new AvaiableDoctorAdapter(this, allDoctors, thisPatient);




        rootRef = FirebaseDatabase.getInstance().getReference();


        rootRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                childRef = rootRef.child(dataSnapshot.getKey());
//                allPatients.add(patient);
                //TODO add doctor to list only if the current patient doesn't have him already
                testAdapter.add(doctor);
//                patientArrayAdapter.notifyDataSetChanged();
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


        listView.setAdapter(testAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor clicked = ((Doctor) parent.getItemAtPosition(position));
                patientRef = rootRef.child("Patients").child(thisPatient.getId());
//                patientRef = rootRef.getReference("Patients").child(thisPatient.getId());
//                doctorRef = rootRef.getReference("Doctors").child(clicked.getId());
                doctorRef = rootRef.child("Doctors").child(clicked.getId());
                patientRef.child("Doctors").child(clicked.getId()).setValue(clicked.getName()+ " " +clicked.getSurname());
                doctorRef.child("Patients").child(thisPatient.getId()).setValue(thisPatient.getId());
//                context.startActivity(intents.adminPatientView);
                Intent patientView = intents.adminPatientView;
                patientView.putExtra("thisPatient", thisPatient);
                context.startActivity(patientView);
                Toast toast = Toast.makeText(context, "Doctor added to patient", Toast.LENGTH_SHORT);
                toast.show();
                finish();

//                Toast toast = Toast.makeText(getApplicationContext(), clicked.getName(), Toast.LENGTH_SHORT);
//                toast.show();
            }
        });

    }


}
