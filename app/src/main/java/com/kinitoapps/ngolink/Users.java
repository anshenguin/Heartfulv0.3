package com.kinitoapps.ngolink;

/**
 * Created by HP on 21-09-2017.
 */

public class Users {

    public String userDesc;
    public String userName;
    public String profilePicLink;
    public String desc;
    public boolean canPost;
    public Users() {
    }

    public Users(String name, String link,boolean post,String desc) {
        this.userName = name;
        this.profilePicLink = link;
        this.canPost=post;
        this.userDesc=desc;
    }
    public String getUserDesc() {
        return userDesc;}

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
