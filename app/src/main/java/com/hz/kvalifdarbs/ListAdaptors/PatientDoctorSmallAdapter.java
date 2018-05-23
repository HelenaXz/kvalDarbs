package com.hz.kvalifdarbs.ListAdaptors;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.smallDoctor;
import com.hz.kvalifdarbs.R;

public class PatientDoctorSmallAdapter extends ArrayAdapter<smallDoctor> {

    public PatientDoctorSmallAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        smallDoctor doctor = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_doctor_list_small_adaptor, parent, false);
        }

        TextView doctorIdtv = convertView.findViewById(R.id.doctorIdList);
        doctorIdtv.setText(doctor.getName());

        return convertView;
    }
}
