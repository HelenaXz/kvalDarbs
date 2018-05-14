package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hz.kvalifdarbs.utils.Constants;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class UserSelectActivity extends AppCompatActivity {
    Button admin, doctor, patient;
    Context context;
    SharedPreferences prefs, prefs2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intents intents = new Intents(this);
        context = getApplicationContext();
        prefs2 = PreferenceManager.getDefaultSharedPreferences(context);
        String userId = prefs2.getString(Constants.KEY_ID, "none");
        String testType = prefs2.getString(Constants.KEY_USER_TYPE, "none");


        if(!userId.equals("")){
            if(testType.equals("Administrator")){
                startActivity(intents.adminMainMenu);
            }
            if(PreferenceUtils.getUserType(context).equals("Doctor")){
                Intent intent = intents.doctorMainMenu;
                intent.putExtra("doctorId", userId);
                startActivity(intent);
            }
            if(PreferenceUtils.getUserType(context).equals("Patient")){
                Intent intent = intents.patientMainMenu;
                intent.putExtra("patientId", userId);
                startActivity(intent);
            }
        } else {

            setContentView(R.layout.activity_user_select);

            admin = findViewById(R.id.adminBtn);
            doctor = findViewById(R.id.doctorBtn);
            patient = findViewById(R.id.patientBtn);

            admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginAdmin = intents.loginView;
                    loginAdmin.putExtra("UserType", "Administrator");
                    startActivity(loginAdmin);
                }
            });
            doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginAdmin = intents.loginView;
                    loginAdmin.putExtra("UserType", "Doctor");
                    startActivity(loginAdmin);
                }
            });
            patient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginAdmin = intents.loginView;
                    loginAdmin.putExtra("UserType", "Patient");
                    startActivity(loginAdmin);
                }
            });
        }




    }
}
