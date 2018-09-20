package com.mobitechs.vbags.connectivity;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.adapter.Wish_List_Adapter;
import com.mobitechs.vbags.model.Product_ListItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Req_Wish_List {
    private static String webMethod;
    private static String userId, params, url = "";
    private static Context context;
    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<Product_ListItems> ItemsArrayForAsyncTask;
    private static Snackbar snackbar;


    public Req_Wish_List(Context context) {
        this.context = context;
    }

    public static void addToWishList(String userId, String productId) {
        webMethod = "addWishList";
        params = "?method=" + webMethod + "&userId=" + userId + "&productId=" + productId;
        url = context.getString(R.string.url) + params;
        String abc =url;

        AndroidNetworking.get(url)
                .setTag("addWishList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("addWishListResponse");
                            if (!result.equals("LIST_ADDED_SUCCESSFULLY")) {
                                Toast.makeText(context, "Added to wish list.", Toast.LENGTH_SHORT).show();
                            }
                            else if (!result.equals("ALREADY AVAILABLE")) {
                                //Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }

    public static void deleteWishList(String userId, String productId) {
        webMethod = "deleteWishList";
        params = "?method=" + webMethod + "&userId=" + userId + "&productId=" + productId;
        url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("deleteWishList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("deleteWishListResponse");
                            if (!result.equals("wishlist_Deleted")) {
                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(context, "Delete from wish list.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }

    public void getWishListProductList(List<Product_ListItems> listItems, RecyclerView recyclerView, Wish_List_Adapter listAdapter, final int currentPage, String idOfUser) {

        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        url = "";
        userId = idOfUser;

        webMethod = "showWishList";
        params = "?method=" + webMethod + "&currentPage=" + currentPage + "&userId=" + userId;
        url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("showWishList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray arr = response.getJSONArray("showWishListResponse");
                            if(currentPage == 1){
                                ItemsArrayForAsyncTask.clear();
                            }
                            if (arr.length() == 0) {
                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Product_ListItems product_ListItems = new Product_ListItems();
                                    product_ListItems.setProductId(obj.getString("productId"));
                                    product_ListItems.setProductName(obj.getString("productName"));
                                    product_ListItems.setCategoryId(obj.getString("categoryId"));
                                    product_ListItems.setCategoryName(obj.getString("categoryName"));
                                    product_ListItems.setProductPrice(obj.getString("productPrice"));
                                    product_ListItems.setDeliveryCharges(obj.getString("deliveryCharges"));
                                    product_ListItems.setSize(obj.getString("size"));
                                    product_ListItems.setColor(obj.getString("color"));
                                    product_ListItems.setProductShortDescription(obj.getString("productShortDescription"));
                                    product_ListItems.setProductLongDescription(obj.getString("productLongDescription"));
                                    product_ListItems.setRatings(obj.getString("rating"));

                                    if (!obj.getString("img1").isEmpty() && obj.getString("img1") != null) {
                                        product_ListItems.setFirstImagePath(obj.getString("img1"));
                                    }
                                    if (!obj.getString("img2").isEmpty() && obj.getString("img2") != null) {
                                        product_ListItems.set2ndImagePath(obj.getString("img2"));
                                    }
                                    if (!obj.getString("img3").isEmpty() && obj.getString("img3") != null) {
                                        product_ListItems.set3rdImagePath(obj.getString("img3"));
                                    }
                                    if (!obj.getString("img4").isEmpty() && obj.getString("img4") != null) {
                                        product_ListItems.set4thImagePath(obj.getString("img4"));
                                    }
                                    ItemsArrayForAsyncTask.add(product_ListItems);
                                }
                                adapterForAsyncTask.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }
}
