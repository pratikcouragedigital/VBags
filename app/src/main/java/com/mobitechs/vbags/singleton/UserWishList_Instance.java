package com.mobitechs.vbags.singleton;


import com.mobitechs.vbags.model.CartList_Items;

import java.util.ArrayList;
import java.util.List;

public class UserWishList_Instance {
    private static ArrayList<String> wishListId = new ArrayList<String>();

    public static List<CartList_Items> listItems = new ArrayList<CartList_Items>();
    public static List<String> productNameArray;

    public static ArrayList<String> getWishList() {
        return wishListId;
    }
    public static void setWishList(ArrayList<String> wiListId) {
        wishListId = wiListId;
    }


    public static List<CartList_Items> getCartList() {
        return listItems;
    }
    public static void setCartList(List<CartList_Items> cartList) {
        listItems = cartList;
    }

    public static List<String> getproductNameArray() {
        return productNameArray;
    }
    public static void setproductNameArray(List<String> productNameArrayc) {
        productNameArray = productNameArrayc;
    }
}
