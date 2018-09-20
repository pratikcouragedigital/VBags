package com.mobitechs.vbags.singleton;

public class My_Custom_Bag_Instance {
    
    
    private static String image = "";
    private static String color = "";

    public static String getBagImage() {
        return image;
    }
    public static void setBagImage(String bagImage) {
        image = bagImage;
    }

    public static String getBagColor() {
        return color;
    }
    public static void setBagColor(String ucolor) {
        color = ucolor;
    }
}
