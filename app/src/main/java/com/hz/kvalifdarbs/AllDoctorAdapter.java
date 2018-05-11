package com.hz.kvalifdarbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hz.kvalifdarbs.Objects.Doctor;

public class AllDoctorAdapter extends ArrayAdapter<Doctor> {
    Context context;
    private TextView doctorName, someInfo;
    Doctor doctor;

    public AllDoctorAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //TODO Don't show the doctors in the list that the patient has
        // Get the data item for this position
        doctor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.all_doctor_list_adaptor, parent, false);
        }
        // Lookup view for data population

//        Button roomField = convertView.findViewById(R.id.room);
//        Populate the data into the template view using the data object

        doctorName = convertView.findViewById(R.id.fullName);
        someInfo = convertView.findViewById(R.id.something);

        doctorName.setText(doctor.getFullName());


//        addDoctor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //1.find patient by Id the the database
//
//
//            }
//        });
        // Return the completed view to render on screen
        return convertView;
    }
}
