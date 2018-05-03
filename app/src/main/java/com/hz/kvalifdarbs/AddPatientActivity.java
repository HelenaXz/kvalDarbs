package com.hz.kvalifdarbs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class AddPatientActivity extends AppCompatActivity {

    DatabaseReference rootRef, demoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        final Intents intents = new Intents(this);

    }
}
