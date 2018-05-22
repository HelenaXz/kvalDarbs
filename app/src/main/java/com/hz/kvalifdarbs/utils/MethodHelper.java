package com.hz.kvalifdarbs.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String sha1Hash( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
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
}
