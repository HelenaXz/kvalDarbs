package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Intents extends AppCompatActivity {
    Intent mainMenu;
    Intent addDoctor;
    Intent addPatient;
    Intent patientList;
    Intent patientView;
    Intent loginView;
    Intent addAdmin;


    Intents(Context thisContext) {
        this.mainMenu = new Intent(thisContext, MainActivity.class);
        this.addDoctor = new Intent(thisContext, AddDoctorActivity.class);
        this.addPatient = new Intent(thisContext, AddPatientActivity.class);
        this.addAdmin = new Intent(thisContext, AddAdminActivity.class);
        this.patientList =  new Intent(thisContext, PatientListActivity.class);
        this.patientView = new Intent(thisContext, ViewPatientActivity.class);
        this.loginView = new Intent(thisContext, LoginActivity.class);

    }
}