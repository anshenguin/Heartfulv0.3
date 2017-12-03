package com.example.hp.heartful;

/**
 * Created by HP on 30-11-2017.
 */

public class RecentActivity {
    private String mText;
    private String mImageLink;
    public RecentActivity(){}


    public RecentActivity(String mText, String mImageLink) {
        this.mText = mText;
        this.mImageLink = mImageLink;
    }
    public String getmText() {
        return mText;
    }
    public String getmImageLink() {
        return mImageLink;
    }
}
