package com.application.imail.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    public int UserID;
    public String Name;
    public String Email;
    public String Password;
    public String message, status;
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<String> paket=new ArrayList<>();
    public static List<listcontact> listcontacts=new ArrayList<>();
    public User(){

    }

    public static List<listcontact> getListcontacts() {
        return listcontacts;
    }

    public static void setListcontacts(List<listcontact> listcontacts) {
        User.listcontacts = listcontacts;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void pulsa(){
        paket.clear();
        paket.add("Pulsa Rp10000");
        paket.add("Pulsa Rp25000");
        paket.add("Pulsa Rp50000");
        paket.add("Pulsa Rp100000");
        paket.add("Pulsa Rp200000");
        paket.add("Pulsa Rp500000");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
