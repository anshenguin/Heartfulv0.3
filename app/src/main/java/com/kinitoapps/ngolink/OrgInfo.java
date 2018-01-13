package com.kinitoapps.ngolink;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP INDIA on 10-Apr-17.
 */
public class OrgInfo {

    public OrgInfo() {
    }

    List<String> mCategoryNew = new ArrayList<>();
    private String mImage;
    private String mOrgname;

    public List<String> getCategories() {
        return mCategoryNew;
    }

    public void setCategories(List<String> categories) {
        this.mCategoryNew = categories;
    }

    public OrgInfo(List<String> categories, String mImage, String mOrgname, String mOrginfo) {
        this.mCategoryNew = categories;
        this.mImage = mImage;
        this.mOrgname = mOrgname;
        this.mOrginfo = mOrginfo;
    }
//    public String getmCategory() {
//        return mCategory;
//    }
//
//    public void setmCategory(String mCategory) {
//        this.mCategory = mCategory;
//    }
//
//    private String mCategory;
    /**
     * Miwok translation for the word
     */
    private String mOrginfo;

    public String getmOrgname() {
        return mOrgname;
    }

    public void setmOrgname(String mOrgname) {
        this.mOrgname = mOrgname;
    }

    public String getmOrginfo() {
        return mOrginfo;
    }

    public void setmOrginfo(String mOrginfo) {
        this.mOrginfo = mOrginfo;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }


}