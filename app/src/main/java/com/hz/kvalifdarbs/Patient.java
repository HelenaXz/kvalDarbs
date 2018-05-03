package com.hz.kvalifdarbs;

import java.util.Calendar;
import java.util.Date;

public class Patient {
    public String name;
    public String surname;
    public String ID;
    public String password;
    public Integer phone;
    public String birthDate;
    public Date addetToSystem;

    public Patient(){

    }

    public Patient(String name, String surname, String ID, String password, Integer phone, String birthDate){
        this.name = name;
        this.surname = surname;
        this.ID = ID;
        this.phone = phone;
        this.birthDate= birthDate;
        Date currentTime = Calendar.getInstance().getTime();
        this.addetToSystem = currentTime;
        //TODO before storing password encrypt it
//        String passwordEncript = encript(password);

    }

//    public String encript(String password){
//        String result;
//        //TODO method which encripts the password
//        return result;
//    }
}
