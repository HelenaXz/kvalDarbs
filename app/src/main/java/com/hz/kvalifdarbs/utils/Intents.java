package com.hz.kvalifdarbs.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.hz.kvalifdarbs.admin.AddAdminActivity;
import com.hz.kvalifdarbs.admin.AddDoctorActivity;
import com.hz.kvalifdarbs.admin.AddPatientActivity;
import com.hz.kvalifdarbs.admin.AddUserActivity;
import com.hz.kvalifdarbs.admin.AdminMainActivity;
import com.hz.kvalifdarbs.admin.AdminViewDoctorActivity;
import com.hz.kvalifdarbs.admin.AdminViewPatientActivity;
import com.hz.kvalifdarbs.admin.AllDoctorListActivity;
import com.hz.kvalifdarbs.admin.AllPatientListActivity;
import com.hz.kvalifdarbs.admin.AvailableDoctorListActivity;
import com.hz.kvalifdarbs.doctor.DoctorMainActivity;
import com.hz.kvalifdarbs.doctor.DoctorPatientListActivity;
import com.hz.kvalifdarbs.doctor.DoctorViewPatientActivity;
import com.hz.kvalifdarbs.LoginActivity;
import com.hz.kvalifdarbs.patient.PatientDoctorListActivity;
import com.hz.kvalifdarbs.patient.PatientExamListActivity;
import com.hz.kvalifdarbs.patient.PatientMainActivity;
import com.hz.kvalifdarbs.UserSelectActivity;

public class Intents extends AppCompatActivity {
    public Intent adminMainMenu, doctorMainMenu, patientMainMenu;
    public Intent addUser, addDoctor, addPatient, addAdmin;
    public Intent allPatientList, allDoctorList;
    public Intent availableDoctorList;
    public Intent doctorPatientList;
    public Intent adminPatientView;
    public Intent adminDoctorView;
    public Intent doctorPatientView;
    public Intent patientDoctorListView;
    public Intent patientDeviceManage;
    public Intent patientExamListView;
    public Intent loginView;
    public Intent userSelect;


    public Intents(Context thisContext) {
        this.adminMainMenu = new Intent(thisContext, AdminMainActivity.class);
        this.doctorMainMenu = new Intent(thisContext, DoctorMainActivity.class);
        this.addDoctor = new Intent(thisContext, AddDoctorActivity.class);
        this.addPatient = new Intent(thisContext, AddPatientActivity.class);
        this.addAdmin = new Intent(thisContext, AddAdminActivity.class);
        this.allPatientList =  new Intent(thisContext, AllPatientListActivity.class);
        this.availableDoctorList =  new Intent(thisContext, AvailableDoctorListActivity.class);
        this.adminPatientView = new Intent(thisContext, AdminViewPatientActivity.class);
        this.adminDoctorView = new Intent(thisContext, AdminViewDoctorActivity.class);
        this.loginView = new Intent(thisContext, LoginActivity.class);
        this.doctorPatientList = new Intent(thisContext, DoctorPatientListActivity.class);
        this.allDoctorList = new Intent(thisContext, AllDoctorListActivity.class);
        this.doctorPatientView = new Intent(thisContext, DoctorViewPatientActivity.class);
        this.patientMainMenu = new Intent(thisContext, PatientMainActivity.class);
        this.userSelect = new Intent(thisContext, UserSelectActivity.class);
        this.addUser = new Intent(thisContext, AddUserActivity.class);
        this.patientDoctorListView = new Intent(thisContext, PatientDoctorListActivity.class);
        this.patientDeviceManage = new Intent(thisContext, ConnectDeviceActivity.class);
        this.patientExamListView = new Intent(thisContext, PatientExamListActivity.class);
    }
}