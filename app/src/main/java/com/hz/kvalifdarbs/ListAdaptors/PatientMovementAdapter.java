

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

        Movement thisMove = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.patient_move_list_adaptor, parent, false);
        }

        TextView moveTime = convertView.findViewById(R.id.moveTime);
        TextView moveAmplitude = convertView.findViewById(R.id.moveAmp);

        Integer x = thisMove.getX();
        Integer y = thisMove.getY();
        Integer z = thisMove.getZ();
        String output = String.format("Movement coordinates x: %d, y: %d, z: %d",x, y, z);
        String s = "Move time: " + thisMove.getAddDateTime();
        moveTime.setText(s);
        moveAmplitude.setText(output);

        return convertView;

    }
}
