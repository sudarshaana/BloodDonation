package me.smondal.blooddonation.activity;

/**
 * Created by Sudarshan on 10/18/2017.
 */

public class Donor {
    private int id;
    private String name;
    private String bloodGroup;
    private String institute;
    private String phoneNo;

    public Donor() {
    }

    public Donor(int id, String name, String bloodGroup, String institute, String phoneNo) {
        this.id = id;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.institute = institute;
        this.phoneNo = phoneNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
