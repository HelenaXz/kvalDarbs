package com.hz.kvalifdarbs.Objects;

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
    private String password;
    private String room;
    private String gender;
    private Integer moveEveryTime;
    private Examination lastExam;



    public Patient(){

    }
    //TODO Pievienot field nosacījumi, cik bieži jākustina

    public Patient(String name, String surname, String Id, String gender, String password, Integer phone, String birthDate, String room, Integer moveEveryTime){
        setName(name);
        setSurname(surname);
        setId(Id);
        setPhone(phone);
        setBirthDate(birthDate);
        setAddedToSystem();
        setPassword(password);
        setRoom(room);
        setGender(gender);
        setMoveEveryTime(moveEveryTime);
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
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
    public String getRoom(){
        return room;
    }
    public void setRoom(String room){
        this.room = room;
    }
    public String getFullName(){
        String fullName = this.name + " " + this.surname;
        return fullName;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public Integer getMoveEveryTime(){
        return this.moveEveryTime;
    }
    public void setMoveEveryTime(Integer moveEveryTime){
        this.moveEveryTime = moveEveryTime;
    }
     public Examination getLastExam(){
        return this.lastExam;
     }
     public void setLastExam(Examination exam){
        this.lastExam = exam;
     }
}
