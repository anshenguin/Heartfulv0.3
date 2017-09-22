package com.example.hp.heartful;

/**
 * Created by HP on 21-09-2017.
 */

public class Users {
    public String userName;
    public String profilePicLink;
    public Users() {
    }

    public Users(String name, String link) {
        this.userName = name;
        this.profilePicLink = link;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }
}
