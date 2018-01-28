package com.kinitoapps.ngolink;

/**
 * Created by HP on 07-10-2017.
 */

public class postComments {
    public String userName;
    public String profilePicLink;
    public String comments;
    public String uid;
    public postComments(){
    }
    public postComments(String userName, String profilePicLink, String comments,String uid) {
        this.userName = userName;
        this.profilePicLink = profilePicLink;
        this.comments = comments;
        this.uid= uid;
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

    public String getUid() {
        return uid;
    }
}
