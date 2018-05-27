package com.hz.kvalifdarbs.Objects;

public class Examination {
    private String doctorId;
    private String doctorName;
    private String addDateTime;
    private String comment;

    public Examination(){

    }

    public Examination(String doctorId, String doctorName, String addDateTime, String comment){
        setAddDateTime(addDateTime);
        setComment(comment);
        setDoctorId(doctorId);
        setDoctorName(doctorName);


    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public void setAddDateTime(String addDateTime) {
        this.addDateTime = addDateTime;
    }
    public String getAddDateTime() {
        return addDateTime;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorId() {
        return doctorId;
    }
}
