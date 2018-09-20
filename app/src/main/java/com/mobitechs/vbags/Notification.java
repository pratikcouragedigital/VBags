package com.mobitechs.vbags;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Notification_Adapter;
import com.mobitechs.vbags.connectivity.Req_Notification;
import com.mobitechs.vbags.model.Notification_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification extends BaseActivity implements View.OnClickListener {

    private String title;
    private String message;
    private String imagePath;
    private String userType;
    private String imFrom;

    public TextView txtMessage, txtTitle;
    ImageView image;

    ProgressBar progressBar;

    RelativeLayout singleNotificationLayout, notificationListLayout;

    public List<Notification_Items> listItems = new ArrayList<Notification_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    FloatingActionButton btnFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userType = user.get(sessionManager.KEY_USERTYPE);

        singleNotificationLayout = (RelativeLayout) findViewById(R.id.singleNotificationLayout);
        notificationListLayout = (RelativeLayout) findViewById(R.id.notificationListLayout);

        if (userType.equals("Admin")) {
            btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
            btnFab.setOnClickListener(this);
        } else {
            btnFab.setVisibility(View.GONE);
        }


        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        image = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        singleNotificationLayout.setVisibility(View.GONE);
        notificationListLayout.setVisibility(View.GONE);

        Intent intent = getIntent();

        title = intent.getStringExtra("Title");
        message = intent.getStringExtra("Message");
        imagePath = intent.getStringExtra("ImagePath");
        imFrom = intent.getStringExtra("imFrom");

        if (imFrom == null) {

            singleNotificationLayout.setVisibility(View.GONE);
            notificationListLayout.setVisibility(View.VISIBLE);

            recyclerView = (RecyclerView) findViewById(R.id.notificationRecyclerView);
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.smoothScrollToPosition(0);
            reviewAdapter = new Notification_Adapter(listItems);
            recyclerView.setAdapter(reviewAdapter);

            getList();
        }
        else if (imFrom.equals("Notification")) {
            singleNotificationLayout.setVisibility(View.VISIBLE);
            notificationListLayout.setVisibility(View.GONE);
            txtTitle.setText(title);
            txtMessage.setText(message);

            if (imagePath.equals("")) {
                image.setVisibility(View.GONE);
            } else {
                Glide.with(image.getContext())
                        .asBitmap()
                        .load(imagePath)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                image.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                image.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(image);
            }
        }

    }

    private void getList() {
        try {
            Req_Notification GetNotificationList = new Req_Notification(this);
            GetNotificationList.NotificationList(listItems, recyclerView, reviewAdapter, progressBar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFab) {

            Intent gotoAddNotification = new Intent(this, Notification_Add.class);
            startActivity(gotoAddNotification);
        }

    }
}
