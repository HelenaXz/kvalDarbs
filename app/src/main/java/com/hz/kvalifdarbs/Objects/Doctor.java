package com.hz.kvalifdarbs.Objects;

public class Doctor {

    private String Id;
    private String name;
    private Integer password;
    private Integer phone;
    private String surname;


    public Doctor(){
    }

    public Doctor(String Id, String name, Integer password,  Integer phone, String surname ){
        setId(Id);
        setName(name);
        setPassword(password);
        setPhone(phone);
        setSurname(surname);
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getId() {
        return Id;
    }
    public Integer getPhone() {
        return phone;
    }
    public Integer getPassword() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setId(String id) {
        Id = id;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }
    public void setPassword(Integer password) {
        this.password = password;
    }
    public String getFullName(){
        String fullName = this.name + " " +  this.surname;
        return fullName;
    }
}
