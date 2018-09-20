package com.mobitechs.vbags;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Payment_ProductList_Adapter;
import com.mobitechs.vbags.connectivity.Req_Generate_Order;
import com.mobitechs.vbags.connectivity.Req_Cart_List;
import com.mobitechs.vbags.model.CartList_Items;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends BaseActivity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();

    TextView txtOrderId,txtProductName;
    int totalPayableAmount=0;
    static int luckyDrawAmount;
    double finalAmount;
    String userId = "",userEmail,userMobile,userName;
    String deliveryAddressId = "";
    String fullAddress;
    String razorpayPaymentID="";

    ArrayList<String> cartIdArray = new ArrayList<String>();
    ArrayList<String> productIdArray = new ArrayList<String>();
    ArrayList<String> productNameArray = new ArrayList<String>();
    ArrayList<String> productImageArray = new ArrayList<String>();
    ArrayList<String> productPriceArray = new ArrayList<String>();
    ArrayList<String> productQtyArray = new ArrayList<String>();
    ArrayList<String> productDeliveryChargesArray = new ArrayList<String>();
    ArrayList<String> productPriceWithQtyArray = new ArrayList<String>();

    Button btnContinue;
    TextView txtName,txtAddress,txtMobileNo,txtEmail;
    static TextView txtAllProductQty,txtAllProductPrice,txtAllProductDelCharges,txtAmountPayable,txtRemainingAmount,txtTotalPayableAmount,txtLuckyDrawAmount;
    static int freeDeliveryOn = 0;
    static int payableAmount = 0;
    static int remainingAmount = 0;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<CartList_Items> listItems = new ArrayList<CartList_Items>();
    private ProgressDialog progressDialog;
    private Payment_ProductList_Adapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);

        freeDeliveryOn = Integer.parseInt(getResources().getString(R.string.freeDeliveryOn));

        txtName = (TextView) findViewById(R.id.txtName);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtMobileNo = (TextView) findViewById(R.id.txtMobileNo);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        txtAllProductQty = (TextView) findViewById(R.id.txtAllProductQty);
        txtAllProductPrice = (TextView) findViewById(R.id.txtAllProductPrice);
        txtAllProductDelCharges = (TextView) findViewById(R.id.txtAllProductDelCharges);
        txtAmountPayable = (TextView) findViewById(R.id.txtAmountPayable);
        txtTotalPayableAmount = (TextView) findViewById(R.id.txtTotalPayableAmount);
        txtRemainingAmount = (TextView) findViewById(R.id.txtRemainingAmount);
        txtLuckyDrawAmount = (TextView) findViewById(R.id.txtLuckyDrawAmount);

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

            //orderId = intent.getStringExtra("orderId");
            userId = intent.getStringExtra("idOfUser");
            userName = intent.getStringExtra("name");
            userEmail = intent.getStringExtra("email");
            userMobile = intent.getStringExtra("mobileNo");
            deliveryAddressId = intent.getStringExtra("deliveryAddressId");
            fullAddress = intent.getStringExtra("fullAddress");

            totalPayableAmount = Integer.parseInt(intent.getStringExtra("totalPayableAmount"));
            luckyDrawAmount = Integer.parseInt(intent.getStringExtra("luckyDrawAmount"));
        }
        int amount = totalPayableAmount - luckyDrawAmount;
        finalAmount = amount * 100; //it consider the amoint in paise (1 rs = 100 paise)

        txtName.setText(userName);
        txtAddress.setText(fullAddress);
        txtMobileNo.setText(userMobile);
        txtEmail.setText(userEmail);
        txtAmountPayable.setText(amount+"");
        txtTotalPayableAmount.setText(amount+"");
        txtLuckyDrawAmount.setText("- "+luckyDrawAmount+"");

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        listAdapter = new Payment_ProductList_Adapter(listItems, txtAllProductPrice, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable);
        recyclerView.setAdapter(listAdapter);

        getProductList();
        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        btnContinue = (Button) findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

    }

    private void getProductList() {

        try {
            Req_Cart_List req_CartList = new Req_Cart_List(this);
            req_CartList.getPaymentProductList(listItems, recyclerView, listAdapter,productIdArray,productNameArray,productImageArray,productPriceArray,productQtyArray,productDeliveryChargesArray,productPriceWithQtyArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePriceDetails(int prodcutCount, int allProductPrice, int allproductDelCharges) {

        txtAllProductQty.setText("( " + prodcutCount + " items)");
        txtAllProductPrice.setText(allProductPrice + "");

        try {
            if (allProductPrice > freeDeliveryOn) {
                payableAmount = allProductPrice - luckyDrawAmount;
                txtAllProductDelCharges.setText("Free Delivery");
                txtAmountPayable.setText(payableAmount + "");
                txtTotalPayableAmount.setText("RS. " + payableAmount);
            } else {
                payableAmount = allProductPrice + allproductDelCharges - luckyDrawAmount;
                txtAllProductDelCharges.setText("+ "+allproductDelCharges + "");
                txtTotalPayableAmount.setText("RS. " + payableAmount);
                txtAmountPayable.setText(payableAmount + "");
            }
            remainingAmount = freeDeliveryOn - payableAmount;
            if(remainingAmount <= 0){
                txtRemainingAmount.setText("* You get free delivery");
            }else{
                txtRemainingAmount.setText("* " + remainingAmount + "Rs. Remains to get free delivery");
            }

        } catch (Exception ex) {
            Log.d("Exception", "Exception of type" + ex.getMessage());
        }
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", finalAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", userEmail);
            preFill.put("contact", userMobile);
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorPayPaymentID) {
        try {
            razorpayPaymentID = razorPayPaymentID;
            String paymentType="Online";
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            Req_Generate_Order genarateOrder = new Req_Generate_Order(this);
            genarateOrder.GenarateOrder(paymentType,userId,razorpayPaymentID,userName,userMobile,userEmail,fullAddress,totalPayableAmount,luckyDrawAmount,deliveryAddressId,cartIdArray,productIdArray,productNameArray,productPriceArray,productQtyArray,productPriceWithQtyArray,productDeliveryChargesArray);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}
