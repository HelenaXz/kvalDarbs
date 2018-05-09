package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Intents extends AppCompatActivity {
    Intent mainMenu;
    Intent addDoctor;
    Intent addPatient;
    Intent allPatientList;
    Intent allDoctorList;
    Intent adminPatientView;
    Intent doctorPatientView;
    Intent loginView;
    Intent addAdmin;


    Intents(Context thisContext) {
        this.mainMenu = new Intent(thisContext, MainActivity.class);
        this.addDoctor = new Intent(thisContext, AddDoctorActivity.class);
        this.addPatient = new Intent(thisContext, AddPatientActivity.class);
        this.addAdmin = new Intent(thisContext, AddAdminActivity.class);
        this.allPatientList =  new Intent(thisContext, AllPatientListActivity.class);
        this.allDoctorList =  new Intent(thisContext, AllDoctorListActivity.class);
        this.adminPatientView = new Intent(thisContext, AdminViewPatientActivity.class);
        this.loginView = new Intent(thisContext, LoginActivity.class);

    }
}