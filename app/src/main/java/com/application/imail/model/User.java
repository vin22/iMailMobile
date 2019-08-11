package com.application.imail.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    public int UserID;
    public String Name;
    public String Email, Alternate_Email;
    public String Password;
    public String Reply;
    public String message, status;
    public static ArrayList<User> users = new ArrayList<>();
    public static List<User> emailparent=new ArrayList<>();
    public static List<listcontact> listcontacts=new ArrayList<>();
    public User(){

    }

    public static List<User> getEmailparent() {
        return emailparent;
    }

    public static void setEmailparent(List<User> emailparent) {
        User.emailparent = emailparent;
    }

    public static List<listcontact> getListcontacts() {
        return listcontacts;
    }

    public static void setListcontacts(List<listcontact> listcontacts) {
        User.listcontacts = listcontacts;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public String getAlternate_Email() {
        return Alternate_Email;
    }

    public void setAlternate_Email(String alternate_Email) {
        Alternate_Email = alternate_Email;
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
