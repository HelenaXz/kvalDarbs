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
        import android.widget.ListView;
        import android.widget.TextView;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.hz.kvalifdarbs.ListAdaptors.PatientMovementAdapter;
        import com.hz.kvalifdarbs.Objects.Movement;
        import com.hz.kvalifdarbs.R;
        import com.hz.kvalifdarbs.utils.Intents;
        import com.hz.kvalifdarbs.utils.MethodHelper;
        import com.hz.kvalifdarbs.utils.PreferenceUtils;

        import java.util.ArrayList;

public class PatientMovementListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    Context context;
    String userId, userName, userSurname, fullName, userType;
    DatabaseReference rootRef, userRef;
    TextView emptyElement;
    ListView movementList;
    ArrayList<String> patientExams;
    PatientMovementAdapter testAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_thing_list);
        context = getApplicationContext();
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Patient Movements");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Strings
        userId  = PreferenceUtils.getId(context);
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        userType = PreferenceUtils.getUserType(context);
        fullName = userName + " " + userSurname;

        //Firebase references
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patients").child(userId);

        //Set up list
        emptyElement = findViewById(R.id.emptyElement);
        movementList = findViewById(R.id.thingList);
        patientExams = new ArrayList<>(); //list of doctor patient id's
        testAdapter = new PatientMovementAdapter(this);
        movementList.setAdapter(testAdapter);

        TextView emptyText = findViewById(android.R.id.empty);
        movementList.setEmptyView(emptyText);


        //Drawer menu
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        MethodHelper.setUpNavigationMenu(navigationView, userId, fullName, userType);

        navigationView.setNavigationItemSelectedListener(this);

        Integer testAdapterSize = testAdapter.getCount();
        if(testAdapterSize==0){
            String emptyTextString = "This patient has no movements";
            emptyElement.setText(emptyTextString);
            movementList.setEmptyView(emptyElement);
        }

        userRef.child("Movements").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                testAdapter.insert(movement, 0);
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

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_my_doctors) {
            startActivity(intents.patientDoctorListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_pat_movements) {
            startActivity(intents.patientMovementListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_pat_exams) {
            startActivity(intents.patientExamListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        } else if (id == R.id.nav_profile) {
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

    public void setUpNavigationMenu(NavigationView navigationView){
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.patient_drawer);

        View headView = navigationView.getHeaderView(0);
        TextView headUserName = headView.findViewById(R.id.headFullName);
        TextView headUserId = headView.findViewById(R.id.headUserId);
        headUserId.setText(userId);
        headUserName.setText(fullName);

        navigationView.setNavigationItemSelectedListener(this);
    }


}

