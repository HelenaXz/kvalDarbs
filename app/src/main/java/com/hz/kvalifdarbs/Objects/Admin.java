package com.hz.kvalifdarbs.Objects;

public class Admin {

    private String Id;
    private String name;
    private String password;
    private Integer phone;
    private String surname;


    public Admin(){
    }

    public Admin(String Id, String name, String password,  Integer phone, String surname ){
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
    public String getPassword() {
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
    public void setPassword(String password) {
        this.password = password;
    }
}
