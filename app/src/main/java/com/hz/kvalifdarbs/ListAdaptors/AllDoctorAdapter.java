package com.hz.kvalifdarbs.ListAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.R;

public class AllDoctorAdapter extends ArrayAdapter<Doctor> {

    public AllDoctorAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Doctor doctor = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_doctor_list_adaptor, parent, false);
        }

        TextView doctorName = convertView.findViewById(R.id.fullName);
        TextView someInfo = convertView.findViewById(R.id.something);

        doctorName.setText(doctor.getFullName());
        someInfo.setText(doctor.getId());

        // Return the completed view to render on screen
        return convertView;
    }
}
