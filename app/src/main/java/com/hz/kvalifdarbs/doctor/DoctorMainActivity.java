package com.hz.kvalifdarbs.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.patient.PatientMainActivity;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class DoctorMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    Context context;
    String userId, userName, userSurname, fullName;
    DatabaseReference rootRef, userRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        userId  = PreferenceUtils.getId(context);

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Doctors").child(userId);


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


        context = getApplicationContext();
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = "Dr. " + userName + " " + userSurname;
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patients").child(userId);

        headUserId.setText(userId);
        headUserName.setText(fullName);



    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_my_patients) {
            Intent intent = intents.doctorPatientList;
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_profile) {
            final String currDbPassString = PreferenceUtils.getPassword(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorMainActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_pass_change, null);
            final EditText currentPass, newPass, newPassRepeat;
            currentPass = mView.findViewById(R.id.oldPass);
            newPass = mView.findViewById(R.id.newPass);
            newPassRepeat = mView.findViewById(R.id.newPassRep);

            Button changePass = mView.findViewById(R.id.btnChangePass);
            Button cancel = mView.findViewById(R.id.btnCancel);

            builder.setView(mView);
            final AlertDialog changePassDialog = builder.create();
            changePassDialog.show();
            changePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MethodHelper.changePassword(newPass, newPassRepeat, currentPass, currDbPassString, changePassDialog, context, userRef);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassDialog.dismiss();
                }
            });
        } else if (id == R.id.nav_logout) {
            MethodHelper.logOut(context, intents);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

