package com.hz.kvalifdarbs.Objects;

public class smallDoctor {
    private String Id;
    private String name;
    public smallDoctor(){
    }
    public smallDoctor(String Id, String name){
        setId(Id);
        setName(name);
    }
    public String getId() {
        return Id;
    }
    public String getName() {
        return name;
    }
    public void setId(String id) {
        Id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
