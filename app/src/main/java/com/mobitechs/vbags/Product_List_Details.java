package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Rating_Review_Adapter;
import com.mobitechs.vbags.connectivity.Req_Cart_List;
import com.mobitechs.vbags.connectivity.Req_Product_Rating_Reviews;
import com.mobitechs.vbags.model.Rating_Review_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product_List_Details extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    TextView txtProductName, txtCategoryName, txtProductPrice, txtLongDescription, txtShortDescription, txtSize, txtColors, txtRatings;
    RelativeLayout detailsLayout, ratingReviewLayout;
    String productId = "", productName = "", productImage = "", categoryId = "", categoryName = "", productPrice = "", longDescription = "", shortDescription = "", productRatings = "", colors = "", size = "", image = "", image2 = "", image3 = "", image4 = "";
    Button btnAddToCart, btnBuyNow;
    TextView lblDetails, lblNoRatings, txtDeliveryCharges;
    int lblDetailsCounter = 0, lblNoRatingsCounter = 0;
    int totalAmount = 0;
    int priceWithQty;
    String userId = "", qty = "1", deliveryCharges = "", ImFrom = "";
    Spinner spinnerQty;
    String[] qtyArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    SessionManager sessionManager;

    ArrayList<String> cartIdArray = new ArrayList<String>();
    ArrayList<String> productIdArray = new ArrayList<String>();
    ArrayList<String> productNameArray = new ArrayList<String>();
    ArrayList<String> productImageArray = new ArrayList<String>();
    ArrayList<String> productPriceArray = new ArrayList<String>();
    ArrayList<String> productPriceWithQtyArray = new ArrayList<String>();
    ArrayList<String> productQtyArray = new ArrayList<String>();
    ArrayList<String> productDeliveryChargesArray = new ArrayList<String>();

    ImageView img, img1, img2, img3, img4;
    RelativeLayout img1Layout, img2Layout, img3Layout, img4Layout;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<Rating_Review_Items> listItems = new ArrayList<Rating_Review_Items>();
    private ProgressDialog progressDialog;
    private Rating_Review_Adapter listAdapter;

    static int freeDeliveryOn = 0;

    TextView txtDeliveryChargesMsg, txtAllProductQty, txtAllProductPrice, txtAllProductDelCharges, txtAmountPayable;

    RatingBar productRating;
    EditText txtFeedbackOfUser;
    Button feedbackSubmit;
    String userFeedback = "";
    String ratingValue = "";

    int cartBadge;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.product_list_details);

        freeDeliveryOn = Integer.parseInt(getResources().getString(R.string.freeDeliveryOn));

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);
        Hash_file_maps = new HashMap<String, String>();

        spinnerQty = (Spinner) findViewById(R.id.spinnerQty);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_dropdown_item, qtyArray);
        spinnerQty.setAdapter(adapter);

        //sliderLayout = (SliderLayout) findViewById(R.id.slider);
        img = (ImageView) findViewById(R.id.img);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

        img1Layout = (RelativeLayout) findViewById(R.id.img1Layout);
        img2Layout = (RelativeLayout) findViewById(R.id.img2Layout);
        img3Layout = (RelativeLayout) findViewById(R.id.img3Layout);
        img4Layout = (RelativeLayout) findViewById(R.id.img4Layout);

        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtLongDescription = (TextView) findViewById(R.id.txtLongDescription);
        txtShortDescription = (TextView) findViewById(R.id.txtShortDescription);
        txtColors = (TextView) findViewById(R.id.txtColors);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtRatings = (TextView) findViewById(R.id.txtRatings);
        txtDeliveryCharges = (TextView) findViewById(R.id.txtDeliveryCharges);

        lblDetails = (TextView) findViewById(R.id.lblDetails);
        lblNoRatings = (TextView) findViewById(R.id.lblNoRatings);

        txtDeliveryChargesMsg = (TextView) findViewById(R.id.txtDeliveryChargesMsg);
        txtAllProductQty = (TextView) findViewById(R.id.txtAllProductQty);
        txtAllProductPrice = (TextView) findViewById(R.id.txtAllProductPrice);
        txtAllProductDelCharges = (TextView) findViewById(R.id.txtAllProductDelCharges);
        txtAmountPayable = (TextView) findViewById(R.id.txtAmountPayable);

        detailsLayout = (RelativeLayout) findViewById(R.id.detailsLayout);
        ratingReviewLayout = (RelativeLayout) findViewById(R.id.ratingReviewLayout);

        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        btnBuyNow = (Button) findViewById(R.id.btnBuyNow);

        btnAddToCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        lblDetails.setOnClickListener(this);
        lblNoRatings.setOnClickListener(this);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);

        txtDeliveryChargesMsg.setText("Free Delivery above " + freeDeliveryOn + " Rs.");

        Intent intent = getIntent();
        ImFrom = intent.getStringExtra("ImFrom");
        image = intent.getStringExtra("Image");
        image2 = intent.getStringExtra("Image2");
        image3 = intent.getStringExtra("Image3");
        image4 = intent.getStringExtra("Image4");
        productImage = intent.getStringExtra("ProductImage");
        productId = intent.getStringExtra("ProductId");
        productName = intent.getStringExtra("ProductName");
        categoryId = intent.getStringExtra("CategoryId");
        categoryName = intent.getStringExtra("CategoryName");
        productPrice = intent.getStringExtra("ProductPrice");
        longDescription = intent.getStringExtra("LongDescription");
        shortDescription = intent.getStringExtra("ShortDescription");
        productRatings = intent.getStringExtra("Rating");
        colors = intent.getStringExtra("Colour");
        size = intent.getStringExtra("Size");
        deliveryCharges = intent.getStringExtra("DeliveryCharges");

        spinnerQty.setOnItemSelectedListener(this);
        productIdArray.add(productId);
        productImageArray.add(productImage);
        productNameArray.add(productName);
        productPriceArray.add(productPrice);
        productDeliveryChargesArray.add(deliveryCharges);

        productRating = (RatingBar) findViewById(R.id.productRatingBar);
        txtFeedbackOfUser = (EditText) findViewById(R.id.feedbackOfUser);
        feedbackSubmit = (Button) findViewById(R.id.feedbackSubmit);
        feedbackSubmit.setOnClickListener(this);
        addListenerOnRatingBar();


        if (ImFrom.equals("Notification") || ImFrom.equals("Slider")) {
            getProductWiseDetails();
        }
        else{
            setImages();
            setProductDetailsToLabel();
            getRatingReviews();

        }
        if (!ImFrom.equals("Notification")) {
            Transition enterTansition = TransitionInflater.from(this).inflateTransition(R.transition.slide);
            getWindow().setEnterTransition(enterTansition);
            getWindow().setAllowEnterTransitionOverlap(false);
        }




