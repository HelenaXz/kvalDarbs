package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {

    EditText userIdView, userPassView;
    TextView userType;
    DatabaseReference rootRef, childRef;
    DataSnapshot userRef;
    String userIdString, userPassString, thisUserType;
    String passEncrypt, passFromDB;
    Context context;
    Intents intents;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        intents = new Intents(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        userType = findViewById(R.id.userTypeLogin);
        userIdView = findViewById(R.id.userId);
        userPassView = findViewById(R.id.userPass);
        Button login = findViewById(R.id.loginBtn);
        checkBox = (CheckBox) findViewById(R.id.checkRemeber);


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
                    passFromDB = userRef.child("password").getValue().toString();
                    String userName = userRef.child("name").getValue().toString();
                    String userSurname = userRef.child("surname").getValue().toString();
                    String phoneString = userRef.child("phone").getValue().toString();
                    String addedToSystem, birthDate, roomString;

                    passEncrypt = MethodHelper.sha1Hash(userPassString);

                    if(passFromDB.equals(passEncrypt)){
                        if(checkBox.isChecked()){
                            PreferenceUtils.saveId(userIdString, context);
                            PreferenceUtils.savePassword(passEncrypt, context);
                            PreferenceUtils.saveUserType(thisUserType, context);
                            PreferenceUtils.saveUserName(userName, context);
                            PreferenceUtils.saveUserSurname(userSurname, context);
                            PreferenceUtils.savePhoneNum(phoneString, context);
                            if(thisUserType.equals("Patient")){
                                addedToSystem = userRef.child("addedToSystem").getValue().toString();
                                birthDate = userRef.child("birthDate").getValue().toString();
                                roomString = userRef.child("room").getValue().toString();
                                PreferenceUtils.saveBirthDate(birthDate, context);
                                PreferenceUtils.saveAddedToSystem(addedToSystem, context);
                                PreferenceUtils.saveRoomNum(roomString, context);
                            }
                        }
                        Toast toast = Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = userTypeMainMenu;
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
