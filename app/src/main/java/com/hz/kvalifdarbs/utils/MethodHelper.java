package com.hz.kvalifdarbs.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class MethodHelper {
    public static void changePassword(EditText newPass, EditText newPassRepeat, EditText currentPass, String currDbPassString, AlertDialog changePassDialog, Context context, DatabaseReference userRef ){
        String newPassw, newPasswRep;
        newPassw = newPass.getText().toString();
        newPasswRep = newPassRepeat.getText().toString();
        Integer currPass = currentPass.getText().toString().hashCode();
        String currPassString = currPass.toString();
        if(currPassString.equals(currDbPassString)){
            if(newPassw.equals(newPasswRep)){
                Integer newPassInt = newPassw.hashCode();
                userRef.child("password").setValue(newPassInt);
                PreferenceUtils.savePassword(newPassInt.toString(), context);
                changePassDialog.dismiss();
            } else {
                String text = "New passwords do not match!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            String text = "Current password does not match Database";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static  void logOut(Context context, Intents intents) {
        PreferenceUtils.saveId("", context);
        PreferenceUtils.savePassword("", context);
        PreferenceUtils.saveUserType("", context);
        PreferenceUtils.saveUserName("", context);
        PreferenceUtils.saveUserSurname("", context);
        context.startActivity(intents.userSelect.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
}