//Explode animation with xml transition
//        Explode enterTransition = new Explode();
//        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//        getWindow().setEnterTransition(enterTransition);

//Explode animation
//        Transition enterTansition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
//        getWindow().setEnterTransition(enterTansition);


// fade animation
//        Fade enterTransition = new Fade();
//        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
//        getWindow().setEnterTransition(enterTransition);

//fade animation with xml transition
//        Transition enterTansition = TransitionInflater.from(this).inflateTransition(R.transition.fade);
//        getWindow().setEnterTransition(enterTansition);

//Slide animation with xml transition
//        Slide enterTransition = new Slide();
//        enterTransition.setSlideEdge(Gravity.TOP);
//        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_very_long));
//        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
//        getWindow().setEnterTransition(enterTransition);

//Slide animation

    }

    public void addListenerOnRatingBar() {

        productRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = productRating.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int) starsf + 1;
                    productRating.setRating(stars);
                    if (stars <= 1) {
                        //red  android:progressTint=""
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            productRating.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.lowRating)));
                        }
                    } else if (stars <= 3) {
                        // orange
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            productRating.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.mediumRating)));
                        }
                    } else {
                        //green
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            productRating.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.goodRating)));
                        }
                    }
                    ratingValue = String.valueOf(stars);
                    v.setPressed(false);

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }
        });
    }

    public void getProductWiseDetails() {
        String webMethod = "ProductWiseDetails";
        final String params = "?method=" + webMethod + "&productId=" + productId;
        String url = getString(R.string.url) + params;
        final String ResponseName = "productListResponse";

        AndroidNetworking.get(url)
                .setTag("productList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray(ResponseName);

                            if (arr.length() == 0) {
//                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);

                                    image = URL_WebService.getImageUrl() + obj.getString("img1");
                                    image2 = URL_WebService.getImageUrl() + obj.getString("img2");
                                    image3 = URL_WebService.getImageUrl() + obj.getString("img3");
                                    image4 = URL_WebService.getImageUrl() + obj.getString("img4");
                                    productId = obj.getString("productId");
                                    productName = obj.getString("productName");
                                    productImage = URL_WebService.getImageUrl() + obj.getString("img1");
                                    categoryId = obj.getString("categoryId");
                                    categoryName = obj.getString("categoryName");
                                    productPrice = obj.getString("productPrice");
                                    longDescription = obj.getString("productLongDescription");
                                    shortDescription = obj.getString("productShortDescription");
                                    productRatings = obj.getString("rating");
                                    colors = obj.getString("color");
                                    size = obj.getString("size");
                                    deliveryCharges = obj.getString("deliveryCharges");
                                }
                                setImages();
                                setProductDetailsToLabel();
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

    private void setImages() {
        if (image == null || image.equals(" ")) {
            img.setVisibility(View.GONE);
        } else {
            RequestOptions options = new RequestOptions().error(R.drawable.a);
            Glide.with(img.getContext())
                    .asBitmap()
                    .load(image)
                    .apply(options)
                    .into((img));
        }

        if (image == null || image.equals(" ")) {
            img1.setVisibility(View.GONE);
//            img1Layout.setVisibility(View.GONE);
        } else {
            img1Layout.setBackgroundResource(R.drawable.border);
            RequestOptions options = new RequestOptions().error(R.drawable.a);
            Glide.with(img1.getContext())
                    .asBitmap()
                    .load(image)
                    .apply(options)
                    .into((img1));
        }

        if (image2 == null || image2.equals(" ")) {
            img2.setVisibility(View.GONE);
//            img2Layout.setVisibility(View.GONE);
        } else {
            img2Layout.setBackgroundResource(R.drawable.border);
            RequestOptions options = new RequestOptions().error(R.drawable.a);
            Glide.with(img2.getContext())
                    .asBitmap()
                    .load(image2)
                    .apply(options)
                    .into((img2));
        }

        if (image3 == null || image3.equals(" ")) {
            img3.setVisibility(View.GONE);
//            img3Layout.setVisibility(View.GONE);
        } else {
            img3Layout.setBackgroundResource(R.drawable.border);
            RequestOptions options = new RequestOptions().error(R.drawable.a);
            Glide.with(img3.getContext())
                    .asBitmap()
                    .load(image3)
                    .apply(options)
                    .into(img3);
        }

        if (image4 == null || image4.equals(" ")) {
            img4.setVisibility(View.GONE);
//            img4Layout.setVisibility(View.GONE);
        } else {
            img4Layout.setBackgroundResource(R.drawable.border);
            RequestOptions options = new RequestOptions().error(R.drawable.a);
            Glide.with(img4.getContext())
                    .asBitmap()
                    .load(image4)
                    .apply(options)
                    .into(img4);
        }
    }

    private void getRatingReviews() {
        recyclerView = (RecyclerView) findViewById(R.id.ratingReviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        listAdapter = new Rating_Review_Adapter(listItems, lblNoRatings);
//        listAdapter = new Rating_Review_Adapter(listItems);
        recyclerView.setAdapter(listAdapter);

        try {
            Req_Product_Rating_Reviews req_Product_Rating_Reviews = new Req_Product_Rating_Reviews(this);
            req_Product_Rating_Reviews.getRatingReviews(listItems, recyclerView, listAdapter, productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProductDetailsToLabel() {
        txtProductName.setText(productName);
        txtProductPrice.setText("Rs. " + productPrice);
        txtDeliveryCharges.setText("Rs." + deliveryCharges);
        txtRatings.setText(productRatings + "*");

        setProductDetails();

        if (ImFrom.equals("Slider")) {
            if (shortDescription == null && longDescription == null && colors == null && size == null) {
                lblDetails.setVisibility(View.GONE);
            } else {
                lblDetails.setVisibility(View.VISIBLE);
            }
        } else {
            if (shortDescription.equals("") && longDescription.equals("") && colors.equals("") && size.equals("")) {
                lblDetails.setVisibility(View.GONE);
            } else {
                lblDetails.setVisibility(View.VISIBLE);
            }
        }


        if (categoryName == null || categoryName.equals("") || categoryName.equals(" ")) {
            txtCategoryName.setVisibility(View.GONE);
        } else {
            txtCategoryName.setVisibility(View.VISIBLE);
            txtCategoryName.setText(categoryName);
        }

        if (shortDescription == null || shortDescription.equals("") || shortDescription.equals(" ")) {
            txtShortDescription.setVisibility(View.GONE);
        } else {
            txtShortDescription.setVisibility(View.VISIBLE);
            txtShortDescription.setText(shortDescription);
        }

        if (longDescription == null || longDescription.equals("") || longDescription.equals(" ")) {
            txtLongDescription.setVisibility(View.GONE);
        } else {
            txtLongDescription.setVisibility(View.VISIBLE);
            txtLongDescription.setText(longDescription);
        }

        if (size == null || size.equals("") || size.equals(" ")) {
            txtSize.setVisibility(View.GONE);
        } else {
            txtSize.setVisibility(View.VISIBLE);
            txtSize.setText("Size: " + size);
        }

        if (colors == null || colors.equals("") || colors.equals(" ")) {
            txtColors.setVisibility(View.GONE);
        } else {
            txtColors.setVisibility(View.VISIBLE);
            txtColors.setText("Colour: " + colors);
        }

        if (productRatings == null || productRatings.equals("") || productRatings.equals("0.00")) {
            //txtRatings.setText("Ratings Not Yet");
            txtRatings.setVisibility(View.GONE);
        } else {
            txtRatings.setVisibility(View.VISIBLE);
            double ratings = Double.parseDouble(productRatings);

            if (ratings < 2) {
                //red
                txtRatings.setBackgroundColor(this.getResources().getColor(R.color.lowRating));
            } else if (ratings > 2 && ratings < 3.5) {
                //orange
                txtRatings.setBackgroundColor(this.getResources().getColor(R.color.mediumRating));
            } else if (ratings > 3.5) {
                //Green
                txtRatings.setBackgroundColor(this.getResources().getColor(R.color.goodRating));
            }
        }
    }

    private void setProductDetails() {

        int amountWithQty = Integer.parseInt(qty) * Integer.parseInt(productPrice);
        int totalPaybleAmt = amountWithQty + Integer.parseInt(deliveryCharges);
        String delCharge = deliveryCharges;
        if (amountWithQty >= freeDeliveryOn) {
            totalPaybleAmt = amountWithQty;
            delCharge = "Free Delivery";
        } else {
            delCharge = "+ " + deliveryCharges;
        }
        txtAllProductQty.setText("x " + qty);
        txtAllProductPrice.setText(productPrice);
        txtAllProductDelCharges.setText(delCharge);
        txtAmountPayable.setText("Rs. " + totalPaybleAmt);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddToCart) {
            try {

                String counter = sessionManager.getCartBadge();
                cartBadge = Integer.parseInt(counter);
                sessionManager.setCartBadge(String.valueOf(cartBadge+1));
                invalidateOptionsMenu();

                Req_Cart_List req_CartList = new Req_Cart_List(this);
                req_CartList.addToCartList(userId, productId, qty, productPrice, deliveryCharges, totalAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.img1) {
            setMainImage(image);
        } else if (v.getId() == R.id.img2) {
            setMainImage(image2);
        } else if (v.getId() == R.id.img3) {
            setMainImage(image3);
        } else if (v.getId() == R.id.img4) {
            setMainImage(image4);
        } else if (v.getId() == R.id.btnBuyNow) {

            String payableAmount = String.valueOf(totalAmount);
            Intent gotoAddress = new Intent(this, DeliveryAddressDetails.class);
            // gotoAddress.putExtra("userId",userId);
            gotoAddress.putExtra("cartIdArray", cartIdArray);
            gotoAddress.putExtra("productIdArray", productIdArray);
            gotoAddress.putExtra("productNameArray", productNameArray);
            gotoAddress.putExtra("productImageArray", productImageArray);
            gotoAddress.putExtra("productPriceArray", productPriceArray);
            gotoAddress.putExtra("productQtyArray", productQtyArray);
            gotoAddress.putExtra("productDeliveryChargesArray", productDeliveryChargesArray);
            gotoAddress.putExtra("productPriceWithQtyArray", productPriceWithQtyArray);
            gotoAddress.putExtra("totalPayableAmount", payableAmount);
            startActivity(gotoAddress);
        } else if (v.getId() == R.id.lblDetails) {

            if (lblDetailsCounter == 0) {
                detailsLayout.setVisibility(View.VISIBLE);
                lblDetailsCounter = 1;
            } else {
                detailsLayout.setVisibility(View.GONE);
                lblDetailsCounter = 0;
            }
        } else if (v.getId() == R.id.lblNoRatings) {

            if (lblNoRatingsCounter == 0) {
                ratingReviewLayout.setVisibility(View.VISIBLE);

                lblNoRatingsCounter = 1;
            } else {
                ratingReviewLayout.setVisibility(View.GONE);
                lblNoRatingsCounter = 0;
            }
        } else if (v.getId() == R.id.feedbackSubmit) {
            userFeedback = txtFeedbackOfUser.getText().toString();
            // ratingValue = String.valueOf(ratingValue);

            if (userFeedback.equals("") || userFeedback == null) {
                Toast.makeText(this, "Please Enter your Review", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.");
                progressDialog.show();

//                Req_Product_Rating_Reviews addProductRatingReviews = new Req_Product_Rating_Reviews(this);
//                addProductRatingReviews.Add_Rating_Review(productId,userId, ratingValue, userFeedback, progressDialog);
                AddRatingReview();
            }

        }
    }

    private void AddRatingReview() {
        JSONObject jsonObject = new JSONObject();
        try {
            String webMethod = "addProductRatingReview";
            jsonObject.put("method", webMethod);
            jsonObject.put("productId", productId);
            jsonObject.put("userId", userId);
            jsonObject.put("rating", ratingValue);
            jsonObject.put("review", userFeedback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("addProductRatingReviews")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String result = response.getString("addProductRatingReviewResponse");
                            if (!result.equals("RATING_REVIEW_ADDED_SUCCESSFULLY")) {
                                Toast.makeText(Product_List_Details.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Product_List_Details.this, "Rating successfully added.", Toast.LENGTH_SHORT).show();
                                getRatingReviews();

                                txtFeedbackOfUser.setText("");
                            }

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                    }
                });
    }

    private void setMainImage(String imagePath) {

        RequestOptions options = new RequestOptions().error(R.drawable.a);
        Glide.with(img.getContext())
                .asBitmap()
                .load(imagePath)
                .apply(options)
                .into(img);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        productQtyArray.clear();
        productPriceWithQtyArray.clear();

        int sid = spinnerQty.getSelectedItemPosition();
        qty = qtyArray[sid];

        priceWithQty = (Integer.parseInt(qty) * Integer.parseInt(productPrice));

        if (priceWithQty > freeDeliveryOn) {
            totalAmount = priceWithQty;
        } else {
            totalAmount = priceWithQty + Integer.parseInt(deliveryCharges);
        }

        productQtyArray.add(qty);
        productPriceWithQtyArray.add(String.valueOf(priceWithQty));

        setProductDetails();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
