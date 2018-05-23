package com.hz.kvalifdarbs.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.ListAdaptors.DoctorPatientAdapter;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.util.ArrayList;

public class DoctorPatientListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    ListView patientList;
    ArrayList<String> doctorPatients;
    DoctorPatientAdapter testAdapter;
    Context context;
    DatabaseReference rootRef, childRef;
    String userId, userName, userSurname, fullName;
    TextView emptyElement;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_list);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId  = PreferenceUtils.getId(context);
        //Firebase reference
        rootRef = FirebaseDatabase.getInstance().getReference();
        childRef = rootRef.child("Doctors").child(userId);


        //Strings
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = "Dr. " + userName + " " + userSurname;


        //Set up ListView
        patientList = findViewById(R.id.patientList);
        doctorPatients = new ArrayList<>(); //list of doctor patient id's
        testAdapter = new DoctorPatientAdapter(this);
        patientList.setAdapter(testAdapter);

        emptyElement = findViewById(R.id.emptyElement);
        TextView emptyText = findViewById(android.R.id.empty);
        patientList.setEmptyView(emptyText);

        //Drawer menu
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.doctor_drawer);
        View headView = navigationView.getHeaderView(0);
        TextView headUserName = headView.findViewById(R.id.headFullName);
        TextView headUserId = headView.findViewById(R.id.headUserId);

        navigationView.setNavigationItemSelectedListener(this);

        headUserId.setText(userId);
        headUserName.setText(fullName);


        childRef.child("Patients").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {

                   for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                       //Loop 1 to go through all the child nodes of users
                       String patientId = uniqueKeySnapshot.getKey();
                       doctorPatients.add(patientId);
                   }
                   Integer testAdapterSize = doctorPatients.size();
                   if(testAdapterSize==0){
                       String emptyText = "There are no patients assigned to you.";
                       emptyElement.setText(emptyText);
                       patientList.setEmptyView(emptyElement);
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });


        rootRef.child("Patients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (String curr : doctorPatients) if (curr.equals(dataSnapshot.getKey())){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    testAdapter.add(patient);
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

        patientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient clicked = ((Patient) parent.getItemAtPosition(position));
                Intent seePatient = intents.doctorPatientView;
                seePatient.putExtra("thisPatient", clicked);
                startActivity(seePatient.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_my_patients) {
            Intent intent = intents.doctorPatientList;
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_profile) {
            startActivity(intents.doctorMainMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_logout) {
            MethodHelper.logOut(context, intents);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
