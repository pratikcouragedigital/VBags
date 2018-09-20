package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.DeliveryAddressDetails;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.adapter.AddressDetails_Adapter;
import com.mobitechs.vbags.model.AddressDetails_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Req_Address_Details {

    private static Context context;
    private static String webMethod;
    public static String ResponseName = "";
    public static String userId, params, url = "";
    private static int checkRequestState;

    private ProgressDialog progressDialog;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<AddressDetails_Items> ItemsArrayForAsyncTask;

    public Req_Address_Details(DeliveryAddressDetails deliveryAddressDetails) {
        context = deliveryAddressDetails;
    }

    public Req_Address_Details(Context con) {
        context = con;
    }

    public void getAddressList(List<AddressDetails_Items> listItems, RecyclerView recyclerView, AddressDetails_Adapter listAdapter, String idOfUser) {

        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;
        url = "";
        userId = idOfUser;

        webMethod = "getAddressDetails";
        params = "?method=" + webMethod + "&userId=" + userId;
        url = context.getString(R.string.url) + params;
        ResponseName = "getAddressDetailsResponse";

        AndroidNetworking.get(url)
                .setTag("getAddressDetails")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray arr = response.getJSONArray(ResponseName);

                            if (arr.length() == 0) {
                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    AddressDetails_Items addressDetails_Items = new AddressDetails_Items();

                                    String userName = obj.getString("userFirstname") +" "+ obj.getString("userLastName");
                                    addressDetails_Items.setUsername(userName);

                                    addressDetails_Items.setAddressLine1(obj.getString("addressLine1"));
                                    addressDetails_Items.setAddressLine2(obj.getString("addressLine2"));
                                    addressDetails_Items.setCity(obj.getString("city"));
                                    addressDetails_Items.setState(obj.getString("state"));
                                    addressDetails_Items.setPincode(obj.getString("pincode"));
                                    addressDetails_Items.setCountry(obj.getString("country"));

                                    addressDetails_Items.setAddressId(obj.getString("addressId"));
                                    addressDetails_Items.setEmail(obj.getString("userEmail"));
                                    addressDetails_Items.setMobileNo(obj.getString("userMobileNo"));

                                    ItemsArrayForAsyncTask.add(addressDetails_Items);
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


    public void Update_Address(String addressId,String userId, String addressLine1, String addressLine2, String userCity, String userState, String userCountry, String userPincode, ProgressDialog pd) {

        progressDialog = pd ;
        String method ="UpdateAddressDetails";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("addressId", addressId);
            jsonObject.put("userId", userId);
            jsonObject.put("method", method);
            jsonObject.put("addressLine1", addressLine1);
            jsonObject.put("addressLine2", addressLine2);
            jsonObject.put("city", userCity);
            jsonObject.put("state", userState);
            jsonObject.put("country", userCountry);
            jsonObject.put("pinCode", userPincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("UpdateAddressDetails")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String result = response.getString("UpdateAddressDetailsResponse");
                            if (result.equals("ADDRESS_UPDATED")) {
                                Toast.makeText(context, "Address successfully updated", Toast.LENGTH_SHORT).show();
                                Intent refreshThis = new Intent(context,DeliveryAddressDetails.class);
                                refreshThis.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(refreshThis);
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
}
