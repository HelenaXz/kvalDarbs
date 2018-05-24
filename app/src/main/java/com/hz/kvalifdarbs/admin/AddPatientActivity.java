package com.hz.kvalifdarbs.admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;

import java.text.DateFormat;
import java.util.ArrayList;


public class AddPatientActivity extends AppCompatActivity {

    DatabaseReference rootRef, userRef;
    EditText name, surname, id, phone, pass, passRepeat, roomNr;
    String nameString, idString,surnameString, phoneString, birthDate, roomString, genderString, passEncrypt;
    Spinner genderSpinner;
    TextView dateOfBirth, moveMinTime;
    Integer phoneNum;
    DatePickerDialog.OnDateSetListener mDataSetListener;
    TimePickerDialog.OnTimeSetListener mTimeSetListener;
    Context context;
    String timeSave;
    int year, month, day, hour, minute1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Firebase reference
        rootRef = FirebaseDatabase.getInstance().getReference("Patients");

        //starting date for date picker
        year = 1990;
        month = 0;
        day = 1;
        hour = 0;
        minute1 = 0;

        //set up spinners
        genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
        R.array.gender_list, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        //TextViews, Buttons
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        id = findViewById(R.id.patientID);
        phone = findViewById(R.id.phoneNumber);
        pass = findViewById(R.id.password);
        passRepeat = findViewById(R.id.passwordRepeat);
        dateOfBirth = findViewById(R.id.birthYear);
        roomNr = findViewById(R.id.roomNr);
        moveMinTime = findViewById(R.id.moveMinTime);

        Button submitForm = findViewById(R.id.submitBtn);
        final ArrayList<String> existingUsers = MethodHelper.userExisting("Patients");

        dateOfBirth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                DatePickerDialog dialog = new DatePickerDialog(AddPatientActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDataSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearPick, int monthPick, int dayOfMonth) {

                month = monthPick + 1;
                String date = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"+ (month < 10 ? "0" : "") + month + "-" + yearPick;
                dateOfBirth.setText(date);
                year = yearPick;
                month = monthPick;
                day = dayOfMonth;
            }
        };

        moveMinTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog2 = new TimePickerDialog(AddPatientActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mTimeSetListener, hour, minute1, true );
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeSave = (hourOfDay < 10 ? "0" : "") + hourOfDay + ":" +(minute < 10 ? "0" : "")+ minute;
                String timeString = "Move every " + hourOfDay + " h " + minute + "min";
                hour = hourOfDay;
                minute1 = minute;
                moveMinTime.setText(timeString);
            }
        };


        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //TODO Validate if moveTime and birthDate are set
                    idString = id.getText().toString();
                    if(!existingUsers.contains(idString)) {
                        nameString = name.getText().toString();
                        surnameString = surname.getText().toString();

                        phoneString = phone.getText().toString();
                        birthDate= dateOfBirth.getText().toString();
                        phoneNum = Integer.parseInt(phoneString);
                        passEncrypt = MethodHelper.sha1Hash(pass.getText().toString());
                        roomString = roomNr.getText().toString();
                        genderString = genderSpinner.getSelectedItem().toString();

                        userRef = rootRef.child(idString);
                        Patient newPatient = new Patient(nameString, surnameString, idString, genderString, passEncrypt, phoneNum, birthDate, roomString, timeSave);
                        userRef.setValue(newPatient);

                        Toast toast = Toast.makeText(context, "Patient added to DB", Toast.LENGTH_SHORT);
                        toast.show();
                        clearForm(v);
                    } else {
                        Toast toast = Toast.makeText(context, "Patient with id exists", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.addUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }


    public boolean validate(){
        int valid = 0;
        if(TextUtils.isEmpty(name.getText().toString().trim())){
            name.setError("Please enter patients name");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(surname.getText().toString().trim())){
            surname.setError("Please enter patients surname");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(id.getText().toString().trim())){
            id.setError("Please enter patients ID number");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(phone.getText().toString().trim())){
            phone.setError("Please enter patients phone number");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(pass.getText().toString().trim())){
            pass.setError("Please enter password");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(passRepeat.getText().toString().trim())){
            passRepeat.setError("Please repeat password");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(roomNr.getText().toString().trim())){
            name.setError("Please enter room");
        } else {valid = valid + 1;}
        if(pass.getText().toString().equals(passRepeat.getText().toString())){
            valid = valid + 1;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Passwords don't match!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if(valid < 8){ return false; } else { return true; }
    }
    public void clearForm(View v){
        //Clear Form
        name.setText(null);
        surname.setText(null);
        id.setText(null);
        phone.setText(null);
        pass.setText(null);
        passRepeat.setText(null);
        dateOfBirth.setText(null);
        moveMinTime.setText(null);
        roomNr.setText(null);
        year = 1990;
        month = 0;
        day = 1;
        //Close keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
