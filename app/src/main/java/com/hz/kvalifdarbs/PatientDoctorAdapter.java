package com.hz.kvalifdarbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PatientDoctorAdapter extends ArrayAdapter<String> {
    TextView doctorIdtv;

    public PatientDoctorAdapter(Context context) {
        super(context, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String doctorId = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_doctor_list_adaptor, parent, false);
        }

        doctorIdtv = convertView.findViewById(R.id.doctorIdList);
        doctorIdtv.setText(doctorId);


        return convertView;

    }
}
