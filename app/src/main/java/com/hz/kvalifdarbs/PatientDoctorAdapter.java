package com.hz.kvalifdarbs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.Objects.Patient;

import java.util.ArrayList;

public class PatientDoctorAdapter extends ArrayAdapter<String> {
    TextView doctorIdtv;

    public PatientDoctorAdapter(Context context) {
        super(context, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
//        Doctor doctorName = (Doctor) getItem(position);
        String doctorId = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_doctor_list, parent, false);
        }

        doctorIdtv = convertView.findViewById(R.id.doctorIdList);
        doctorIdtv.setText(doctorId);


        return convertView;

    }
}
