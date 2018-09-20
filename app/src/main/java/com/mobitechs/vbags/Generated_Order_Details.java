package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Generated_Order_ProductList_Adapter;
import com.mobitechs.vbags.adapter.Spinner_Filter_Adapter;
import com.mobitechs.vbags.connectivity.Req_Generated_Order_Details;
import com.mobitechs.vbags.model.My_Order_ProductItems;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Generated_Order_Details extends BaseActivity {

    TextView txtLuckyDrawAmount,txtOrderId,txtDate,txtAddress,txtUserName,txtUserEmails,txtUserMobileNo;
    String  userId,userEmail,userMobile,deliveryStatus,address,date,deliveryCharges,totalPrice,orderId,productPrice,qty,orderBulkId,username;
    public static TextView txtAllProductPrice, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable, txtRemainingAmount;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<My_Order_ProductItems> listItems = new ArrayList<My_Order_ProductItems>();
    private ProgressDialog progressDialog;
    private Generated_Order_ProductList_Adapter listAdapter;
    SessionManager sessionManager;

    static int payableAmount = 0;
    static int freeDeliveryOn = 0;
    int luckyDrawAmount;
    Spinner OrderStatus;
    String OrderStatusName = "";
    String OrderStatusId = "";
    private String[] OrderStatusArrayList;
    private Spinner_Filter_Adapter spinnerAdapter;
    private ProgressDialog OrderStatusDialogBox;
    private List<String> OrderStatusIdList = new ArrayList<String>();
    private List<String> OrderStatusNameList = new ArrayList<String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_order_product_list_details);

        freeDeliveryOn = Integer.parseInt(getResources().getString(R.string.freeDeliveryOn));

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        txtOrderId = (TextView) findViewById(R.id.txtOrderId);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtAddress = (TextView) findViewById(R.id.txtUserAddress);
        txtUserMobileNo = (TextView) findViewById(R.id.txtUserMobileNo);
        txtUserEmails = (TextView) findViewById(R.id.txtUserEmails);

        txtAllProductQty = (TextView) findViewById(R.id.txtAllProductQty);
        txtAllProductPrice = (TextView) findViewById(R.id.txtAllProductPrice);
        txtAllProductDelCharges = (TextView) findViewById(R.id.txtAllProductDelCharges);
        txtLuckyDrawAmount = (TextView) findViewById(R.id.txtLuckyDrawAmount);
        txtAmountPayable = (TextView) findViewById(R.id.txtAmountPayable);
        OrderStatus = (Spinner) findViewById(R.id.spinnerOrderStatus);

        //txtTotalPayableAmount = (TextView) findViewById(R.id.txtTotalPayableAmount);

        Intent intent = getIntent();

        orderId = intent.getStringExtra("OrderId");
        orderBulkId = intent.getStringExtra("OrderBulkId");
        qty = intent.getStringExtra("Qty");
        productPrice = intent.getStringExtra("ProductPrice");
        date = intent.getStringExtra("Date");
        deliveryStatus = intent.getStringExtra("DeliveryStatus");
        deliveryCharges = intent.getStringExtra("DeliveryCharges");
        totalPrice = intent.getStringExtra("TotalPrice");
        username = intent.getStringExtra("Username");
        address = intent.getStringExtra("Address");
        userMobile = intent.getStringExtra("UserMobile");
        userEmail = intent.getStringExtra("UserEmail");
        OrderStatusId = intent.getStringExtra("orderStatusId");
        luckyDrawAmount = Integer.parseInt(intent.getStringExtra("LuckyDrawAmount"));

        txtOrderId.setText(orderId);
        txtDate.setText(date);
        txtAddress.setText(address);
        txtUserName.setText(username);
        txtUserMobileNo.setText(userMobile);
        txtUserEmails.setText(userEmail);

        txtLuckyDrawAmount.setText(""+luckyDrawAmount);
        txtAmountPayable.setText(""+totalPrice);

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        listAdapter = new Generated_Order_ProductList_Adapter(listItems);
        recyclerView.setAdapter(listAdapter);

        getProductList();
        getOrderStatusList();

    }

    private void getOrderStatusList() {
        OrderStatusArrayList = new String[]{
                "Order Status"
        };

        OrderStatusNameList = new ArrayList<>(Arrays.asList(OrderStatusArrayList));
        spinnerAdapter = new Spinner_Filter_Adapter(this, R.layout.spinner_item, OrderStatusNameList);
        getListOfOrderStatus();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OrderStatus.setAdapter(spinnerAdapter);
        OrderStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    OrderStatusName = parent.getItemAtPosition(position).toString();
                    OrderStatusId = OrderStatusIdList.get(position-1);

                    Drawable icon = getResources().getDrawable(R.drawable.ic_launcher);

                    new MaterialStyledDialog.Builder(view.getContext())
                            .setTitle("Confirmation!")
                            .setDescription("Do you really want to change order status?")
                            .setPositiveText("OK")
                            .setIcon(icon)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    ChangeOrderStatus();
                                }
                            })
                            .setNegativeText("CANCEL")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                }
                            })
                            .show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void ChangeOrderStatus() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait. Updating Order Status..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        try{
            Req_Generated_Order_Details statusSpinnerList = new Req_Generated_Order_Details(this);
            statusSpinnerList.ChangeOrderStatus(OrderStatusId, orderId,orderBulkId,progressDialog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getListOfOrderStatus() {
        Req_Generated_Order_Details statusSpinnerList = new Req_Generated_Order_Details(this);
        statusSpinnerList.FetchOrderStatus(OrderStatusNameList, OrderStatusIdList,spinnerAdapter);
    }

    private void getProductList() {
        try {
            Req_Generated_Order_Details getGeneratedOrderDetails = new Req_Generated_Order_Details(this);
            getGeneratedOrderDetails.getGeneratedOrderWiseProductList(listItems, recyclerView, listAdapter, userId, orderBulkId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePriceDetails(int prodcutCount, int allProductPrice, int allproductDelCharges) {


        txtAllProductQty.setText("( " + prodcutCount + " items)");
        txtAllProductPrice.setText(allProductPrice + "");

        try {
            if (allProductPrice > freeDeliveryOn) {
                payableAmount = allProductPrice;
                txtAllProductDelCharges.setText("Free Delivery");
//                txtAmountPayable.setText("Rs. "+ payableAmount);
//                txtTotalPayableAmount.setText("RS. " + payableAmount);

            } else {
                payableAmount = allProductPrice + allproductDelCharges;
                txtAllProductDelCharges.setText(allproductDelCharges + "");
//                txtTotalPayableAmount.setText("RS. " + payableAmount);
//                txtAmountPayable.setText("Rs. "+ payableAmount);

            }
        } catch (Exception ex) {
            Log.d("Exception", "Exception of type" + ex.getMessage());
        }
    }
}