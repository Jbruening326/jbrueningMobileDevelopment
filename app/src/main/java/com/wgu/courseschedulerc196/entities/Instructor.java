package com.wgu.courseschedulerc196.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructors")
public class Instructor {
    @PrimaryKey(autoGenerate = true)
    private int instructorId;
    private String name;
    private String phoneNumber;
    private String email;

    public Instructor(int instructorId, String name, String phoneNumber, String email) {
        this.instructorId = instructorId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Instructor() {
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {

        return name + ", " + phoneNumber + ", " + email;
    }
}
