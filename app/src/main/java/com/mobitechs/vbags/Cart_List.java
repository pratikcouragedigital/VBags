package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Cart_List_Adapter;
import com.mobitechs.vbags.connectivity.Req_Cart_List;
import com.mobitechs.vbags.model.CartList_Items;
import com.mobitechs.vbags.scrollListeners.ProductListScrollListener;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class Cart_List extends BaseActivity implements View.OnClickListener {

    private View v;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<CartList_Items> listItems = new ArrayList<CartList_Items>();
    private ProgressDialog progressDialog;
    private Cart_List_Adapter listAdapter;
    String userId = "";
    private static int currentPage = 1;
    int requestState;
    SessionManager sessionManager;
    RelativeLayout notLoggedInLayout;
    static RelativeLayout priceDetailsMainLayout;
    LinearLayout containerLayout;
    static LinearLayout continueBtnLayout;
    public static TextView txtAllProductPrice, txtTotalPayableAmount, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable, txtRemainingAmount;
    Button btnContinue;
    static int payableAmount = 0;
    static int freeDeliveryOn = 0;
    static int remainingAmount = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);

        freeDeliveryOn = Integer.parseInt(getResources().getString(R.string.freeDeliveryOn));

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        notLoggedInLayout = (RelativeLayout) findViewById(R.id.notLoggedInLayout);
        priceDetailsMainLayout = (RelativeLayout) findViewById(R.id.priceDetailsMainLayout);
        containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
        continueBtnLayout = (LinearLayout) findViewById(R.id.continueBtnLayout);
        txtRemainingAmount = (TextView) findViewById(R.id.txtRemainingAmount);

        continueBtnLayout.setVisibility(GONE);

        if (userId == null) {
            userId = "";
        }
        if (userId.equals("") || userId == null) {
            notLoggedInLayout.setVisibility(View.VISIBLE);
            containerLayout.setVisibility(GONE);
        } else {

            containerLayout.setVisibility(View.VISIBLE);
            notLoggedInLayout.setVisibility(GONE);
            priceDetailsMainLayout.setVisibility(GONE);

            txtAllProductQty = (TextView) findViewById(R.id.txtAllProductQty);
            txtAllProductPrice = (TextView) findViewById(R.id.txtAllProductPrice);
            txtAllProductDelCharges = (TextView) findViewById(R.id.txtAllProductDelCharges);
            txtAmountPayable = (TextView) findViewById(R.id.txtAmountPayable);
            txtTotalPayableAmount = (TextView) findViewById(R.id.txtTotalPayableAmount);
            btnContinue = (Button) findViewById(R.id.btnContinue);
            btnContinue.setOnClickListener(this);

            recyclerView = (RecyclerView) findViewById(R.id.cartListRecyclerView);
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.smoothScrollToPosition(0);
            listAdapter = new Cart_List_Adapter(this,listItems, txtAllProductPrice, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable, btnContinue,freeDeliveryOn);
            recyclerView.setAdapter(listAdapter);

            requestState = 0;
            getProductList(currentPage);

            recyclerView.addOnScrollListener(new ProductListScrollListener(linearLayoutManager, currentPage) {
                @Override
                public void onLoadMore(int currentPage) {
                    getProductList(currentPage);
                }
            });
            recyclerView.smoothScrollToPosition(0);
        }
    }

    private void getProductList(int currentPage) {

        try {
            Req_Cart_List req_CartList = new Req_Cart_List(this);
            req_CartList.getCartList(listItems, recyclerView, listAdapter, userId, currentPage, requestState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public static void updatePriceDetails(int prodcutCount,int allProductPrice,int allproductDelCharges,int payableAmount){
    public static void updatePriceDetails(int prodcutCount, int allProductPrice, int allproductDelCharges) {
        continueBtnLayout.setVisibility(View.VISIBLE);
        priceDetailsMainLayout.setVisibility(View.VISIBLE);

        txtAllProductQty.setText("( " + prodcutCount + " items)");
        txtAllProductPrice.setText(allProductPrice + "");
        int remainingAmount = 0;
        try {
            if (allProductPrice > freeDeliveryOn) {

                payableAmount = allProductPrice;
                txtAllProductDelCharges.setText("Free Delivery");
                txtAmountPayable.setText(payableAmount + "");
                txtTotalPayableAmount.setText("RS. " + payableAmount);

                remainingAmount = payableAmount - freeDeliveryOn;
            }
            else {

                payableAmount = allProductPrice + allproductDelCharges;
                txtAllProductDelCharges.setText(allproductDelCharges + "");
                txtTotalPayableAmount.setText("RS. " + payableAmount);
                txtAmountPayable.setText(payableAmount + "");

                remainingAmount = freeDeliveryOn - payableAmount;
            }

            if (payableAmount >= freeDeliveryOn) {
                txtRemainingAmount.setText("* You get free delivery");
            } else {
                txtRemainingAmount.setText("* " + remainingAmount + "Rs. Remains to get free delivery");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception", "Exception of type" + ex.getMessage());
        }
    }

    @Override
    public void onClick(View v) {

    }
}