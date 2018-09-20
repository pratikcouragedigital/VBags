package com.mobitechs.vbags.Drawer;

public class DrawerListInstance {

    private static Integer imagePositionInstance;

    public static Integer getDrawerItemListImagePositionInstances(){
        return imagePositionInstance;
    }
    public  static void  setDrawerItemListImagePositionInstances(Integer imagePosition){
        imagePositionInstance = imagePosition;
    }
}
