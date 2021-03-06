package com.hz.kvalifdarbs.ListAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Doctor;
import com.hz.kvalifdarbs.Objects.Examination;
import com.hz.kvalifdarbs.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PatientDoctorAdapter extends ArrayAdapter<Doctor> {

    public PatientDoctorAdapter(Context context) {
        super(context, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Doctor thisDoctor = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_doctor_list_adaptor, parent, false);
        }

        TextView doctorName = convertView.findViewById(R.id.doctorName);
        TextView doctorId = convertView.findViewById(R.id.doctorId);
        TextView doctorPhone = convertView.findViewById(R.id.doctorPhone);

        doctorName.setText(thisDoctor.getFullName());
        doctorId.append(thisDoctor.getId());
        doctorPhone.append(thisDoctor.getPhone().toString());


        return convertView;

    }
}
