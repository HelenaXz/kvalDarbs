package com.hz.kvalifdarbs.utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hz.kvalifdarbs.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MethodHelper {

    public static void changePassword(EditText newPass, EditText newPassRepeat, EditText currentPass, String currDbPassString, AlertDialog changePassDialog, Context context, DatabaseReference userRef){

        String newPassw, newPasswRep;
        newPassw = newPass.getText().toString();
        newPasswRep = newPassRepeat.getText().toString();
        String currPass = MethodHelper.sha1Hash(currentPass.getText().toString());
        if(currPass.equals(currDbPassString)){
            if(newPassw.equals(newPasswRep)){
                Integer newPassInt = newPassw.hashCode();
                userRef.child("password").setValue(newPassInt);
                PreferenceUtils.savePassword(newPassInt.toString(), context);
                changePassDialog.dismiss();
            } else {
                MethodHelper.showToast(context, "New passwords do not match!");
            }
        } else {
            MethodHelper.showToast(context, "Current password does not match Database");
        }
    }

    public static  void logOut(Context context, Intents intents) {
        PreferenceUtils.saveId("", context);
        PreferenceUtils.savePassword("", context);
        PreferenceUtils.saveUserType("", context);
        PreferenceUtils.saveUserName("", context);
        PreferenceUtils.saveUserSurname("", context);
        PreferenceUtils.saveLoggedIn("false", context);
        context.startActivity(intents.userSelect.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public static String sha1Hash( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException | UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }

    public static ArrayList<String> userExisting(String userAddType){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userTypeRef = rootRef.child(userAddType);

        final ArrayList<String> users = new ArrayList<>();
        //TODO check if user with Id exists
        userTypeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                users.add(key);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return users;

    }
    public static void setUpNavigationMenu(NavigationView navigationView, String userId, String fullName, String userType){
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        if(userType.equals("Patient")){
            navigationView.inflateMenu(R.menu.patient_drawer);
        }
        if(userType.equals("Doctor")){
            navigationView.inflateMenu(R.menu.doctor_drawer);
        }
        if(userType.equals("Administrator")){
            navigationView.inflateMenu(R.menu.admin_drawer);
        }

        View headView = navigationView.getHeaderView(0);
        TextView headUserName = headView.findViewById(R.id.headFullName);
        TextView headUserId = headView.findViewById(R.id.headUserId);
        headUserId.setText(userId);
        headUserName.setText(fullName);
    }

    public static void showToast(Context context, String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
