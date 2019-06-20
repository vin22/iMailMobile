package com.application.imail.model;

public class listcontact {
    public String addressbookid, userid, name, email, phone, birth_date, gender, status, message;
    public boolean saved, suggestion, delete;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setSuggestion(boolean suggestion) {
        this.suggestion = suggestion;
    }

    public boolean isSuggestion() {
        return suggestion;
    }

    public String getAddressbookid() {
        return addressbookid;
    }

    public void setAddressbookid(String addressbookid) {
        this.addressbookid = addressbookid;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
