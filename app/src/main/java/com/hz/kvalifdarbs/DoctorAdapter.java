package com.hz.kvalifdarbs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.Objects.Patient;

import java.io.Console;
import java.util.ArrayList;

class DoctorAdapter extends ArrayAdapter<Doctor> {
    Context context;
    String patientId;
    Doctor doctor;
    DatabaseReference patientRef, doctorRef;
    FirebaseDatabase rootRef;
    Intents intents;
    Intent i;
    Patient thisPatient;


    public DoctorAdapter(Context context, ArrayList<Doctor> allDoctors, Patient thisPatient) {
        super(context, 0, allDoctors);
        this.context = context;
        rootRef = FirebaseDatabase.getInstance();
        intents = new Intents(context);
        this.thisPatient = thisPatient;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //TODO Don't show the doctors in the list that the patient has
        // Get the data item for this position
        doctor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_doctor_list_add_button, parent, false);
        }
        // Lookup view for data population
        TextView fullName =  convertView.findViewById(R.id.doctorFullName);
        TextView addDoctor = convertView.findViewById(R.id.addBtn);
//        Button roomField = convertView.findViewById(R.id.room);
//        Populate the data into the template view using the data object

        String fullNameString = doctor.getName() + " " + doctor.getSurname();
        fullName.setText(fullNameString);
        addDoctor.setFocusable(false);
        addDoctor.setClickable(false);


        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.find patient by Id the the database


            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}