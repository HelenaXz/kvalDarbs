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
    String userId, userName, userSurname, fullName, phoneNum, userType;
    DatabaseReference rootRef, userRef;
    TextView fullNameTV, userIdTV, phoneNumTV;
    Button changePassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_menu);
        context = getApplicationContext();
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId  = PreferenceUtils.getId(context);

        //Firebase
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Doctors").child(userId);

        //Strings
        userType = PreferenceUtils.getUserType(context);
        phoneNum = PreferenceUtils.getPhoneNum(context);
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = "Dr. " + userName + " " + userSurname;

        //Text Views, Buttons
        fullNameTV = findViewById(R.id.fullName);
        userIdTV = findViewById(R.id.userId);
        phoneNumTV = findViewById(R.id.phoneNum);
        changePassword = findViewById(R.id.changePassword);

        fullNameTV.setText(fullName);
        userIdTV.append(userId);
        phoneNumTV.append(phoneNum);

        //Drawer menu
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        MethodHelper.setUpNavigationMenu(navigationView, userId, fullName, userType);

        navigationView.setNavigationItemSelectedListener(this);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

