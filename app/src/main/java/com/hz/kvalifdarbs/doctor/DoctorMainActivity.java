package com.hz.kvalifdarbs.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class DoctorMainActivity extends AppCompatActivity {
    Button viewAllPat, btnLogout, btnChangePass;
    String doctorId;
    Context context;
    DatabaseReference rootRef, userRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        doctorId  = PreferenceUtils.getId(context);

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Doctors").child(doctorId);

        viewAllPat = findViewById(R.id.btnViewPatients);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePass = findViewById(R.id.btnChangePassword);

        viewAllPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = intents.doctorPatientList;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveId("", context);
                PreferenceUtils.savePassword("", context);
                PreferenceUtils.saveUserType("", context);
                startActivity(intents.userSelect.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });


        btnChangePass.setOnClickListener(new View.OnClickListener() {
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


}

