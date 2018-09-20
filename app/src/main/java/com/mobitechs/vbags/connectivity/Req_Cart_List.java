package com.mobitechs.vbags.connectivity;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.adapter.Cart_List_Adapter;
import com.mobitechs.vbags.adapter.Payment_ProductList_Adapter;
import com.mobitechs.vbags.model.CartList_Items;
import com.mobitechs.vbags.singleton.UserWishList_Instance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Req_Cart_List {

    private static Context context;
    private static String webMethod;
    public static String ResponseName = "";
    public static String userId, params, url = "";
    private static int checkRequestState;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<CartList_Items> ItemsArrayForAsyncTask;

    private static ArrayList<String> userWishListId = new ArrayList<String>();

    public Req_Cart_List(Context home) {
        context = home;
    }

    public void getCartList(List<CartList_Items> listItems, RecyclerView recyclerView, Cart_List_Adapter listAdapter, String idOfUser, final int currentPage, int requestState) {

        checkRequestState = requestState;
        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        url = "";
        userId = idOfUser;

        webMethod = "showUserCartList";
        params = "?method=" + webMethod + "&currentPage=" + currentPage + "&userId=" + userId;
        url = context.getString(R.string.url) + params;
        ResponseName = "showUserCartListResponse";

        AndroidNetworking.get(url)
                .setTag("showUserCartList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (!userId.equals("")) {
                            try {

                                if (!response.getJSONArray("userWishList").equals(JSONObject.NULL)) {
                                    if (checkRequestState == 0) {
                                        JSONArray wishListArr = response.getJSONArray("userWishList");

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
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            JSONArray arr = response.getJSONArray(ResponseName);
                            if(currentPage == 1){
                                ItemsArrayForAsyncTask.clear();
                            }
                            if (arr.length() == 0) {
                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    CartList_Items cartList_Items = new CartList_Items();

                                    cartList_Items.setuserId(obj.getString("userId"));
                                    cartList_Items.setusername(obj.getString("userFirstname") + " " + obj.getString("userLastName"));
                                    cartList_Items.setcartId(obj.getString("cartId"));
                                    cartList_Items.setproductId(obj.getString("productId"));
                                    cartList_Items.setproductName(obj.getString("productName"));
                                    cartList_Items.setcategoryId(obj.getString("categoryId"));
                                    cartList_Items.setcategoryName(obj.getString("categoryName"));
                                    cartList_Items.setqty(obj.getString("qty"));
                                    cartList_Items.setproductPrice(obj.getString("productPrice"));
                                    cartList_Items.setdeliveryCharges(obj.getString("deliveryCharges"));
                                    cartList_Items.settotalPrice(obj.getString("totalPrice"));
                                    cartList_Items.setsize(obj.getString("size"));
                                    cartList_Items.setcolor(obj.getString("color"));
                                    cartList_Items.setproductShortDescription(obj.getString("productShortDescription"));
                                    cartList_Items.setproductLongDescription(obj.getString("productLongDescription"));
                                    cartList_Items.setratings(obj.getString("rating"));
                                    if (!obj.getString("img1").isEmpty() && obj.getString("img1") != null) {
                                        cartList_Items.setFirstImagePath(obj.getString("img1"));
                                    }

                                    ItemsArrayForAsyncTask.add(cartList_Items);
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

    public static void addToCartList(String userId, String productId, String qty, String productPrice, String deliveryCharges, int totalPrice) {
        url = "";
        webMethod = "addToCart";
        params = "?method=" + webMethod + "&userId=" + userId + "&productId=" + productId + "&qty=" + qty + "&productPrice=" + productPrice + "&deliveryCharges=" + deliveryCharges + "&totalPrice=" + totalPrice;
        url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("addToCart")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("addToCartResponse");
                            if (result.equals("LIST_ADDED_SUCCESSFULLY")) {
                                Toast.makeText(context, "Successfully added to cart", Toast.LENGTH_SHORT).show();
                            } else {
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

    public static void updateCartList(String userId, String cartId,String productId, int qty,  int totalPrice) {
        url = "";
        webMethod = "updateToCart";
        params = "?method=" + webMethod + "&cartId=" + cartId +"&userId=" + userId + "&totalPrice=" + totalPrice + "&qty=" + qty + "&productId=" + productId +"";
        url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("updateToCart")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("updateToCartResponse");
                            if (result.equals("LIST_UPDATED_SUCCESSFULLY")) {
                                //Toast.makeText(context, "Cart successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error)   {
                        error.getErrorDetail();
                    }
                });
    }

    public static void removeFromCartList(String userId, String productId, String cartId) {
        url = "";
        webMethod = "deleteFromCart";
        params = "?method=" + webMethod + "&userId=" + userId + "&productId=" + productId+ "&cartId=" + cartId;
        url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("deleteCartListResponse")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("deleteCartListResponse");
                            if (!result.equals("Deleted_From_Cart")) {
                                Toast.makeText(context, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Successfully removed from cart", Toast.LENGTH_SHORT).show();
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


    public void getPaymentProductList(List<CartList_Items> listItems, RecyclerView recyclerView, Payment_ProductList_Adapter listAdapter, ArrayList<String> productIdArray, ArrayList<String> productNameArray,ArrayList<String> productImageArray, ArrayList<String> productPriceArray, ArrayList<String> productQtyArray, ArrayList<String> productDeliveryChargesArray, ArrayList<String> productPriceWithQtyArray) {
        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        for (int i = 0; i < productIdArray.size(); i++) {
            CartList_Items cartList_Items = new CartList_Items();

            cartList_Items.setproductId(productIdArray.get(i));
            cartList_Items.setproductName(productNameArray.get(i));
            cartList_Items.setproductPrice(productPriceArray.get(i));
            cartList_Items.setqty(productQtyArray.get(i));
            cartList_Items.setdeliveryCharges(productDeliveryChargesArray.get(i));
            cartList_Items.settotalPrice(productPriceWithQtyArray.get(i));
            cartList_Items.setImagePath(productImageArray.get(i));

            ItemsArrayForAsyncTask.add(cartList_Items);
        }
        adapterForAsyncTask.notifyDataSetChanged();
    }
}
