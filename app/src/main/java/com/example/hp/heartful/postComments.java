package com.example.hp.heartful;

/**
 * Created by HP on 07-10-2017.
 */

public class postComments {
    public String userName;
    public String profilePicLink;
    public String comments;
    public postComments(){
    }
    public postComments(String userName, String profilePicLink, String comments) {
        this.userName = userName;
        this.profilePicLink = profilePicLink;
        this.comments = comments;

    }
    public String getUserName() {
        return userName;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public String getComments() {
        return comments;
    }



}
