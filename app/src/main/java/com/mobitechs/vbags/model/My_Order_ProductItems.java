package com.mobitechs.vbags.model;

import com.mobitechs.vbags.URL_WebService;

public class My_Order_ProductItems {

    public String orderId, orderBulkId, luckyDrawPrice,ordered_Date, productId, deleveryStatus, address, username, mobileNo, email, orderStatusId;
    public String productName, category, productPrice, qty, deliveryCharges, totalPrice, color, size, productShortDescription, productLongDescription, imagePath, ratings;

    public My_Order_ProductItems() {

    }

    public My_Order_ProductItems(String orderBulkId, String luckyDrawPrice, String productId, String imagePath, String productName, String productPrice, String color, String size, String productShortDescription, String category, String productLongDescription, String ratings, String deleveryStatus, String address, String ordered_Date, String qty, String totalPrice, String deliveryCharges, String orderStatusId, String orderId, String username, String mobileNo, String email) {

        this.orderBulkId = orderBulkId;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
        this.color = color;
        this.size = size;
        this.productShortDescription = productShortDescription;
        this.productLongDescription = productLongDescription;
        this.imagePath = imagePath;
        this.ratings = ratings;
        this.deleveryStatus = deleveryStatus;
        this.orderStatusId = orderStatusId;
        this.address = address;
        this.ordered_Date = ordered_Date;
        this.qty = qty;
        this.deliveryCharges = deliveryCharges;
        this.orderId = orderId;
        this.username = username;
        this.mobileNo = mobileNo;
        this.email = email;
        this.luckyDrawPrice = luckyDrawPrice;

    }

    public String getimagePath() {
        return imagePath;
    }

    public void setimagePath(String imagePath) {
        this.imagePath = URL_WebService.getImageUrl() + imagePath;
//        this.imagePath = imagePath;
    }

    public String getorderBulkId() {
        return orderBulkId;
    }

    public void setorderBulkId(String orderBulkId) {
        this.orderBulkId = orderBulkId;
    }

    public String getproductId() {
        return productId;
    }

    public void setproductId(String productId) {
        this.productId = productId;
    }

    public String getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public String getproductShortDescription() {
        return productShortDescription;
    }

    public void setproductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }


    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

    public String getsize() {
        return size;
    }

    public void setsize(String size) {
        this.size = size;
    }

    public String getproductLongDescription() {
        return productLongDescription;
    }

    public void setproductLongDescription(String productLongDescription) {
        this.productLongDescription = productLongDescription;
    }

    public String getratings() {
        return ratings;
    }

    public void setratings(String ratings) {
        this.ratings = ratings;
    }


    public String getdeleveryStatus() {
        return deleveryStatus;
    }

    public void setdeleveryStatus(String deleveryStatus) {
        this.deleveryStatus = deleveryStatus;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public String getordered_Date() {
        return ordered_Date;
    }

    public void setordered_Date(String ordered_Date) {
        this.ordered_Date = ordered_Date;
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

    public String getorderStatusId() {
        return orderStatusId;
    }

    public void setorderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String gettotalPrice() {
        return totalPrice;
    }

    public void settotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getorderId() {
        return orderId;
    }

    public void setorderId(String orderId) {
        this.orderId = orderId;
    }


    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getmobileNo() {
        return mobileNo;
    }

    public void setmobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getluckyDrawPrice() {
        return luckyDrawPrice;
    }

    public void setluckyDrawPrice(String luckyDrawPrice) {
        this.luckyDrawPrice = luckyDrawPrice;
    }

}
