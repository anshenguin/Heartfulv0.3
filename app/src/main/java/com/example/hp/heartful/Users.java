package com.example.hp.heartful;

/**
 * Created by HP on 21-09-2017.
 */

public class Users {
    public String userName;
    public String profilePicLink;
    public boolean canPost;
    public Users() {
    }

    public Users(String name, String link,boolean post) {
        this.userName = name;
        this.profilePicLink = link;
        this.canPost=post;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }
    public boolean isCanPost() {
        return canPost;
    }
}
