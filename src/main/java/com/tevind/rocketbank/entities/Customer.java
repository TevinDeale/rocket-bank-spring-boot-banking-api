package com.tevind.rocketbank.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @jakarta.persistence.Id
    private String uuid;

    @Column(name="fname", nullable = false)
    private String fname;

    @Column(name = "lname", nullable = false)
    private String lname;

    @Column(name = "username", nullable = false)
    private String username;

    //Getter methods
    public String getFName() {
        return this.fname;
    }

    public String getLName() {
        return this.lname;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFullName() {
        String fullName = this.fname + " " + this.lname;
        return fullName;
    }

    //Setter Methods
    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
