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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class AddUserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    Context context;
    String userId, userName, userSurname, fullName;
    Button addDoctorBtn, addPatientBtn, addAdminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select User to Add");
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        final Intents intents = new Intents(this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.inflateMenu(R.menu.admin_drawer);
        View headView2 = navigationView.getHeaderView(0);

        TextView headUserId = headView2.findViewById(R.id.headUserId);
        TextView headUserName = headView2.findViewById(R.id.headFullName);

        userId  = PreferenceUtils.getId(context);
        userName = PreferenceUtils.getUserName(context);
        userSurname = PreferenceUtils.getUserSurname(context);
        fullName = userName + " " + userSurname;
        headUserId.setText(userId);
        headUserName.setText(fullName);

        navigationView.setNavigationItemSelectedListener(this);

        //Buttons
        addDoctorBtn = findViewById(R.id.btnAddDoctor);
        addPatientBtn = findViewById(R.id.btnAddPatient);
        addAdminBtn = findViewById(R.id.btnAddAdmin);

        addAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.addAdmin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        addDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.addDoctor.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.addPatient.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}