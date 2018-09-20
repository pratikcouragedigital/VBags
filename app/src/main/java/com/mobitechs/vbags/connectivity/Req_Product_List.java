package com.mobitechs.vbags.connectivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.URL_WebService;
import com.mobitechs.vbags.adapter.Product_List_adapter;
import com.mobitechs.vbags.model.Product_ListItems;
import com.mobitechs.vbags.singleton.UserWishList_Instance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Req_Product_List {

    private static Context context;
    private String webMethod;
    private URL_WebService URL;
    public String ResponseName = "";
    String userId, params, url = "";
    private static int checkRequestState, currentPage;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<Product_ListItems> ItemsArrayForAsyncTask;

    private static ArrayList<String> userWishListId = new ArrayList<String>();

    public Req_Product_List(Context home) {
        context = home;
    }

    public void getProductList(final ProgressBar progressBar, List<Product_ListItems> listItems, RecyclerView recyclerView, Product_List_adapter listAdapter, int currPage, String categoryId, String requestFor, String idOfUser, int requestState) {

        currentPage = currPage;
        checkRequestState = requestState;
        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        url = "";
        userId = idOfUser;

        if (userId == null) {
            userId = "";
        }

        if (requestFor.equals("allProduct")) {
            webMethod = "productList";
            params = "?method=" + webMethod + "&currentPage=" + currentPage + "&userId=" + userId;
            url = context.getString(R.string.url) + params;
            ResponseName = "productListResponse";
        } else if (requestFor.equals("categoryWiseProduct")) {
            webMethod = "categoryWiseProductList";
            params = "?method=" + webMethod + "&categoryId=" + categoryId + "&currentPage=" + currentPage + "&userId=" + userId;
            url = context.getString(R.string.url) + params;
            ResponseName = "categoryWiseProductListResponse";
        }

        AndroidNetworking.get(url)
                .setTag("productList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        if (checkRequestState == 0) {
                            if (userId.equals("") || userId == null) {

                            } else {
                                if (currentPage == 1) {

                                    try {
                                        JSONArray wishListArr = response.getJSONArray("userWishListResponse");

                                        for (int i = 0; i < wishListArr.length(); i++) {
                                            try {
                                                JSONObject obj = wishListArr.getJSONObject(i);
                                                String item = (obj.getString("productId"));
                                                userWishListId.add(item);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        UserWishList_Instance userWishListInstance = new UserWishList_Instance();
                                        userWishListInstance.setWishList(userWishListId);
                                        checkRequestState = 1;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        try {
                            JSONArray arr = response.getJSONArray(ResponseName);
                            if (currentPage == 1) {
                                ItemsArrayForAsyncTask.clear();
                            }
                            if (arr.length() == 0) {
//                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
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
                                    //product_ListItems.setPostDate(obj.getString("post_date"));
                                    //product_ListItems.setAdminId(obj.getString("userId"));
//                                product_ListItems.setFirstImagePath(obj.getString("productImageName"));
//                                product_ListItems.setSecondImagePath(obj.getString("second_image_path"));
//                                product_ListItems.setThirdImagePath(obj.getString("third_image_path"));
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
                        progressBar.setVisibility(View.GONE);
                        error.getErrorDetail();
                    }
                });
    }

    public void getPriceFilterProductList(List<Product_ListItems> listItems, RecyclerView recyclerView, Product_List_adapter listAdapter, int requestState, int currPage, int minimumVal, int maximumVal, String idOfUser) {

        currentPage = currPage;
        checkRequestState = requestState;
        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        url = "";
        userId = idOfUser;

        if (userId == null) {
            userId = "";
        }

        webMethod = "PriceRangeWiseProductList";
        params = "?method=" + webMethod + "&currentPage=" + currentPage + "&userId=" + userId + "&minVal=" + minimumVal + "&maxVal=" + maximumVal;
        url = context.getString(R.string.url) + params;
        ResponseName = "PriceRangeWiseProductListResponse";

        AndroidNetworking.get(url)
                .setTag("productList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (checkRequestState == 0) {
                            if (userId.equals("") || userId == null) {

                            } else {
                                if (currentPage == 1) {

                                    try {
                                        JSONArray wishListArr = response.getJSONArray("userWishListResponse");

                                        for (int i = 0; i < wishListArr.length(); i++) {
                                            try {
                                                JSONObject obj = wishListArr.getJSONObject(i);
                                                String item = (obj.getString("productId"));
                                                userWishListId.add(item);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        UserWishList_Instance userWishListInstance = new UserWishList_Instance();
                                        userWishListInstance.setWishList(userWishListId);
                                        checkRequestState = 1;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        try {
                            JSONArray arr = response.getJSONArray(ResponseName);
                            if (currentPage == 1) {
                                ItemsArrayForAsyncTask.clear();
                            }
                            if (arr.length() == 0) {
//                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
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
                                    //product_ListItems.setPostDate(obj.getString("post_date"));
                                    //product_ListItems.setAdminId(obj.getString("userId"));
//                                product_ListItems.setFirstImagePath(obj.getString("productImageName"));
//                                product_ListItems.setSecondImagePath(obj.getString("second_image_path"));
//                                product_ListItems.setThirdImagePath(obj.getString("third_image_path"));
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
