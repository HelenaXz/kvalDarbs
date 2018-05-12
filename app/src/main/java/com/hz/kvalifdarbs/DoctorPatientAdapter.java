package com.hz.kvalifdarbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Patient;

import java.util.ArrayList;

public class DoctorPatientAdapter extends ArrayAdapter<Object> {
    TextView patientId, patientName;

    public DoctorPatientAdapter(Context context, ArrayList<String> myPatients) {
        super(context, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = (Patient) getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_patient_list_adaptor, parent, false);
        }


        patientId = convertView.findViewById(R.id.patientId);
        patientName = convertView.findViewById(R.id.fullName);

        patientId.setText(patient.getId());
        patientName.setText(patient.getFullName());


        return convertView;

    }
}
