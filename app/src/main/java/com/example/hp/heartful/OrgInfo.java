package com.example.hp.heartful;

/**
 * Created by HP INDIA on 10-Apr-17.
 */
public class OrgInfo {


    private String mImage;
    //    /** Default translation for the word */
    private String mOrgname;
    private String mCategory;
    /** Miwok translation for the word */
    private String mOrginfo;
    public OrgInfo(){}
    public OrgInfo(String mOrgname, String mOrginfo, String mImage,String mCategory) {
        this.mOrginfo=mOrginfo;
        this.mOrgname=mOrgname;
        this.mCategory=mCategory;
        this.mImage=mImage;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }



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




//    /** Image resource ID for the word */
//    private int mImageResourceId = NO_IMAGE_PROVIDED;
//
//    /** Constant value that represents no image was provided for this word */
//    private static final int NO_IMAGE_PROVIDED = -1;
//    public OrgInfo(){}
//
//    /**
//     * Create a new Word object.
//     *
//     * @param defaultTranslation is the word in a language that the user is already familiar with
//     *                           (such as English)
//     * @param miwokTranslation is the word in the Miwok language
//     */
////    public OrgInfo(String defaultTranslation, String miwokTranslation) {
////        mOrgname = defaultTranslation;
////        mOrginfo = miwokTranslation;
////    }
//
//    /**
//     * Create a new Word object.
//     *
//     * @param defaultTranslation is the word in a language that the user is already familiar with
//     *                           (such as English)
//     * @param miwokTranslation is the word in the Miwok language
//     * @param imageResourceId is the drawable resource ID for the image associated with the word
//     *
//     */
//    public OrgInfo(String defaultTranslation, String miwokTranslation, int imageResourceId) {
//        mOrgname = defaultTranslation;
//        mOrginfo = miwokTranslation;
//        mImageResourceId = imageResourceId;
//    }
//
//    /**
//     * Get the default translation of the word.
//     */
//    public String getOrgname() {
//        return mOrgname;
//    }
//
//    /**
//     * Get the Miwok translation of the word.
//     */
//    public String getOrginfo() {
//        return mOrginfo;
//    }
//
//    /**
//     * Return the image resource ID of the word.
//     */
//    public int getImageResourceId() {
//        return mImageResourceId;
//    }
//
//    /**
//     * Returns whether or not there is an image for this word.
//     */
//    public boolean hasImage() {
//        return mImageResourceId != NO_IMAGE_PROVIDED;
//    }
//}
}