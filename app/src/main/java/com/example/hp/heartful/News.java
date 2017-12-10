package com.example.hp.heartful;

/**
 * Created by HP on 25-05-2017.
 */

public class News {
        private String Title,Description,Image,DateAndTime;

    public  News(){
    }


    public News(String title, String description, String image, String dateAndTime) {
        this.Title = title;
        this.DateAndTime=dateAndTime;
        this.Description = description;
        this.Image = image;

    }
    public String getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        DateAndTime = dateAndTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }


    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


}
