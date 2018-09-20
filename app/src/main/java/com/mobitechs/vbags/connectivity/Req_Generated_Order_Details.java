package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Add_Category;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.adapter.Generated_Order_List_Adapter;
import com.mobitechs.vbags.adapter.Generated_Order_ProductList_Adapter;
import com.mobitechs.vbags.adapter.My_Oder_Adapter;
import com.mobitechs.vbags.adapter.My_Order_ProductList_Adapter;
import com.mobitechs.vbags.adapter.Spinner_Filter_Adapter;
import com.mobitechs.vbags.model.My_Order_ProductItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Req_Generated_Order_Details {

    private static Context context;
    private String webMethod;
    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<My_Order_ProductItems> ItemsArrayForAsyncTask;

    public Req_Generated_Order_Details(Context home) {
        context = home;
    }


    public void getGeneratedOrderList(List<My_Order_ProductItems> listItems, RecyclerView recyclerView, Generated_Order_List_Adapter listAdapter, final int currentPage) {

        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;

        webMethod = "showGeneratedOrders";
        String params = "?method=" + webMethod + "&currentPage=" + currentPage;
        String url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("showUserOrders")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ItemsArrayForAsyncTask.clear();
                        try {
                            String  res =  response.getString("showUserOrdersResponse");
                            if (res.equals("Order Not Available.")) {
                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray arr = response.getJSONArray("showUserOrdersResponse");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    My_Order_ProductItems myOrder_ProductItems = new My_Order_ProductItems();

                                    myOrder_ProductItems.setorderId(obj.getString("orderId"));
                                    myOrder_ProductItems.setorderBulkId(obj.getString("orderBulkId"));
                                    myOrder_ProductItems.setordered_Date(obj.getString("entryDate"));
                                    myOrder_ProductItems.setdeleveryStatus(obj.getString("statusName"));
                                    myOrder_ProductItems.setorderStatusId(obj.getString("orderStatusId"));
                                    myOrder_ProductItems.setdeliveryCharges(obj.getString("orderDeliveryChages"));
                                    myOrder_ProductItems.settotalPrice(obj.getString("totalPrice"));
                                    myOrder_ProductItems.setEmail(obj.getString("userEmail"));
                                    myOrder_ProductItems.setmobileNo(obj.getString("userMobileNo"));
                                    myOrder_ProductItems.setluckyDrawPrice(obj.getString("luckyDrawPrice"));

                                    String userName = obj.getString("userFirstname") +" "+ obj.getString("userLastName");
                                    myOrder_ProductItems.setusername(userName);

                                    String address = obj.getString("addressLine1") + " "+obj.getString("addressLine2")+" "+obj.getString("city")+" "+obj.getString("state")+" "+obj.getString("pincode")+" "+obj.getString("country");
                                    myOrder_ProductItems.setaddress(address);

                                    ItemsArrayForAsyncTask.add(myOrder_ProductItems);
                                    adapterForAsyncTask.notifyDataSetChanged();
                                }
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

    public void getGeneratedOrderWiseProductList(List<My_Order_ProductItems> listItems, RecyclerView recyclerView, Generated_Order_ProductList_Adapter listAdapter, String userId,  String orderBulkId) {
        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;

        webMethod = "showGeneratedOrdersWiseProductList";
        String params = "?method=" + webMethod + "&orderBulkId=" + orderBulkId;
        String url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("showUserOrders")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray arr = response.getJSONArray("showUserOrdersResponse");
                            if (arr.length() == 0) {
                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    My_Order_ProductItems myOrder_ProductItems = new My_Order_ProductItems();

                                    myOrder_ProductItems.setorderId(obj.getString("orderId"));
                                    myOrder_ProductItems.setorderBulkId(obj.getString("orderBulkId"));
                                    myOrder_ProductItems.setordered_Date(obj.getString("entryDate"));
                                    myOrder_ProductItems.setproductId(obj.getString("productId"));
                                    myOrder_ProductItems.setproductName(obj.getString("productName"));
                                    myOrder_ProductItems.setcategory(obj.getString("categoryName"));
                                    myOrder_ProductItems.setproductPrice(obj.getString("productPrice"));
                                    myOrder_ProductItems.setqty(obj.getString("qty"));
                                    myOrder_ProductItems.setdeliveryCharges(obj.getString("orderDeliveryChages"));
                                    myOrder_ProductItems.settotalPrice(obj.getString("totalPrice"));
                                    myOrder_ProductItems.setcolor(obj.getString("color"));
                                    myOrder_ProductItems.setsize(obj.getString("size"));
                                    myOrder_ProductItems.setproductShortDescription(obj.getString("productShortDescription"));
                                    myOrder_ProductItems.setproductLongDescription(obj.getString("productLongDescription"));

                                    myOrder_ProductItems.setratings(obj.getString("rating"));
                                    myOrder_ProductItems.setdeleveryStatus(obj.getString("statusName"));

                                    String userName = obj.getString("userFirstname") + obj.getString("userLastName");
                                    myOrder_ProductItems.setusername(userName);


                                    String address = obj.getString("addressLine1") + obj.getString("addressLine2")+obj.getString("city")+obj.getString("state")+obj.getString("pincode")+obj.getString("country");
                                    myOrder_ProductItems.setaddress(address);

                                    if (!obj.getString("img1").isEmpty() && obj.getString("img1") != null) {
                                        myOrder_ProductItems.setimagePath(obj.getString("img1"));
                                    }


                                    ItemsArrayForAsyncTask.add(myOrder_ProductItems);
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


    public void FetchOrderStatus( final List<String> orderStatusNameList, final List<String> orderStatusIdList, final Spinner_Filter_Adapter spinnerAdapter) {

        String webMethod = "OrderStatusList";
        String params =  "?method=" + webMethod;
        String url = context.getString(R.string.url) + params;
        AndroidNetworking.get(url)
                .setTag("OrderStatusList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("OrderStatusResponse");

                            if (arr.length() == 0) {
                                // Toast.makeText(this, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
//                                orderStatusNameList.add("Order Status");
//                                orderStatusIdList.add("0");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    orderStatusIdList.add(obj.getString("orderStatusId"));
                                    orderStatusNameList.add(obj.getString("statusName"));

                                }
                                spinnerAdapter.notifyDataSetChanged();
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

    public void ChangeOrderStatus(String orderStatusId, String orderId, String orderBulkId, final ProgressDialog progressDialog) {

        String webMethName= "UpdateOrderStatus";

        String url = context.getString(R.string.url)+ "?method="+webMethName+"&orderId="+orderId+"&orderBulkId="+orderBulkId+"&orderStatusId="+orderStatusId;
        AndroidNetworking.get(url)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("cancelOrderResponse");
                            if(res.equals("Order_Status_Changed")){
                                Toast.makeText(context, "Order Status Successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
