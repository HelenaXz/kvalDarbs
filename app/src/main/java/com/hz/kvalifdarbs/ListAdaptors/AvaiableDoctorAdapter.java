package com.hz.kvalifdarbs.ListAdaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.R;

import java.util.ArrayList;

public class AvaiableDoctorAdapter extends ArrayAdapter<Doctor> {
    Context context;
    String patientId;
    Doctor doctor;
    FirebaseDatabase rootRef;
    Intents intents;
    Intent i;
    TextView fullName;


    public AvaiableDoctorAdapter(Context context, ArrayList<Doctor> allDoctors) {
        super(context, 0, allDoctors);
        this.context = context;
        rootRef = FirebaseDatabase.getInstance();
        intents = new Intents(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        doctor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.avaiable_doctor_list_adaptor, parent, false);
        }
        // Lookup view for data population
        fullName =  convertView.findViewById(R.id.doctorFullName);
//        Populate the data into the template view using the data object

        fullName.setText(doctor.getFullName());

        // Return the completed view to render on screen
        return convertView;
    }
}