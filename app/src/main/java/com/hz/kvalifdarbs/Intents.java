package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class Intents extends AppCompatActivity {
    Intent adminMainMenu;
    Intent doctorMainMenu;
    Intent patientMainMenu;
    Intent addDoctor;
    Intent addPatient;
    Intent allPatientList;
    Intent avaiableDoctorList;
    Intent allDoctorList;
    Intent doctorPatientList;
    Intent adminPatientView;
    Intent adminDoctorView;
    Intent doctorPatientView;
    Intent loginView;
    Intent addAdmin;


    Intents(Context thisContext) {
        this.adminMainMenu = new Intent(thisContext, AdminMainActivity.class);
        this.doctorMainMenu = new Intent(thisContext, DoctorMainActivity.class);
        this.addDoctor = new Intent(thisContext, AddDoctorActivity.class);
        this.addPatient = new Intent(thisContext, AddPatientActivity.class);
        this.addAdmin = new Intent(thisContext, AddAdminActivity.class);
        this.allPatientList =  new Intent(thisContext, AllPatientListActivity.class);
        this.avaiableDoctorList =  new Intent(thisContext, AvaiableDoctorListActivity.class);
        this.adminPatientView = new Intent(thisContext, AdminViewPatientActivity.class);
        this.adminDoctorView = new Intent(thisContext, AdminViewDoctorActivity.class);
        this.loginView = new Intent(thisContext, LoginActivity.class);
        this.doctorPatientList = new Intent(thisContext, DoctorPatientListActivity.class);
        this.allDoctorList = new Intent(thisContext, AllDoctorListActivity.class);
        this.doctorPatientView = new Intent(thisContext, DoctorViewPatientActivity.class);
        this.patientMainMenu = new Intent(thisContext, PatientMainActivity.class);

    }
}