package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class UserSelectActivity extends AppCompatActivity {
    Button admin, doctor, patient;
    Context context;
    String userId, userType, loggedIn;
    Intents intents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intents = new Intents(this);
        context = getApplicationContext();
        userId = PreferenceUtils.getId(context);
        userType = PreferenceUtils.getUserType(context);
        loggedIn = PreferenceUtils.getLoggedIn(context);


        if (!loggedIn.equals("true")) {
            setContentView(R.layout.activity_user_select);
            admin = findViewById(R.id.adminBtn);
            doctor = findViewById(R.id.doctorBtn);
            patient = findViewById(R.id.patientBtn);

            admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUser(intents.loginView, "Administrator");
                }
            });
            doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUser(intents.loginView, "Doctor");
                }
            });
            patient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUser(intents.loginView, "Patient");
                }
            });

        } else {
            loggedIn(PreferenceUtils.getUserType(context));
        }
    }

    private void startUser(Intent intent, String userType) {
        intent.putExtra("UserType", userType);
        startActivity(intent);
    }


    public void loggedIn(String userType) {
        if (userType.equals("Administrator")) {
            startActivity(intents.adminMainMenu);
        }
        if (userType.equals("Doctor")) {
            Intent intent = intents.doctorMainMenu;
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
        if (userType.equals("Patient")) {
            Intent intent = intents.patientMainMenu;
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }
}
