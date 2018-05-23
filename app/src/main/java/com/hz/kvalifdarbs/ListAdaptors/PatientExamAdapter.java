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

import java.util.ArrayList;
import java.util.Collections;

public class PatientExamAdapter extends ArrayAdapter<Examination> {


    public PatientExamAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Examination thisExam = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_exam_list_adaptor, parent, false);
        }

        TextView examTime = convertView.findViewById(R.id.examTime);
        TextView doctorName = convertView.findViewById(R.id.examDoctor);
        TextView examComment = convertView.findViewById(R.id.examComment);

        String s = "Time: " + thisExam.getAddDateTime();
        examTime.setText(s);
        s = "Doctor: " + thisExam.getDoctorName();
        doctorName.setText(s);
        s = "Comment: " + thisExam.getComment();
        examComment.setText(s);
//        examTime.append(thisExam.getAddDateTime());
//        doctorName.append(thisExam.getDoctorName());
//        examComment.append(thisExam.getComment());

        return convertView;

    }
}
