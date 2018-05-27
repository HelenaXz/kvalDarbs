package com.hz.kvalifdarbs.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.ListAdaptors.AllPatientAdapter;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.util.ArrayList;

public class AllPatientListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    DatabaseReference rootRef, childRef;
    ArrayList<Patient> allPatients;
    AllPatientAdapter testAdapter;
    Context context;
    String userId, userName, userSurname, fullName, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patient_list);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        rootRef = FirebaseDatabase.getInstance().getReference();
        //Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("All Patients");

        //Strings
        userType = PreferenceUtils.getUserType(context);
        userId  = PreferenceUtils.getId(context);
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = userName + " " + userSurname;

        //Drawer menu
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        MethodHelper.setUpNavigationMenu(navigationView, userId, fullName, userType);

        navigationView.setNavigationItemSelectedListener(this);

        //ListView setup
        ListView listView = findViewById(R.id.allPatients);
        allPatients = new ArrayList<>();
        testAdapter = new AllPatientAdapter(this);

        getAllUsers();

        listView.setAdapter(testAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Patient clicked = ((Patient) parent.getItemAtPosition(position));
                Intent seePatient = intents.adminPatientView;
                seePatient.putExtra("thisPatient", clicked);
                startActivity(seePatient.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_add_user) {
            startActivity(intents.addUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_all_patients) {
            startActivity(intents.allPatientList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_all_doctors) {
            startActivity(intents.allDoctorList.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_logout) {
            MethodHelper.logOut(context, intents);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getAllUsers(){
        rootRef.child("Patients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Patient patient = dataSnapshot.getValue(Patient.class);
                childRef = rootRef.child(dataSnapshot.getKey());
                testAdapter.add(patient);
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
