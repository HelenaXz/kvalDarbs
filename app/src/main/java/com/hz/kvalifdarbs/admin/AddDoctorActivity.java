package com.hz.kvalifdarbs.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;

import java.util.ArrayList;

public class AddDoctorActivity extends AppCompatActivity {

    DatabaseReference rootRef, userRef;
    EditText name, surname, id, phone, pass, passRepeat;
    private String passEncrypt;
    Button submitForm;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase references
        rootRef = FirebaseDatabase.getInstance().getReference("Doctors");

        //TextViews, Buttons
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        id = findViewById(R.id.doctorID);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        passRepeat = findViewById(R.id.passwordRepeat);
        submitForm = findViewById(R.id.submitBtn);
        final ArrayList<String> existingUsers = MethodHelper.userExisting("Doctors");

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //get text from fields
                    String idString = id.getText().toString();
                    if(!existingUsers.contains(idString)) {
                        String nameString = name.getText().toString();
                        String surnameString = surname.getText().toString();

                        String phoneString = phone.getText().toString();
                        Integer phoneNum = Integer.parseInt(phoneString);
                        passEncrypt = MethodHelper.sha1Hash(pass.getText().toString());

                        userRef = rootRef.child(idString);
                        Doctor newDoctor = new Doctor(idString, nameString, passEncrypt, phoneNum, surnameString);
                        userRef.setValue(newDoctor);

                        MethodHelper.showToast(context, "Doctor added to DB");
                    } else {
                        MethodHelper.showToast(context, "Doctor with id exists");
                    }
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.addUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        });
    }

    public boolean validate(){
        int valid = 0;
        if(TextUtils.isEmpty(name.getText().toString().trim())){
            name.setError("Please enter doctors name");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(surname.getText().toString().trim())){
            surname.setError("Please enter doctors surname");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(id.getText().toString().trim())){
            id.setError("Please enter doctors ID number");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(phone.getText().toString().trim())){
            phone.setError("Please enter doctors phone number");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(pass.getText().toString().trim())){
            pass.setError("Please enter password");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(passRepeat.getText().toString().trim())){
            passRepeat.setError("Please repeat password");
        } else {valid = valid + 1;}
        if(pass.getText().toString().equals(passRepeat.getText().toString())) {
            valid = valid + 1;
        } else {
            MethodHelper.showToast(getApplicationContext(), "Passwords don't match!");
        }
        if(valid < 7) return false;
        else { return true; }
    }
    public void clearForm(View v){
        name.setText(null);
        surname.setText(null);
        id.setText(null);
        phone.setText(null);
        pass.setText(null);
        passRepeat.setText(null);
        //close keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
