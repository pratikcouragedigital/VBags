package com.mobitechs.vbags;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
import com.bluehomestudio.luckywheel.WheelView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.connectivity.Req_Generate_Order;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LuckyDraw extends BaseActivity implements View.OnClickListener, OnLuckyWheelReachTheTarget {
    private static final String TAG = LuckyDraw.class.getSimpleName();

    SessionManager sessionManager;

    String userId = "", userEmail, userMobile, userName,  isLuckyDrawDone, luckyDrawAmount = "0";
    String deliveryAddressId = "",fullAddress;
    int payableAmount;

    ArrayList<String> cartIdArray = new ArrayList<String>();
    ArrayList<String> productIdArray = new ArrayList<String>();
    ArrayList<String> productNameArray = new ArrayList<String>();
    ArrayList<String> productImageArray = new ArrayList<String>();
    ArrayList<String> productPriceArray = new ArrayList<String>();
    ArrayList<String> productQtyArray = new ArrayList<String>();
    ArrayList<String> productDeliveryChargesArray = new ArrayList<String>();
    ArrayList<String> productPriceWithQtyArray = new ArrayList<String>();

    private WheelView wheelView;
    private ImageView arrow;
    Button btnSpin,btnSubmit;
    List<WheelItem> wheelItems = new ArrayList<>();

    String luckyPriceArrayMax[] = new String[]{"0", "10", "20", "30", "50", "60", "70", "100", "150", "300"};
    String luckyPriceArrayMin[] = new String[]{"0", "10", "20", "30", "40", "50", "60", "70", "80", "90"};
    int minAmt = 500, maxAmt = 2000,freeDeliveryOn=0,allTotal;
    RelativeLayout paymentDetailsLayout,luckySpinLayout;
    TextView txtAllTotal,txtMsg,txtAllProductPrice,txtLuckyDrawAmount,txtAllProductDelCharges,txtAmountPayable;
    String msg = "";
    LinearLayout rootLayout,dynamicProductNameLayout,nameLayout,qtyLayout,priceLayout,totalPriceLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_draw);

        freeDeliveryOn = Integer.parseInt(getResources().getString(R.string.freeDeliveryOn));

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);
        userEmail = user.get(sessionManager.KEY_USEREMAIL);
        userMobile = user.get(sessionManager.KEY_CONTACTNO);
        userName = user.get(sessionManager.KEY_FIRST_NAME) + " " + user.get(sessionManager.KEY_LAST_NAME);

        if (userId == null) {
            userId = "";
        }

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

            deliveryAddressId = intent.getStringExtra("deliveryAddressId");
            fullAddress = intent.getStringExtra("fullAddress");
            payableAmount = Integer.parseInt(intent.getStringExtra("totalPayableAmount"));
        }

        allTotal = payableAmount;
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        luckySpinLayout = (RelativeLayout) findViewById(R.id.luckySpinLayout);
        paymentDetailsLayout = (RelativeLayout) findViewById(R.id.paymentDetailsLayout);

        txtAllTotal = (TextView) findViewById(R.id.txtAllTotal);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtAllProductPrice = (TextView) findViewById(R.id.txtAllProductPrice);
        txtLuckyDrawAmount = (TextView) findViewById(R.id.txtLuckyDrawAmount);
        txtAllProductDelCharges = (TextView) findViewById(R.id.txtAllProductDelCharges);
        txtAmountPayable = (TextView) findViewById(R.id.txtAmountPayable);
        btnSubmit.setOnClickListener(this);

        dynamicProductNameLayout = (LinearLayout) findViewById(R.id.dynamicProductNameLayout);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
        qtyLayout = (LinearLayout) findViewById(R.id.qtyLayout);
        priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
        totalPriceLayout = (LinearLayout) findViewById(R.id.totalPriceLayout);

        HashMap<String, String> luckyAmount = sessionManager.getLuckyDrawAmount();
        luckyDrawAmount = luckyAmount.get(sessionManager.KEY_LUCKYDRAW_AMOUNT);
        isLuckyDrawDone = luckyAmount.get(sessionManager.KEY_IS_LUCKYDRAW_DONE);

        wheelView = (WheelView) findViewById(R.id.wv_main_wheel);
        arrow = (ImageView) findViewById(R.id.iv_arrow);
        btnSpin = (Button) findViewById(R.id.btnSpin);
        btnSpin.setOnClickListener(this);

        if (isLuckyDrawDone == null || isLuckyDrawDone.equals("No")) {
            luckySpinLayout.setVisibility(View.VISIBLE);
            paymentDetailsLayout.setVisibility(View.GONE);
            CreateLuckyDrawWheel();
        }
        else {
            luckySpinLayout.setVisibility(View.GONE);
            paymentDetailsLayout.setVisibility(View.VISIBLE);

            btnSubmit.setVisibility(View.VISIBLE);
            btnSpin.setVisibility(View.GONE);

            payableAmount = payableAmount - Integer.parseInt(luckyDrawAmount);

            if (luckyDrawAmount == null || luckyDrawAmount.equals("0")) {
                msg = "You already tried your. Sorry but you got nothing,Please try next time.";
                Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
            }
            else {
                msg = "You already tried your.And you won " + luckyDrawAmount + " Rs. Now You need to pay" + payableAmount + " Rs.";
                Toast.makeText(this, "" + msg, Toast.LENGTH_LONG).show();
            }
            setAmountDetails();
        }
    }
    private void setAmountDetails() {

        int size = productPriceArray.size();
        String pName="";
        int price=0;
        int qty=0;
        int totalPrice=0;
        int delCharge=0;

        String deliveryCharge="";

        for(int i= 0; i< size; i++){
            pName = productNameArray.get(i);
            price = Integer.parseInt(productPriceArray.get(i));
            qty = Integer.parseInt(productQtyArray.get(i));
            totalPrice = price * qty;
            delCharge = delCharge + Integer.parseInt(productDeliveryChargesArray.get(i));

            final TextView txtName = new TextView(this);
            txtName.setText(pName+"");
            txtName.setId(i);
            if(txtName.getParent()!=null)
                ((ViewGroup)txtName.getParent()).removeView(txtName);
            nameLayout.addView(txtName);

            final TextView txtqty = new TextView(this);

            final TextView txtPrice = new TextView(this);
            txtPrice.setText(price+"");
            txtPrice.setId(i);
            txtPrice.setGravity(Gravity.END);
            if(txtPrice.getParent()!=null)
                ((ViewGroup)txtPrice.getParent()).removeView(txtPrice);
            priceLayout.addView(txtPrice);

            txtqty.setText(qty+"");
            txtqty.setId(i);
            txtqty.setGravity(Gravity.END);
            if(txtqty.getParent()!=null)
                ((ViewGroup)txtqty.getParent()).removeView(txtqty);
            qtyLayout.addView(txtqty);

            final TextView txtTotalPrice = new TextView(this);
            txtTotalPrice.setText(totalPrice+"");
            txtTotalPrice.setId(i);
            txtTotalPrice.setGravity(Gravity.END);
            if(txtTotalPrice.getParent()!=null)
                ((ViewGroup)txtTotalPrice.getParent()).removeView(txtTotalPrice);
            totalPriceLayout.addView(txtTotalPrice);

            if(nameLayout.getParent()!=null)
                ((ViewGroup)nameLayout.getParent()).removeView(nameLayout);
            dynamicProductNameLayout.addView(nameLayout);

            if(priceLayout.getParent()!=null)
                ((ViewGroup)priceLayout.getParent()).removeView(priceLayout);
            dynamicProductNameLayout.addView(priceLayout);

            if(qtyLayout.getParent()!=null)
                ((ViewGroup)qtyLayout.getParent()).removeView(qtyLayout);
            dynamicProductNameLayout.addView(qtyLayout);

            if(totalPriceLayout.getParent()!=null)
                ((ViewGroup)totalPriceLayout.getParent()).removeView(totalPriceLayout);
            dynamicProductNameLayout.addView(totalPriceLayout);

            if(dynamicProductNameLayout.getParent()!=null)
                ((ViewGroup)dynamicProductNameLayout.getParent()).removeView(dynamicProductNameLayout);
            rootLayout.addView(dynamicProductNameLayout);
        }

        if(payableAmount >= freeDeliveryOn){
            deliveryCharge ="Free Delivery";
            txtAllProductDelCharges.setTextColor(getResources().getColor(R.color.goodRating));
        }else{
            deliveryCharge ="+"+delCharge;
            allTotal = allTotal - delCharge;
        }

        if(!luckyDrawAmount.equals("0")){
            txtLuckyDrawAmount.setTextColor(getResources().getColor(R.color.lowRating));
        }

        txtMsg.setText("*"+msg);
        txtAllTotal.setText(allTotal+"");
        txtLuckyDrawAmount.setText("-"+luckyDrawAmount);
        txtAllProductDelCharges.setText(deliveryCharge);
        txtAmountPayable.setText(payableAmount+"");
    }

    private void CreateLuckyDrawWheel() {

        if (payableAmount > maxAmt) {
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_1), BitmapFactory.decodeResource(getResources(), R.drawable.a_0)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_2), BitmapFactory.decodeResource(getResources(), R.drawable.a_10)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_3), BitmapFactory.decodeResource(getResources(), R.drawable.a_20)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_4), BitmapFactory.decodeResource(getResources(), R.drawable.a_30)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.dot_dark_screen1), BitmapFactory.decodeResource(getResources(), R.drawable.a_50)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.dot_dark_screen2), BitmapFactory.decodeResource(getResources(), R.drawable.a_60)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.userNameBlue), BitmapFactory.decodeResource(getResources(), R.drawable.a_70)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.Lime500), BitmapFactory.decodeResource(getResources(), R.drawable.a_100)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.thirdFabBtn), BitmapFactory.decodeResource(getResources(), R.drawable.a_150)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.colorAccent), BitmapFactory.decodeResource(getResources(), R.drawable.a_300)));

        }
        else {
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_1), BitmapFactory.decodeResource(getResources(), R.drawable.a_0)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_2), BitmapFactory.decodeResource(getResources(), R.drawable.a_10)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_3), BitmapFactory.decodeResource(getResources(), R.drawable.a_20)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.refresh_progress_4), BitmapFactory.decodeResource(getResources(), R.drawable.a_30)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.colorAccent), BitmapFactory.decodeResource(getResources(), R.drawable.a_40)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.dot_dark_screen1), BitmapFactory.decodeResource(getResources(), R.drawable.a_50)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.dot_dark_screen2), BitmapFactory.decodeResource(getResources(), R.drawable.a_60)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.userNameBlue), BitmapFactory.decodeResource(getResources(), R.drawable.a_70)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.Lime500), BitmapFactory.decodeResource(getResources(), R.drawable.a_80)));
            wheelItems.add(new WheelItem(getResources().getColor(R.color.thirdFabBtn), BitmapFactory.decodeResource(getResources(), R.drawable.a_90)));
        }
        addWheelItems(wheelItems);
    }

    public void addWheelItems(List<WheelItem> wheelItems) {
        wheelView.addWheelItems(wheelItems);
    }

    public void rotateWheelTo(int number) {
        wheelView.resetRotationLocationToZeroAngle(number);
    }

    @Override
    public void onReachTarget() {
        Drawable icon;
        String title;
        isLuckyDrawDone = "Yes";

        sessionManager.SetLuckyDrawAmount(luckyDrawAmount, isLuckyDrawDone);

        if (luckyDrawAmount.equals("0")) {
            if (payableAmount < minAmt) {
                msg = "Sorry, Your order is below 500 Rs,If you purchase more than 500 Rs then there will be chance to get something.Try again later.";
            } else {
                msg = "Sorry, it doesn't seems to a lucky day.Better luck next time.";
            }
            icon = getResources().getDrawable(R.drawable.sad);
            title = "Sorry";
        } else {
            icon = getResources().getDrawable(R.drawable.congo);
            msg = "Congratulation you won Rs." + luckyDrawAmount + ". Now you need to pay only " + payableAmount + " Rs.";
            title = "Congratulations";
        }
        //Toast.makeText(this, "Now you need to pay only : " + payableAmount + " Rs.", Toast.LENGTH_LONG).show();

        new MaterialStyledDialog.Builder(this)
                .setTitle(title)
                .setHeaderColor(R.color.colorAccent)
                .setDescription(msg)
                .setPositiveText("Ok")
                .setIcon(icon)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        paymentDetailsLayout.setVisibility(View.VISIBLE);
                        luckySpinLayout.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                })
                .show();

        setAmountDetails();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSpin) {
            btnSpin.setVisibility(View.GONE);

            int price = payableAmount;
            String randomStr = "0";
            int luckyAmount = 0;

            if (price < maxAmt && price >= minAmt) {
                randomStr = luckyPriceArrayMin[new Random().nextInt(luckyPriceArrayMin.length)];
                luckyAmount = new ArrayList<String>(Arrays.asList(luckyPriceArrayMin)).indexOf(randomStr);
            } //2100 >= 2000

            else if (price >= maxAmt) {
                randomStr = luckyPriceArrayMax[new Random().nextInt(luckyPriceArrayMax.length)];
                luckyAmount = new ArrayList<String>(Arrays.asList(luckyPriceArrayMax)).indexOf(randomStr);
            }

            luckyAmount = luckyAmount + 1;
            rotateWheelTo(luckyAmount);
            wheelView.setWheelListener(this);
            luckyDrawAmount = randomStr;
            payableAmount = payableAmount - Integer.parseInt(randomStr);

        }
        else if (v.getId() == R.id.btnSubmit) {
            GotoPaymentMode();
        }
    }

    private void GotoPaymentMode() {
        //check cod or Online

        String positiveButtonText = "ONLINE";
        String negativeButtonText = "OFFLINE";
        Drawable icon = getResources().getDrawable(R.drawable.ic_launcher);
        String msg = "There is two option online and offline, if you want to do payment with COD(Cash On Delivery) then click on OFFLINE or If you want do with Net Backing, Credit/Debit card then click on ONLINE.";

        new MaterialStyledDialog.Builder(this)
                .setTitle("Payment Mode!")
                .setHeaderColor(R.color.colorAccent)
                .setDescription(msg)
                .setPositiveText(positiveButtonText)
                .setIcon(icon)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        goOnline();
                    }
                })
                .setNegativeText(negativeButtonText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        goOffline();
                    }
                })
                .show();
    }

    private void goOffline() {
        try {
            int totalPayableAmount = payableAmount;
            String razorpayPaymentID = "";
            String paymentType = "Offline";
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            Req_Generate_Order genarateOrder = new Req_Generate_Order(this);
            genarateOrder.GenarateOrder(paymentType, userId, razorpayPaymentID, userName, userMobile, userEmail, fullAddress, totalPayableAmount, Integer.parseInt(luckyDrawAmount), deliveryAddressId, cartIdArray, productIdArray, productNameArray, productPriceArray, productQtyArray, productPriceWithQtyArray, productDeliveryChargesArray);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    private void goOnline() {

        String a = String.valueOf(payableAmount);
        Intent gotoPaymentActivity = new Intent(this, PaymentActivity.class);
        gotoPaymentActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        gotoPaymentActivity.putExtra("idOfUser", userId);
        gotoPaymentActivity.putExtra("name", userName);
        gotoPaymentActivity.putExtra("email", userEmail);
        gotoPaymentActivity.putExtra("mobileNo", userMobile);
        gotoPaymentActivity.putExtra("fullAddress", fullAddress);
        gotoPaymentActivity.putExtra("deliveryAddressId", deliveryAddressId);
        gotoPaymentActivity.putExtra("totalPayableAmount", String.valueOf(payableAmount));
        gotoPaymentActivity.putExtra("luckyDrawAmount", luckyDrawAmount);

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
