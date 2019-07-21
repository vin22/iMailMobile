package com.application.imail.model;

import java.util.Date;

public class Message implements Comparable<Message>{
    public int MessageID;
    public String Sendername, Sender, Receiver, Reply, Forward, Subject, Body, Attachment, Cc, Bcc, Folder, Date;
    public boolean Dlt, isRead, isTrash, Starred;
    public String status, message;
    public boolean isChecked=false;
    public boolean isLongClick=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

    public String getSendername() {
        return Sendername;
    }

    public void setSendername(String sendername) {
        Sendername = sendername;
    }

    public String getFolder() {
        return Folder;
    }

    public void setFolder(String folder) {
        Folder = folder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageID() {
        return MessageID;
    }

    public void setMessageID(int messageID) {
        MessageID = messageID;
    }

    public void setDlt(boolean dlt) {
        Dlt = dlt;
    }

    public boolean isDlt() {
        return Dlt;
    }

    public String getSubject() {
        return Subject;
    }


    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getCc() {
        return Cc;
    }

    public void setCc(String cc) {
        Cc = cc;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public String getBcc() {
        return Bcc;
    }

    public void setBcc(String bcc) {
        Bcc = bcc;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getForward() {
        return Forward;
    }

    public void setForward(String forward) {
        Forward = forward;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setStarred(boolean starred) {
        Starred = starred;
    }

    public boolean isStarred() {
        return Starred;
    }


    public boolean isTrash() {
        return isTrash;
    }


    public void setTrash(boolean trash) {
        isTrash = trash;
    }

    @Override
    public int compareTo(Message message) {
        return message.getDate().compareTo(getDate());
    }
}

