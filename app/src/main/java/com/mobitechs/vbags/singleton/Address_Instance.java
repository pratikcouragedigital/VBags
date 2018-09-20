package com.mobitechs.vbags.singleton;

import java.util.ArrayList;


public class Address_Instance {

    private static String addressId = "";
    private static String fullAddress = "";

    public static String getAddressId() {
        return addressId;
    }
    public static void setAddressId(String userAddressId) {
        addressId = userAddressId;
    }

    public static String getfullAddress() {
        return fullAddress;
    }
    public static void setfullAddress(String ufullAddress) {
        fullAddress = ufullAddress;
    }
}
