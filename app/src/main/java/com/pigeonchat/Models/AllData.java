package com.pigeonchat.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AllData {

    ArrayList<String> contacts;
    String profilePic;

    public AllData(String profilePic, ArrayList<String> contacts) {
        this.profilePic = profilePic;
        this.contacts = contacts;
    }

    public AllData(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
