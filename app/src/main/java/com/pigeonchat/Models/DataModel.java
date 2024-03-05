package com.pigeonchat.Models;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private String about, emailId, image, name, userId;
    long created_at;
    boolean is_online;

//    public ArrayList<List> getContacts() {
//        return contacts;
//    }

    public DataModel(String about, String emailId, String image, String name, String userId, long created_at, boolean is_online) {
        this.about = about;
        this.emailId = emailId;
        this.image = image;
        this.name = name;
        this.userId = userId;
        this.created_at = created_at;
        this.is_online = is_online;

    }

    public DataModel() {
    }

    public String getAbout() {
        return about;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public long getCreated_at() {
        return created_at;
    }

    public boolean is_online() {
        return is_online;
    }

}

