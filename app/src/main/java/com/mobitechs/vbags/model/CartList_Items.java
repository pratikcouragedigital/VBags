package com.mobitechs.vbags.model;


import com.mobitechs.vbags.URL_WebService;

import java.util.ArrayList;

public class CartList_Items extends ArrayList<String> {

    public String productId,productName,categoryId,categoryName,productPrice,color,size;
    public String productShortDescription,productLongDescription;
    public String firstImagePath,imagePath;
    public String ratings;


    public String userId,username,cartId,qty,deliveryCharges,totalPrice;


    public CartList_Items() {
    }

    public CartList_Items(String productId, String firstImagePath,String imagePath, String productName,String categoryName, String productPrice, String color, String productShortDescription, String categoryId, String productLongDescription, String ratings, String size,String userId, String username, String cartId, String deliveryCharges,String totalPrice,String qty) {

        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productPrice = productPrice;
        this.color = color;
        this.size = size;
        this.ratings = ratings;
        this.productShortDescription = productShortDescription;
        this.productLongDescription = productLongDescription;
        this.firstImagePath = firstImagePath;
        this.imagePath = imagePath;

        this.userId = userId;
        this.username = username;
        this.cartId = cartId;
        this.qty = qty;
        this.deliveryCharges = deliveryCharges;
        this.totalPrice = totalPrice;

    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = URL_WebService.getImageUrl()+firstImagePath;
//        this.firstImagePath = firstImagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
//        this.firstImagePath = firstImagePath;
    }

    public String getproductId() {
        return productId;
    }

    public void setproductId(String productId) {
        this.productId = productId;
    }


    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getproductPrice() {
        return productPrice;
    }
    public void setproductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getcategoryName() {
        return categoryName;
    }

    public void setcategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getproductLongDescription() {
        return productLongDescription;
    }

    public void setproductLongDescription(String productLongDescription) {
        this.productLongDescription = productLongDescription;
    }

    public String getproductShortDescription() {
        return productShortDescription;
    }

    public void setproductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getsize() {
        return size;
    }

    public void setsize(String size) {
        this.size = size;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

    public String getratings() {
        return ratings;
    }

    public void setratings(String ratings) {
        this.ratings = ratings;
    }

    public String getqty() {
        return qty;
    }

    public void setqty(String qty) {
        this.qty = qty;
    }

    public String getdeliveryCharges() {
        return deliveryCharges;
    }

    public void setdeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String gettotalPrice() {
        return totalPrice;
    }

    public void settotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getcartId() {
        return cartId;
    }

    public void setcartId(String cartId) {
        this.cartId = cartId;
    }


    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }


    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }


}
