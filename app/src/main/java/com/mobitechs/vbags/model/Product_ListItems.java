package com.mobitechs.vbags.model;

import com.mobitechs.vbags.URL_WebService;

public class Product_ListItems {

    public String productId, productName, categoryId, categoryName, productPrice, deliveryCharges, color, size;
    public String productShortDescription, productLongDescription;
    public String firstImagePath, secondImagePath, thirdImagePath;
    public String ratings;
    public String postDate;
    public String adminId;
    public String img1,img2,img3,img4;


    public Product_ListItems() {
    }

    public Product_ListItems(String img1,String img2,String img3,String img4,String deliveryCharges, String productId, String firstImagePath, String secondImagePath, String thirdImagePath, String productName, String productPrice, String color, String productShortDescription, String categoryId, String productLongDescription, String ratings, String size, String postDate, String adminId) {

        this.productId = productId;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.productName = productName;
        this.categoryId = categoryId;
        this.productPrice = productPrice;
        this.color = color;
        this.size = size;
        this.ratings = ratings;
        this.productShortDescription = productShortDescription;
        this.productLongDescription = productLongDescription;
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;
        this.postDate = postDate;
        this.adminId = adminId;
        this.deliveryCharges = deliveryCharges;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = URL_WebService.getImageUrl()+firstImagePath;
        //this.firstImagePath = firstImagePath;
    }

    public String get2ndImagePath() {
        return img2;
    }

    public void set2ndImagePath(String img2) {
        this.img2 = URL_WebService.getImageUrl()+img2;
        //this.img2 = img2;
    }

    public String get3rdImagePath() {
        return img3;
    }

    public void set3rdImagePath(String img3) {
        this.img3 = URL_WebService.getImageUrl()+img3;
//        this.img3 = img3;
    }
    public String get4thImagePath() {
        return img4;
    }

    public void set4thImagePath(String img4) {
        this.img4 = URL_WebService.getImageUrl()+img4;
//        this.img4 = img4;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getproductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShortDescription() {
        return productShortDescription;
    }

    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProductLongDescription() {
        return productLongDescription;
    }

    public void setProductLongDescription(String productLongDescription) {
        this.productLongDescription = productLongDescription;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }


}