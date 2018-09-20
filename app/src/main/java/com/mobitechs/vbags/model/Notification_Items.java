package com.mobitechs.vbags.model;

import com.mobitechs.vbags.URL_WebService;

public class Notification_Items {

   
    public String title;
    public String message;
    public String ListId;
    public String image;


    public Notification_Items() {
    }

    public Notification_Items(String ListId, String message, String title, String image ) {


        this.ListId = ListId;
        this.title = title;
        this.message = message;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String firstImagePath) {
        this.image = URL_WebService.getImageUrl()+firstImagePath;
        //this.firstImagePath = firstImagePath;
    }


    public String getListId() {
        return ListId;
    }

    public void setListId(String ListId) {
        this.ListId = ListId;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }


}