package com.mobitechs.vbags;

public class URL_WebService {


    private static String url = "http://mobitechs.in/VBagsAPI/api/vbags_api.php";
    private static String imageUrl = "http://mobitechs.in/VBagsAPI/product_images/";
//    private static String url = "http://192.168.0.103/VBagsAPI/php/api/vbags_api.php";
//    private static String imageUrl = "http://192.168.0.103/VBagsAPI/product_images/";


    public static String getUrl() {
        return url;
    }
    public static String getImageUrl() {
        return imageUrl;
    }

    private URL_WebService() {
    }
}
