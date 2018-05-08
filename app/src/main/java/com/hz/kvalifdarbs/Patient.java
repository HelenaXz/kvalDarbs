package com.hz.kvalifdarbs;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Patient implements Serializable {
    private String Id;
    private String addedToSystem;
    private String birthDate;
    private String name;
    private Integer phone;
    private String surname;
    private Integer password;
    private String room;



    public Patient(){

    }
    //TODO Pievienot field nosacījumi, cik bieži jākustina

    public Patient(String name, String surname, String Id, Integer password, Integer phone, String birthDate, String room){
        setName(name);
        setSurname(surname);
        setId(Id);
        setPhone(phone);
        setBirthDate(birthDate);
        setAddedToSystem();
        setPassword(password);
        setRoom(room);
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setAddedToSystem(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        String currentDateTime = dateFormat.format(new Date());
        this.addedToSystem = currentDateTime;
    }
    public String getAddedToSystem() {
        return addedToSystem;
    }
    public void setId(String Id){
        this.Id = Id;
    }
    public String getId() {
        return Id;
    }
    public void setBirthDate(String birthDate){
        this.birthDate = birthDate;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }
    public Integer getPhone() {
        return phone;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getSurname() {
        return surname;
    }
    public void setPassword(Integer password){
        this.password = password;
    }
    public Integer getPassword(){
        return password;
    }
    public String getRoom(){
        return room;
    }
    public void setRoom(String room){
        this.room = room;
    }

}
