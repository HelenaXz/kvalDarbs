package com.hz.kvalifdarbs.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Admin;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;
import com.hz.kvalifdarbs.utils.PreferenceUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class AddAdminActivity extends AppCompatActivity {
    DatabaseReference rootRef, userRef;
    EditText name, surname, id, phone, pass, passRepeat;
    String passEncrypt;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        context = getApplicationContext();
        final Intents intents = new Intents(this);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase reference
        rootRef = FirebaseDatabase.getInstance().getReference("Admins");

        //TextViews, Buttons
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        id = findViewById(R.id.adminID);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        passRepeat = findViewById(R.id.passwordRepeat);
        Button submitForm = findViewById(R.id.submitBtn);
        final ArrayList<String> existingUsers = MethodHelper.userExisting("Admins");


        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){

                    String idString = id.getText().toString();
                    if(!existingUsers.contains(idString)){
                        //If user doesn't exist
                        String nameString = name.getText().toString();
                        String surnameString = surname.getText().toString();
                        String phoneString = phone.getText().toString();
                        passEncrypt = MethodHelper.sha1Hash(pass.getText().toString());

                        userRef = rootRef.child(idString);

                        Admin newAdmin = new Admin(idString, nameString, passEncrypt, phoneString, surnameString);
                        userRef.setValue(newAdmin);

                        Toast toast = Toast.makeText(context, "Administrator added to DB", Toast.LENGTH_SHORT);
                        toast.show();
                        //Clear Form
                        clearForm(v);
                    } else {
                        Toast toast = Toast.makeText(context, "Administrator with id exists", Toast.LENGTH_SHORT);
                        toast.show();
                    }


                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intents.addUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }


    public boolean validate(){
        int valid = 0;
        if(TextUtils.isEmpty(name.getText().toString().trim())){
            name.setError("Please enter name");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(surname.getText().toString().trim())){
            surname.setError("Please enter surname");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(id.getText().toString().trim())){
            id.setError("Please enter ID number");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(phone.getText().toString().trim())){
            phone.setError("Please enter phone number");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(pass.getText().toString().trim())){
            pass.setError("Please enter password");
        } else {valid = valid + 1;}
        if(TextUtils.isEmpty(passRepeat.getText().toString().trim())){
            passRepeat.setError("Please repeat password");
        } else {valid = valid + 1;}
        if(pass.getText().toString().equals(passRepeat.getText().toString())) {
            valid = valid + 1;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Passwords don't match!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if (valid < 7) return false;
        else return true;
    }

    public void clearForm(View v){
        name.setText(null);
        surname.setText(null);
        id.setText(null);
        phone.setText(null);
        pass.setText(null);
        passRepeat.setText(null);
        //Close keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}
