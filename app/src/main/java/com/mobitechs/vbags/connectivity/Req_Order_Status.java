package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Add_Category;
import com.mobitechs.vbags.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Req_Order_Status {

    Context context;
    String webMethName;
    private ProgressDialog progressDialog;

    public void AddCategory(String orderId, String userId, final ProgressDialog progressDialog) {

        webMethName= "cancelOrder";

        String url = context.getString(R.string.url)+ "?method=" + webMethName+"&orderId="+orderId+"&userId="+userId;
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
                            if(res.equals("Order_Cancel")){
                                Toast.makeText(context, "Category Cancel Successfully.", Toast.LENGTH_SHORT).show();
                                Intent gotoAddCategory = new Intent(context, Add_Category.class);
                                gotoAddCategory.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoAddCategory);
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
