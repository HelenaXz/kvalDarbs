package com.hz.kvalifdarbs.Objects;

import java.io.Serializable;

public class Movement implements Serializable {
    Integer x, y, z;
    String addDateTime;

    public Movement(){

    }
    public Movement(String addDateTime, Integer x, Integer y, Integer z){
        setAddDateTime(addDateTime);
        setX(x);
        setY(y);
        setZ(z);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public String getAddDateTime() {
        return addDateTime;
    }

    public void setAddDateTime(String addDateTime) {
        this.addDateTime = addDateTime;
    }
}
