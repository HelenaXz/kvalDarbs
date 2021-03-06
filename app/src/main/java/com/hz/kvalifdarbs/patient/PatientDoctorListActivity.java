package com.hz.kvalifdarbs.patient;

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
import com.hz.kvalifdarbs.ListAdaptors.PatientDoctorAdapter;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.util.ArrayList;

public class PatientDoctorListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    ListView doctorList;
    ArrayList<String> patientDoctors;
    PatientDoctorAdapter testAdapter;
    Context context;
    DatabaseReference rootRef, childRef;
    String userType;
    TextView emptyElement;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_thing_list);
        context = getApplicationContext();
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Strings
        userType = PreferenceUtils.getUserType(context);
        String userId  = PreferenceUtils.getId(context);
        String userName = PreferenceUtils.getUserName(context);
        String userSurname = PreferenceUtils.getUserSurname(context);
        String fullName = userName + " " + userSurname;

        //Firebase references
        rootRef = FirebaseDatabase.getInstance().getReference();
        childRef = rootRef.child("Patients").child(userId);


        //Set up list
        emptyElement = findViewById(R.id.emptyElement);
        doctorList = findViewById(R.id.thingList);
        patientDoctors = new ArrayList<>(); //list of doctor patient id's
        testAdapter = new PatientDoctorAdapter(this);
        doctorList.setAdapter(testAdapter);

        TextView emptyText = findViewById(android.R.id.empty);
        doctorList.setEmptyView(emptyText);


        //Drawer menu
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MethodHelper.setUpNavigationMenu(navigationView, userId, fullName, userType);

        navigationView.setNavigationItemSelectedListener(this);

        //
        getPatientDoctors();
        showPatientDoctors();

    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_my_doctors) {
            startActivity(intents.patientDoctorListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_pat_exams) {
            startActivity(intents.patientExamListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_pat_movements) {
            //TODO
        }else if (id == R.id.nav_profile) {
            startActivity(intents.patientMainMenu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_BT_device){
            startActivity(intents.patientDeviceManage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_logout) {
            MethodHelper.logOut(context, intents);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getPatientDoctors(){
        childRef.child("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    //Loop 1 to go through all the child nodes of users
                    String doctorId = uniqueKeySnapshot.getKey();
                    patientDoctors.add(doctorId);
                }
                Integer testAdapterSize = patientDoctors.size();
                if(testAdapterSize==0){
                    String emptyText = "There are no doctors assigned to you.";
                    emptyElement.setText(emptyText);
                    doctorList.setEmptyView(emptyElement);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showPatientDoctors(){
        rootRef.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (String curr : patientDoctors) {
                    if (curr.equals(dataSnapshot.getKey())){
                        Doctor doctor = dataSnapshot.getValue(Doctor.class);
                        testAdapter.add(doctor);
                    }

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

}
