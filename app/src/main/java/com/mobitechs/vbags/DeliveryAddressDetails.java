package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.AddressDetails_Adapter;
import com.mobitechs.vbags.connectivity.Req_Address_Details;
import com.mobitechs.vbags.model.AddressDetails_Items;
import com.mobitechs.vbags.model.CartList_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.singleton.Address_Instance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class DeliveryAddressDetails extends BaseActivity implements View.OnClickListener {

    private static final String TAG = DeliveryAddressDetails.class.getSimpleName();
    private static int currentPage = 1;
    int addAddressCount = 0;
    List<CartList_Items> cartList_items;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<AddressDetails_Items> listItems = new ArrayList<AddressDetails_Items>();
    private ProgressDialog progressDialog;
    private AddressDetails_Adapter listAdapter;
    SessionManager sessionManager;
    RelativeLayout  addressDetailsLayout, recyclerViewLayout;
    RelativeLayout notLoggedInLayout;
    RelativeLayout containerLayout;
    EditText txtAddressLine1, txtAddressLine2, txtCity, txtState, txtCountry, txtPincode;
    Button btnSubmitAddress, btnDeliverHere,btnAddAddress;

    String userId = "", userEmail, userMobile, userName, payableAmount,isLuckyDrawDone, luckyDrawAmount="0";
    String deliveryAddressId = "";
    String fullAddress, addressLine1, addressLine2, userCity, userState, userCountry, userPincode;

    ArrayList<String> cartIdArray = new ArrayList<String>();
    ArrayList<String> productIdArray = new ArrayList<String>();
    ArrayList<String> productNameArray = new ArrayList<String>();
    ArrayList<String> productImageArray = new ArrayList<String>();
    ArrayList<String> productPriceArray = new ArrayList<String>();
    ArrayList<String> productQtyArray = new ArrayList<String>();
    ArrayList<String> productDeliveryChargesArray = new ArrayList<String>();
    ArrayList<String> productPriceWithQtyArray = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address_details);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);
        userEmail = user.get(sessionManager.KEY_USEREMAIL);
        userMobile = user.get(sessionManager.KEY_CONTACTNO);
        userName = user.get(sessionManager.KEY_FIRST_NAME) + " " + user.get(sessionManager.KEY_LAST_NAME);

        recyclerView = (RecyclerView) findViewById(R.id.addressRecyclerView);
        notLoggedInLayout = (RelativeLayout) findViewById(R.id.notLoggedInLayout);
        containerLayout = (RelativeLayout) findViewById(R.id.containerLayout);
        notLoggedInLayout.setVisibility(View.GONE);

        if (userId == null) {
            userId = "";
        }
        if (userId.equals("") || userId == null) {
            notLoggedInLayout.setVisibility(View.VISIBLE);
            containerLayout.setVisibility(GONE);
        } else {

            Intent intent = getIntent();
            if (null != intent) {
                cartIdArray = intent.getStringArrayListExtra("cartIdArray");
                productIdArray = intent.getStringArrayListExtra("productIdArray");
                productNameArray = intent.getStringArrayListExtra("productNameArray");
                productImageArray = intent.getStringArrayListExtra("productImageArray");
                productPriceArray = intent.getStringArrayListExtra("productPriceArray");
                productQtyArray = intent.getStringArrayListExtra("productQtyArray");
                productDeliveryChargesArray = intent.getStringArrayListExtra("productDeliveryChargesArray");
                productPriceWithQtyArray = intent.getStringArrayListExtra("productPriceWithQtyArray");
                payableAmount = intent.getStringExtra("totalPayableAmount");
            }

            recyclerViewLayout = (RelativeLayout) findViewById(R.id.recyclerViewLayout);
            addressDetailsLayout = (RelativeLayout) findViewById(R.id.addressDetailsLayout);
            txtAddressLine1 = (EditText) findViewById(R.id.txtAddressLine1);
            txtAddressLine2 = (EditText) findViewById(R.id.txtAddressLine2);
            txtCity = (EditText) findViewById(R.id.txtCity);
            txtState = (EditText) findViewById(R.id.txtState);
            txtCountry = (EditText) findViewById(R.id.txtCountry);
            txtPincode = (EditText) findViewById(R.id.txtPincode);

            btnSubmitAddress = (Button) findViewById(R.id.btnSubmitAddress);
            btnDeliverHere = (Button) findViewById(R.id.btnDeliverHere);
            btnAddAddress = (Button) findViewById(R.id.btnAddAddress);

            btnSubmitAddress.setOnClickListener(this);
            btnDeliverHere.setOnClickListener(this);
            btnAddAddress.setOnClickListener(this);

            addressDetailsLayout.setVisibility(View.GONE);
            btnDeliverHere.setVisibility(View.GONE);

            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.smoothScrollToPosition(0);
            listAdapter = new AddressDetails_Adapter(listItems, btnDeliverHere, btnAddAddress);
            recyclerView.setAdapter(listAdapter);

            HashMap<String, String> luckyAmount = sessionManager.getLuckyDrawAmount();
            luckyDrawAmount = luckyAmount.get(sessionManager.KEY_LUCKYDRAW_AMOUNT);
            isLuckyDrawDone = luckyAmount.get(sessionManager.KEY_IS_LUCKYDRAW_DONE);

            getAddressList();

            //Slide animation
            Transition enterTansition = TransitionInflater.from(this).inflateTransition(R.transition.slide);
            getWindow().setEnterTransition(enterTansition);


            getWindow().setAllowEnterTransitionOverlap(false);
        }
    }



    public void getAddressList() {
        try {
            Req_Address_Details req_AddressDetails = new Req_Address_Details(this);
            req_AddressDetails.getAddressList(listItems, recyclerView, listAdapter, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnDeliverHere) {
            Address_Instance address_Instance = new Address_Instance();
            deliveryAddressId = address_Instance.getAddressId();
            fullAddress = address_Instance.getfullAddress();
            if (deliveryAddressId.equals("") || deliveryAddressId == null) {
                Toast.makeText(this, "Please select address", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent gotoPaymentActivity = new Intent(this, LuckyDraw.class);
                gotoPaymentActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                gotoPaymentActivity.putExtra("fullAddress", fullAddress);
                gotoPaymentActivity.putExtra("deliveryAddressId", deliveryAddressId);
                gotoPaymentActivity.putExtra("totalPayableAmount", payableAmount);

                gotoPaymentActivity.putStringArrayListExtra("cartIdArray", cartIdArray);
                gotoPaymentActivity.putStringArrayListExtra("productIdArray", productIdArray);
                gotoPaymentActivity.putStringArrayListExtra("productNameArray", productNameArray);
                gotoPaymentActivity.putStringArrayListExtra("productImageArray", productImageArray);
                gotoPaymentActivity.putStringArrayListExtra("productPriceArray", productPriceArray);
                gotoPaymentActivity.putStringArrayListExtra("productQtyArray", productQtyArray);
                gotoPaymentActivity.putStringArrayListExtra("productDeliveryChargesArray", productDeliveryChargesArray);
                gotoPaymentActivity.putStringArrayListExtra("productPriceWithQtyArray", productPriceWithQtyArray);
                startActivity(gotoPaymentActivity);
            }
        }
        else if (v.getId() == R.id.btnAddAddress) {

            if (addAddressCount == 0) {
                addressDetailsLayout.setVisibility(View.VISIBLE);
                recyclerViewLayout.setVisibility(View.GONE);
                addAddressCount = 1;
            } else {
                addressDetailsLayout.setVisibility(View.GONE);
                recyclerViewLayout.setVisibility(View.VISIBLE);
                addAddressCount = 0;
            }
        } else if (v.getId() == R.id.btnSubmitAddress) {

            addressLine1 = txtAddressLine1.getText().toString();
            addressLine2 = txtAddressLine2.getText().toString();
            userCity = txtCity.getText().toString();
            userState = txtState.getText().toString();
            userCountry = txtCountry.getText().toString();
            userPincode = txtPincode.getText().toString();

            if (txtAddressLine1.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Address.", Toast.LENGTH_LONG).show();
            } else if (txtCity.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter City.", Toast.LENGTH_LONG).show();
            } else if (txtState.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter State.", Toast.LENGTH_LONG).show();
            } else if (txtCountry.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Country.", Toast.LENGTH_LONG).show();
            } else if (txtPincode.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Pin Code.", Toast.LENGTH_LONG).show();
            } else {
                Add_Address();
            }
        }
    }

    public void Add_Address() {
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait");
        pd.show();

        String method = "AddAddressDetails";
        JSONObject jsonObject = new JSONObject();
        try {
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

        String url = getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("AddAddressDetails")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        try {
                            String result = response.getString("AddAddressDetailsResponse");
                            if (result.equals("ADDRESS_SAVED")) {
                                Toast.makeText(DeliveryAddressDetails.this, "Address successfully added", Toast.LENGTH_SHORT).show();
//                                Intent refreshThis = new Intent(context,DeliveryAddressDetails.class);
//                                refreshThis.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(refreshThis);
                                getAddressList();
                                addressDetailsLayout.setVisibility(View.GONE);
                                recyclerViewLayout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(DeliveryAddressDetails.this, "Please try again later", Toast.LENGTH_SHORT).show();
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
