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

    private DoctorPatientAdapter(Context context, ArrayList<String> myPatients) {
        super(context, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = (Patient) getItem(position);
//        return super.getView(position, convertView, parent);
//        Doctor doctorName = (Doctor) getItem(position);
//        String doctorId = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_patient_list, parent, false);
        }

        TextView patientId = convertView.findViewById(R.id.patientId);
        TextView patientName = convertView.findViewById(R.id.fullName);

        patientId.setText(patient.getId());
        patientName.setText(patient.getName());



        return convertView;

    }
}
