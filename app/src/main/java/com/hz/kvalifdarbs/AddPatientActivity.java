package com.hz.kvalifdarbs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.Objects.Patient;

import java.text.DateFormat;


public class AddPatientActivity extends AppCompatActivity {

    DatabaseReference rootRef, patientRef;
    EditText name, surname, id, phone, pass, passRepeat, roomNr;
    String nameString, idString,surnameString, phoneString, birthDate, roomString;
    TextView dateOfBirth;
    Integer passEncrypt, phoneNum;
    DatePickerDialog.OnDateSetListener mDataSetListener;
    int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);
        rootRef = FirebaseDatabase.getInstance().getReference("Patients");

        year = 1990;
        month = 0;
        day = 1;

        //set up spinners
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
        R.array.gender_list, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        //Buttons
        Button submitForm = findViewById(R.id.submit);
        //Text fields
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        id = findViewById(R.id.patientID);
        phone = findViewById(R.id.phoneNumber);
        pass = findViewById(R.id.password);
        passRepeat = findViewById(R.id.passwordRepeat);
        dateOfBirth = findViewById(R.id.birthYear);
        roomNr = findViewById(R.id.roomNr);

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
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
                month = monthPick + 1;
                String date = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "-"+ (month < 10 ? "0" : "") + month + "-" + yearPick;
                dateOfBirth.setText(date);
                year = yearPick;
                month = monthPick;
                day = dayOfMonth;
            }
        };

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    nameString = name.getText().toString();
                    surnameString = surname.getText().toString();
                    idString = id.getText().toString();
                    phoneString = phone.getText().toString();
                    birthDate= dateOfBirth.getText().toString();
                    phoneNum = Integer.parseInt(phoneString);
                    passEncrypt = pass.getText().toString().hashCode();
                    roomString = roomNr.getText().toString();

                    patientRef = rootRef.child(idString);
                    Patient newPatient = new Patient(nameString, surnameString, idString, passEncrypt, phoneNum, birthDate, roomString);
                    patientRef.setValue(newPatient);

                    Context context = getApplicationContext();
                    CharSequence text = "Patient added to DB";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
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
        if(pass.getText().toString().equals(passRepeat.getText().toString())){
            valid = valid + 1;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Passwords don't match!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if(valid < 7){ return false; } else { return true; }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
