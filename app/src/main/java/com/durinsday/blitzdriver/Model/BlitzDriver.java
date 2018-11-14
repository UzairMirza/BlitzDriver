package com.durinsday.blitzdriver.Model;

public class BlitzDriver {

    private String email, password, name, phone, id, carType;

    public BlitzDriver(){

    }

    public BlitzDriver(String email, String password, String name, String phone, String id, String carType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.id = id;
        this.carType = carType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}

