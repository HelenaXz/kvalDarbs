package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
        DatabaseReference rootRef, demoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);


        rootRef = FirebaseDatabase.getInstance().getReference("TestUsers");
        //Buttons
        Button addToDB = findViewById(R.id.addDBBtn);
        Button addDoctorBtn = findViewById(R.id.btnAddDoctor);
        Button addPatientBtn = findViewById(R.id.btnAddPatient);
        Button viewPatientList = findViewById(R.id.btnViewPatients);

        addToDB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

//                demoRef = rootRef.child("User2");
//                Doctor testDoc = new Doctor("Andris", "Cielava", "parole1234", 12345568);
//                demoRef.setValue(testDoc);

                Context context = getApplicationContext();
                CharSequence text = "Doc added to DB!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        addDoctorBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(intents.addDoctor);
            }
        });

        addPatientBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(intents.addPatient);
            }
        });

        viewPatientList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(intents.patientList);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater()
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
