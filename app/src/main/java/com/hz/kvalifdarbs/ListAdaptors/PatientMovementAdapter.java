package com.hz.kvalifdarbs.ListAdaptors;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import com.hz.kvalifdarbs.Objects.Doctor;
        import com.hz.kvalifdarbs.Objects.Examination;
        import com.hz.kvalifdarbs.Objects.Movement;
        import com.hz.kvalifdarbs.R;

        import java.util.ArrayList;
        import java.util.Collections;

public class PatientMovementAdapter extends ArrayAdapter<Movement> {


    public PatientMovementAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movement thisMovement = getItem(position);

        if (convertView == null){
            //TODO 2 create patient_movement_list_adaptor.xml
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_movement_list_adaptor, parent, false);
        }

        //TODO 3 create the adaptor for it

        return convertView;

    }
}
