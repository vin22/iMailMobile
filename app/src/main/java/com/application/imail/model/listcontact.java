package com.application.imail.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.Chip;

public class listcontact extends Chip {
    public String AddressBookID, UserID, Name, Email, Phone, Birth_Date, Gender, status, message;
    public boolean Saved, Suggestion, IsDelete;

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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddressBookID() {
        return AddressBookID;
    }

    public void setAddressBookID(String addressBookID) {
        AddressBookID = addressBookID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirth_Date() {
        return Birth_Date;
    }

    public void setBirth_Date(String birth_Date) {
        Birth_Date = birth_Date;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public boolean isSaved() {
        return Saved;
    }

    public void setSaved(boolean saved) {
        Saved = saved;
    }

    public boolean isSuggestion() {
        return Suggestion;
    }

    public void setSuggestion(boolean suggestion) {
        Suggestion = suggestion;
    }

    public boolean isDelete() {
        return IsDelete;
    }

    public void setDelete(boolean delete) {
        IsDelete = delete;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Nullable
    @Override
    public Object getId() {
        return getAddressBookID();
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return getName();
    }

    @NonNull
    @Override
    public String getTitle() {
        return getEmail();
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return null;
    }


}
