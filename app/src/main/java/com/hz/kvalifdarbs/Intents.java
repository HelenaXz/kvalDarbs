package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Intents extends AppCompatActivity {
    Intent mainMenu;
    Intent addDoctor;
    Intent addPatient;


    Intents(Context thisContext) {
        this.mainMenu = new Intent(thisContext, MainActivity.class);
        this.addDoctor = new Intent(thisContext, AddDoctorActivity.class);
        this.addPatient = new Intent(thisContext, AddPatientActivity.class);

    }
}