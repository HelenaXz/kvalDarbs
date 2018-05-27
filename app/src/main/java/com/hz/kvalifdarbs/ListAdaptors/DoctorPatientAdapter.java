package com.hz.kvalifdarbs.ListAdaptors;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.Objects.Examination;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DoctorPatientAdapter extends ArrayAdapter<Object> {
    Context context;
    public DoctorPatientAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }
    String lastExamTime;
    @SuppressLint("WrongConstant")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Patient patient = (Patient) getItem(position);
        Examination lastExam = patient.getLastExam();
        if(lastExam != null){
            lastExamTime = lastExam.getAddDateTime();
        }



        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_patient_list_adaptor, parent, false);
        }

        TextView patientId = convertView.findViewById(R.id.patientId);
        TextView patientName = convertView.findViewById(R.id.fullName);
        TextView patientCheck = convertView.findViewById(R.id.nextCheckup);
        TextView colorIndicator = convertView.findViewById(R.id.colorIndicator);

        patientId.setText(patient.getId());
        patientName.setText(patient.getFullName());
        if(lastExamTime != null){
            //TODO get current time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = Calendar.getInstance().getTime();
            String currentTime  = sdf.format(time);
            currentTime = currentTime.substring(11);
            int minTime = patient.getMoveEveryTime();
            int curTimeHour = Integer.parseInt(currentTime.substring(0,2));
            int curTimeMin = Integer.parseInt(currentTime.substring(3,5));
            int curTimeAllMins = curTimeHour * 60 + curTimeMin;

            lastExamTime = lastExamTime.substring(11);
            int lastTimeHour = Integer.parseInt(lastExamTime.substring(0,2));
            int lastTimeMin = Integer.parseInt(lastExamTime.substring(3,5));
            int lastTimeAllMins = lastTimeHour * 60 + lastTimeMin;
            int toNextMove = minTime-(curTimeAllMins - lastTimeAllMins);
            if(toNextMove>=60){
                colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            } else if (toNextMove < 60 && toNextMove > 20){
                colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.ok));
            } else {
                colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.medium));
            }

            if(toNextMove < 0){
                int colorFrom = ContextCompat.getColor(context, R.color.urgentBright);
                int colorTo = ContextCompat.getColor(context, R.color.urgentDark);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(2000); // milliseconds
                colorAnimation.setRepeatMode(Animation.REVERSE);
                colorAnimation.setRepeatCount(Animation.INFINITE);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        colorIndicator.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();



                String t = String.valueOf(toNextMove*(-1));
                String s = "Check now! Missed by " + t + " min";
                patientCheck.setText(s);
            } else {
                String t = String.valueOf(toNextMove);
                String s = "Next checkup in " + t + " min";
                patientCheck.setText(s);
            }

            lastExamTime=null;
        } else {
            colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.urgent));
            String s = "No checkups made!";
            patientCheck.setText(s);
        }

        return convertView;

    }
}
