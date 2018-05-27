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
import android.view.View;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        DatabaseReference rootRef, userRef;
        Context context;
        String userId, userName, userSurname, fullName, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hello");

        //Strings
        userType = PreferenceUtils.getUserType(context);
        userId  = PreferenceUtils.getId(context);
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = userName + " " + userSurname;
        //Firebase
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patients").child(userId);

        //Drawer menu
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        MethodHelper.setUpNavigationMenu(navigationView, userId, fullName, userType);

        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
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


}
