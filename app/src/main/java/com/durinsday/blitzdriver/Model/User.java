package com.durinsday.blitzdriver.Model;

public class User {

    private String email, password, name, phone, id;

    public User(){

    }


    public  User(String email, String password, String name, String phone, String id){
        this.email=email;
        this.password=password;
        this.name=name;
        this.phone=phone;
        this.id=id;
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
}

