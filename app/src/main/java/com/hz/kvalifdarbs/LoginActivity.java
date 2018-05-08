package com.hz.kvalifdarbs;

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

public class LoginActivity extends AppCompatActivity {

    EditText userId, userPass;
    Button login;
    TextView userType;
    DatabaseReference rootRef, childRef;
    DataSnapshot userRef;
    String userIdString, userPassString, thisUserType, added;
    Integer passEncrypt, passFromDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);

        userId = findViewById(R.id.userId);
        userPass = findViewById(R.id.userPass);
        login = findViewById(R.id.loginBtn);
        userType = findViewById(R.id.userTypeLogin);


        Intent i = getIntent();
        thisUserType = i.getStringExtra("UserType");
        added = i.getStringExtra("UserType") + " Login";
        userType.setText(added);

        rootRef = FirebaseDatabase.getInstance().getReference();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdString = userId.getText().toString();
                userPassString = userPass.getText().toString();
                if(thisUserType.equals("Doctor")){
                    childRef = rootRef.child("Doctors");
                    childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(userIdString)) {
                                userRef = dataSnapshot.child(userIdString);
                                passFromDB = Integer.parseInt(userRef.child("password").getValue().toString());

                                passEncrypt = userPassString.hashCode();
                                if(passFromDB.toString().equals(passEncrypt.toString())){
                                    Toast toast = Toast.makeText(getApplicationContext(), "Login Succesful", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Password incorect!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Doctor not found!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                if(thisUserType.equals("Patient")){
                    childRef = rootRef.child("Patients");
                    childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(userIdString)) {
                                userRef = dataSnapshot.child(userIdString);
                                passFromDB = Integer.parseInt(userRef.child("password").getValue().toString());

                                passEncrypt = userPassString.hashCode();
                                if(passFromDB.toString().equals(passEncrypt.toString())){
                                    Toast toast = Toast.makeText(getApplicationContext(), "Login Succesful", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Password incorect!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Patient not found!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

//        Log.i("USER", userIdString);
//        if (dataSnapshot.hasChild(userIdString)) {
//            Toast toast = Toast.makeText(getApplicationContext(), "Doctor found", Toast.LENGTH_SHORT);
//            toast.show();
//        } else {
//            Toast toast = Toast.makeText(getApplicationContext(), "Doctor NOT found", Toast.LENGTH_SHORT);
//            toast.show();
//        }



    }
}
