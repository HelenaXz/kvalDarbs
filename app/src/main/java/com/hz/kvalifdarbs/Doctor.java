package com.hz.kvalifdarbs;

public class Doctor {
    public String name;
    public String surname;
    public String email;
    public String password;
    public Integer phone;

    public Doctor(){
    }

    public Doctor(String name, String surname, String email, String password, Integer phone ){
        this.name = name;
        this.surname = surname;
        this.email = email;
//        //TODO before storing password encrypt it
//        String passwordEncript = encript(password);
        this.password = password;
        this.phone = phone;
    }

//    public String encript(String password){
//        String result;
//        //TODO method which encripts the password
//        return result;
//    }
}
