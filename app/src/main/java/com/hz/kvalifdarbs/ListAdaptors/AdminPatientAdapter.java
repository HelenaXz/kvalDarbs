package com.hz.kvalifdarbs.ListAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;

public class AdminPatientAdapter extends ArrayAdapter<Patient> {
    TextView tvName, tvInfo;

    public AdminPatientAdapter(Context context) {
        super(context, 0);
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
        tvInfo = (TextView) convertView.findViewById(R.id.someInfo);
        // Populate the data into the template view using the data object
        tvName.setText(patient.getFullName());
        String s = patient.getId()+"/"+patient.getRoom();
        tvInfo.setText(s);
        // Return the completed view to render on screen
        return convertView;
    }
}