package com.hz.kvalifdarbs.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.utils.Intents;
import com.hz.kvalifdarbs.Objects.Admin;
import com.hz.kvalifdarbs.R;
import com.hz.kvalifdarbs.utils.MethodHelper;

public class AddAdminActivity extends AppCompatActivity {

    DatabaseReference rootRef, adminRef;
    EditText name, surname, id, phone, pass, passRepeat;
    String passEncrypt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intents intents = new Intents(this);

        rootRef = FirebaseDatabase.getInstance().getReference("Admins");

        //Buttons
        Button submitForm = findViewById(R.id.submitBtn);
        //Text fields
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        id = findViewById(R.id.adminID);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        passRepeat = findViewById(R.id.passwordRepeat);

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String nameString = name.getText().toString();
                    String surnameString = surname.getText().toString();
                    String idString = id.getText().toString();
                    String phoneString = phone.getText().toString();
                    Integer phoneNum = Integer.parseInt(phoneString);
                    passEncrypt = MethodHelper.sha1Hash(pass.getText().toString());

                    adminRef = rootRef.child(idString);
                    Admin newAdmin = new Admin(idString, nameString, passEncrypt, phoneNum, surnameString);
                    adminRef.setValue(newAdmin);

                    Context context = getApplicationContext();
                    String text = "Administrator added to DB";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                    name.setText(null);
                    surname.setText(null);
                    id.setText(null);
                    phone.setText(null);
                    pass.setText(null);
                    passRepeat.setText(null);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                startActivity(intents.addUser.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
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
        if (valid < 7) return true;
        else return false;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
