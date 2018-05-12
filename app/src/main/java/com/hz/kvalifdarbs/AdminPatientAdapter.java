package com.hz.kvalifdarbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Patient;

import java.util.ArrayList;

class AdminPatientAdapter extends ArrayAdapter<Patient> {
    TextView tvName, tvSurname;

    public AdminPatientAdapter(Context context, ArrayList<Patient> allPatients) {
        super(context, 0, allPatients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Patient patient = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_patient_list_adaptor, parent, false);
        }
        // Lookup view for data population
        tvName = (TextView) convertView.findViewById(R.id.patientName);
        tvSurname = (TextView) convertView.findViewById(R.id.patientSurname);
//        Button roomField = convertView.findViewById(R.id.room);
        // Populate the data into the template view using the data object
//        roomField.setText(patient.getBirthDate());
        tvName.setText(patient.getName());
        tvSurname.setText(patient.getSurname());
        // Return the completed view to render on screen
        return convertView;
    }
}