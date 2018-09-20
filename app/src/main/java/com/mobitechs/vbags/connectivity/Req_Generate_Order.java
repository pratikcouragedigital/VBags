package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.DeliveryAddressDetails;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.PaymentActivity;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.model.CartList_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Req_Generate_Order {

    Context context;

    private ProgressDialog progressDialog;
    SessionManager sessionManager;
    List<CartList_Items> cartListItems;
    String idOfUser,name,email,mobileNo;
    int totalPayableAmount;
    String paymentType;
    public Req_Generate_Order(Context con) {
        context = con;
    }

    public Req_Generate_Order(DeliveryAddressDetails DeliveryAddressDetails) {
        context = DeliveryAddressDetails;
    }

    public void GenarateOrder(String typeOfPayment, String userId, String razorpayPaymentID, String userName, String userMobile, String userEmail, String fullAddress, int payableAmount, int luckyDrawPrice, String deliveryAddressId, ArrayList<String> cartIdArray, ArrayList<String> productIdArray, List<String> productNameArray, ArrayList<String> productPriceArray, ArrayList<String> productQtyArray, ArrayList<String> productPriceWithQtyArray, ArrayList<String> productDeliveryChargesArray) {
        sessionManager = new SessionManager(context);

        String totalPayableAmount = String.valueOf(payableAmount);
        String method ="orderGeneration";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("userName", userName);
            jsonObject.put("mobileNo", userMobile);
            jsonObject.put("email", userEmail);
            jsonObject.put("shipmentAddressId", deliveryAddressId);
            jsonObject.put("fullAddress", fullAddress);
            jsonObject.put("razorpayPaymentID", razorpayPaymentID);
            jsonObject.put("paymentType", typeOfPayment);
            jsonObject.put("totalPayableAmount", totalPayableAmount);
            jsonObject.put("luckyDrawPrice", luckyDrawPrice);

            jsonObject.put("method", method);

            JSONArray arrayOfCartID = new JSONArray(cartIdArray);
            jsonObject.put("cartIdArray", arrayOfCartID.toString());

            JSONArray arrayOfProductID = new JSONArray(productIdArray);
            jsonObject.put("productId", arrayOfProductID.toString());

            JSONArray arrayOfProductName = new JSONArray(productNameArray);
            jsonObject.put("productName", arrayOfProductName.toString());

            JSONArray arrayOfProductPrice = new JSONArray(productPriceArray);
            jsonObject.put("productPrice", arrayOfProductPrice.toString());

            JSONArray arrayOfProductQty = new JSONArray(productQtyArray);
            jsonObject.put("qty", arrayOfProductQty.toString());

            JSONArray arrayOfProductPriceDeliveryCharges = new JSONArray(productDeliveryChargesArray);
            jsonObject.put("deliveryCharges", arrayOfProductPriceDeliveryCharges.toString());

            JSONArray arrayOfProductPriceWithQty = new JSONArray(productPriceWithQtyArray);
            jsonObject.put("totalPriceWithDelCharges", arrayOfProductPriceWithQty.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = context.getString(R.string.url);
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("orderGeneration")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressDialog.dismiss();

                        try {
                            int orderId = response.getInt("orderGenerationResponse");
                            Toast.makeText(context, "Your Order has been generated. Your order id is: "+orderId+" ,also you can check your mail.", Toast.LENGTH_LONG).show();
                            Intent gotoHome = new Intent(context,MainActivity.class);
                            gotoHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(gotoHome);

                            //order added generated so user can try their luck with new order
                            sessionManager.SetLuckyDrawAmount("0", "No");
                            //remove from cart if this product available
                        }
                        catch(Exception e) {
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
