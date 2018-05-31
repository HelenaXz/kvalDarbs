package com.hz.kvalifdarbs.ListAdaptors;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.hz.kvalifdarbs.Objects.Movement;
import com.hz.kvalifdarbs.Objects.Patient;
import com.hz.kvalifdarbs.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DoctorPatientAdapter extends ArrayAdapter<Object> {
    Context context;
    public DoctorPatientAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }
    String lastExamTime;
    String lastMoveTime;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Patient patient = (Patient) getItem(position);
        Examination lastExam = patient.getLastExam();
        Movement lastMove = patient.getLastMovement();
        if(
//                lastExam != null ||
                        lastMove != null){
//            lastExamTime = lastExam.getAddDateTime();
            lastMoveTime = lastMove.getAddDateTime();
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
        if(
//                lastExamTime != null ||
                        lastMoveTime != null){
            //TODO get current time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = Calendar.getInstance().getTime();
            String currentTime  = sdf.format(curDate);
//            Date lastExamDate = null;
            Date lastMoveDate = null;
            Date curTime = null;
            try {
                curTime = sdf.parse(currentTime);
//                lastExamDate = sdf.parse(lastExamTime);
                lastMoveDate = sdf.parse(lastMoveTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            float diff = curTime.getTime() - lastExamDate.getTime();
            float diff = curTime.getTime() - lastMoveDate.getTime();

            float flo = (diff / (1000 * 60 * 60 * 24));

            int mins = Math.round(flo * 24 * 60);

            int minTime = patient.getMoveEveryTime();


            int toNextMove = minTime-mins;
            if(toNextMove>=100){
                colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            } else if (toNextMove < 100 && toNextMove > 30){
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


                int hours = toNextMove / 60 * (-1);
                int minsLeft = toNextMove % 60 * (-1);
                String t = String.valueOf(toNextMove*(-1));
                String s = "Check now! Missed by " + hours + "h and " + minsLeft +"mins";
                patientCheck.setText(s);
                patientCheck.setTextSize(12);
            } else {
                String t = String.valueOf(toNextMove);
                String s = "Next checkup in " + t + " min";
                patientCheck.setText(s);
            }

//            lastExamTime = null;
            lastMoveTime = null;
        } else {
            colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.urgent));
            String s = "No checkups made!";
            patientCheck.setText(s);
        }

        return convertView;

    }
}
