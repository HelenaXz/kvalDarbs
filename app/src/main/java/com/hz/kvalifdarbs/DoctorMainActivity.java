package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveId("", context);
                PreferenceUtils.savePassword("", context);
                PreferenceUtils.saveUserType("", context);
                startActivity(intents.userSelect);
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
                        String newPassw, newPasswRep;
                        newPassw = newPass.getText().toString();
                        newPasswRep = newPassRepeat.getText().toString();
                        Integer currPass = currentPass.getText().toString().hashCode();
                        String currPassString = currPass.toString();
                        if(currPassString.equals(currDbPassString)){
                            if(newPassw.equals(newPasswRep)){
                                Integer newPassInt = newPassw.hashCode();
                                userRef.child("password").setValue(newPassInt);
                                PreferenceUtils.savePassword(newPassInt.toString(), context);
                                changePassDialog.dismiss();
                            } else {
                                String text = "New passwords do not match!";
                                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            String text = "Current password does not match Database";
                            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                            toast.show();
                        }
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
