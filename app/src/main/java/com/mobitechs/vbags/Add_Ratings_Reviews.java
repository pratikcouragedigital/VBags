package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mobitechs.vbags.connectivity.Req_Product_Rating_Reviews;
import com.mobitechs.vbags.internetConnectivity.NetworkChangeReceiver;

public class Add_Ratings_Reviews extends AppCompatActivity implements View.OnClickListener {

    RatingBar productRating;
    EditText txtFeedbackOfUser;
    Button feedbackSubmit;

    String userFeedback = "";
    String userId = "";
    String productId = "";
    String userEmail = "";
    String ratingValue = "";
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ratings_reviews);

        productRating = (RatingBar) findViewById(R.id.productRatingBar);
        txtFeedbackOfUser = (EditText) findViewById(R.id.feedbackOfUser);
        feedbackSubmit = (Button) findViewById(R.id.feedbackSubmit);
        feedbackSubmit.setOnClickListener(this);
        addListenerOnRatingBar();

        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");


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
                    if(stars <= 1){
                        //red  android:progressTint=""
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            productRating.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.lowRating)));
                        }
                    }
                    else if(stars <= 3){
                        // orange
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            productRating.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.mediumRating)));
                        }
                    }
                    else {
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

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.feedbackSubmit) {
            userFeedback = txtFeedbackOfUser.getText().toString();
           // ratingValue = String.valueOf(ratingValue);

            if (userFeedback.equals("") || userFeedback == null) {
                Toast.makeText(this, "Please Enter your Review", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait.");
                progressDialog.show();

                Req_Product_Rating_Reviews addProductRatingReviews = new Req_Product_Rating_Reviews(this);
                addProductRatingReviews.Add_Rating_Review(productId,userId, ratingValue, userFeedback, progressDialog);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        ComponentName component = new ComponentName(this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}