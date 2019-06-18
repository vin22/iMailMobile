package com.application.imail.model;

import java.util.ArrayList;

public class User {
    public String username;
    public String Name;
    public String Email;
    public String norek;
    public String password;
    public String nohp;
    public String saldo;
    public String message, status;
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<String> paket=new ArrayList<>();
    public User(){

    }
    public User(String username, String norek, String name, String email, String password, String nohp, String saldo) {
        this.username = username;
        this.Name = name;
        this.Email = email;
        this.password = password;
        this.nohp = nohp;
        this.norek=norek;
        this.saldo=saldo;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getNoRek() {
        return norek;
    }

    public void setNoRek(String norek) {
        this.norek = norek;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
