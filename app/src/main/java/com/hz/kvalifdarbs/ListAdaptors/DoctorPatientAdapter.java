package com.hz.kvalifdarbs.ListAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;


public class DoctorPatientAdapter extends ArrayAdapter<Object> {

    public DoctorPatientAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Patient patient = (Patient) getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_patient_list_adaptor, parent, false);
        }

        TextView patientId = convertView.findViewById(R.id.patientId);
        TextView patientName = convertView.findViewById(R.id.fullName);

        patientId.setText(patient.getId());
        patientName.setText(patient.getFullName());


        return convertView;

    }
}
