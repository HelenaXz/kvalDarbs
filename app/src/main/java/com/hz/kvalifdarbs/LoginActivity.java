package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {

    EditText userIdView, userPassView;
    TextView userType;
    DatabaseReference rootRef, childRef;
    DataSnapshot userRef;
    String userIdString, userPassString, thisUserType;
    Integer passEncrypt, passFromDB;
    Context context;
    Intents intents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intents = new Intents(this);
        context = getApplicationContext();

        userIdView = findViewById(R.id.userId);
        userPassView = findViewById(R.id.userPass);
        Button login = findViewById(R.id.loginBtn);
        userType = findViewById(R.id.userTypeLogin);


        Intent i = getIntent();
        thisUserType = i.getStringExtra("UserType");
        String s = thisUserType + " Login";
        userType.setText(s);
        rootRef = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdString = userIdView.getText().toString();
                userPassString = userPassView.getText().toString();
                if(thisUserType.equals("Doctor")){
                    childRef = rootRef.child("Doctors");
                    attemptLogin(thisUserType, intents.doctorMainMenu);
                }

                if(thisUserType.equals("Patient")){
                    childRef = rootRef.child("Patients");
                    attemptLogin(thisUserType, intents.patientMainMenu);
                }

                if(thisUserType.equals("Administrator")){
                    childRef = rootRef.child("Admins");
                    attemptLogin(thisUserType, intents.adminMainMenu);
                }
            }
        });
    }

    public void attemptLogin(final String userTypeString, final Intent userTypeMainMenu){
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userIdString) && !userIdString.isEmpty()) {
                    userRef = dataSnapshot.child(userIdString);
                    passFromDB = Integer.parseInt(userRef.child("password").getValue().toString());

                    passEncrypt = userPassString.hashCode();
                    if(passFromDB.toString().equals(passEncrypt.toString())){
                        Toast toast = Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT);
                        toast.show();

                        PreferenceUtils.saveId(userIdString, context);
                        PreferenceUtils.savePassword(passEncrypt.toString(), context);
                        PreferenceUtils.saveUserType(thisUserType, context);
                        Intent intent = userTypeMainMenu;
//                        intent.putExtra("userId", userIdString);
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Password incorrect!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    if(userIdString.isEmpty()){
                        Toast toast = Toast.makeText(getApplicationContext(), "Enter user ID", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), userTypeString + "not found!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
