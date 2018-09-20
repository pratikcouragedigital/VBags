package com.mobitechs.vbags;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.My_Order_ProductList_Adapter;
import com.mobitechs.vbags.connectivity.Req_My_Order_List;
import com.mobitechs.vbags.model.My_Order_ProductItems;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class My_Orders_Details extends BaseActivity {

    TextView txtLuckyDrawAmount, txtOrderId,txtDate,txtAddress,txtUserName,txtUserEmails,txtUserMobileNo;
    String  userId,userEmail,userMobile,deliveryStatus,address,date,deliveryCharges,totalPrice,orderId,productPrice,qty,orderBulkId,username;
    public static TextView txtAllProductPrice, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable, txtRemainingAmount;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<My_Order_ProductItems> listItems = new ArrayList<My_Order_ProductItems>();
    private ProgressDialog progressDialog;
    private My_Order_ProductList_Adapter listAdapter;
    SessionManager sessionManager;

    int orderStatusid= 1;
    int luckyDrawAmount;
    static int payableAmount = 0;
    static int freeDeliveryOn = 0;
    StepView stepView;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_product_list_details);

        freeDeliveryOn = Integer.parseInt(getResources().getString(R.string.freeDeliveryOn));

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

//        stepView = findViewById(R.id.step_view);
        final StepView stepView = findViewById(R.id.step_view);
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
        //txtTotalPayableAmount = (TextView) findViewById(R.id.txtTotalPayableAmount);

        Intent intent = getIntent();

        orderId = intent.getStringExtra("OrderId");
        orderBulkId = intent.getStringExtra("OrderBulkId");
        qty = intent.getStringExtra("Qty");
        productPrice = intent.getStringExtra("ProductPrice");
        date = intent.getStringExtra("Date");
        orderStatusid = Integer.parseInt(intent.getStringExtra("OrderStatusId"));
        deliveryStatus = intent.getStringExtra("DeliveryStatus");
        deliveryCharges = intent.getStringExtra("DeliveryCharges");
        totalPrice = intent.getStringExtra("TotalPrice");
        username = intent.getStringExtra("Username");
        address = intent.getStringExtra("Address");
        userMobile = intent.getStringExtra("UserMobile");
        userEmail = intent.getStringExtra("UserEmail");
        luckyDrawAmount = Integer.parseInt(intent.getStringExtra("LuckyDrawAmount"));

        txtOrderId.setText(orderId);
        txtDate.setText(date);
        txtAddress.setText(address);
        txtUserName.setText(username);
        txtUserMobileNo.setText(userMobile);
        txtUserEmails.setText(userEmail);
        txtLuckyDrawAmount.setText(""+luckyDrawAmount);
        txtAmountPayable.setText(""+totalPrice);

//        for(int i=1;i<orderStatusid;i++){
//            stepView.go(orderStatusid , true);
//        }

       stepView.go(orderStatusid -1, true);

       if(orderStatusid == 5){
           stepView.done(true);
       }

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        listAdapter = new My_Order_ProductList_Adapter(listItems);
        recyclerView.setAdapter(listAdapter);

        getProductList();

    }

    private void getProductList() {
        try {
            Req_My_Order_List req_My_OrderList = new Req_My_Order_List(this);
            req_My_OrderList.getOrderWiseProductList(listItems, recyclerView, listAdapter, userId, orderBulkId);

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
            }
            else {
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
