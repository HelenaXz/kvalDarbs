

package com.hz.kvalifdarbs;

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

public class AllDoctorListActivity extends AppCompatActivity {
    DatabaseReference rootRef, childRef;
    ArrayList<Doctor> allDoctors;
    private ListView listView;
    DoctorAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctor_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);
        Intent i = getIntent();
        Patient thisPatient = (Patient)i.getSerializableExtra("thisPatient");

        listView = findViewById(R.id.allDoctors);
        allDoctors = new ArrayList<>();
        testAdapter = new DoctorAdapter(this, allDoctors, thisPatient);


        rootRef = FirebaseDatabase.getInstance().getReference();


        rootRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                childRef = rootRef.child(dataSnapshot.getKey());
//                allPatients.add(patient);
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

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Doctor clicked = ((Doctor) parent.getItemAtPosition(position));
//
//                Toast toast = Toast.makeText(getApplicationContext(), clicked.getName(), Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });

    }


}
