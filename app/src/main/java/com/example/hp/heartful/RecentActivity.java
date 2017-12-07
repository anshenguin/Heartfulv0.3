package com.example.hp.heartful;

/**
 * Created by HP on 30-11-2017.
 */

public class RecentActivity {
    private String mText;
    private String mImageLink;
    private boolean isNgo;
        public RecentActivity(){}


    public RecentActivity(String mText, String mImageLink, boolean isNgo) {
        this.mText = mText;
        this.mImageLink = mImageLink;
        this.isNgo=isNgo;
    }

    public boolean isNgo() {
        return isNgo;
    }

    public String getmText() {
        return mText;
    }
    public String getmImageLink() {
        return mImageLink;
    }
}
