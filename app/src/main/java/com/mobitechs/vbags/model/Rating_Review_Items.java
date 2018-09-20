package com.mobitechs.vbags.model;


public class Rating_Review_Items {

    public String rating;
    public String review;
    public String postDate;
    public String userid;
    public String userFirstName;
    public String userLastName;

    public Rating_Review_Items() {
    }

    public Rating_Review_Items(String rating, String review, String userid, String userFirstName, String userLastName, String postDate) {

        this.rating = rating;
        this.review = review;
        this.userFirstName = userFirstName;
        this.userid = userid;
        this.userLastName = userLastName;
        this.postDate = postDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
 public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

}
