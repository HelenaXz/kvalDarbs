package com.hz.kvalifdarbs.patient;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.ListAdaptors.DeviceListAdapter;
import com.hz.kvalifdarbs.admin.AdminMainActivity;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.util.ArrayList;

public class PatientMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    DatabaseReference rootRef, userRef;
    String userId, userName, userSurname, fullName;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();


        //Drawer menu
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.patient_drawer);
        View headView = navigationView.getHeaderView(0);
        TextView headUserName = headView.findViewById(R.id.headFullName);
        TextView headUserId = headView.findViewById(R.id.headUserId);

        navigationView.setNavigationItemSelectedListener(this);


        context = getApplicationContext();
        userId  = PreferenceUtils.getId(context);
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = userName + " " + userSurname;
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patients").child(userId);

        headUserId.setText(userId);
        headUserName.setText(fullName);

        //Firebase
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Patients").child(userId);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

    }//End of OnCreate

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intents intents = new Intents(this);

        if (id == R.id.nav_my_doctors) {
//            startActivity(intents.addUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            startActivity(intents.patientDoctorListView.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            //TODO
        } else if (id == R.id.nav_pat_movements) {
            //TODO
        } else if (id == R.id.nav_pat_exams) {
            //TODO
        } else if (id == R.id.nav_profile) {
            final String currDbPassString = PreferenceUtils.getPassword(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(PatientMainActivity.this);
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
        } else if (id == R.id.nav_BT_device){
            //TODO
        } else if (id == R.id.nav_logout) {
            MethodHelper.logOut(context, intents);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}