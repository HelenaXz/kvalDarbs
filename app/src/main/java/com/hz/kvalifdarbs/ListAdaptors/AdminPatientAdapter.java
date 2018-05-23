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
        Patient patient = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_patient_list_adaptor, parent, false);
        }
        tvName = convertView.findViewById(R.id.patientName);
        tvInfo = convertView.findViewById(R.id.someInfo);

        tvName.setText(patient.getFullName());
        String s = patient.getId()+"/"+patient.getRoom();
        tvInfo.setText(s);

        return convertView;
    }
}