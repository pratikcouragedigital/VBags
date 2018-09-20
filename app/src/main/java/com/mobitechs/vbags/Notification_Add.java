package com.mobitechs.vbags;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.connectivity.Req_Notification;
import com.mobitechs.vbags.imageSelector.Image;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.io.File;
import java.util.HashMap;

public class Notification_Add extends BaseActivity implements View.OnClickListener, Image.OnRecyclerSetImageListener {

    public String title;
    public String message;
    public String imagePath;
    public String userType;
    public TextView txtMessage, txtTitle;
    ImageView imageView;
    Button btnAddImage, btnSubmit;
    ProgressBar progressBar;
    Image image;
    File imgFile1;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userType = user.get(sessionManager.KEY_USERTYPE);

        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imageView = (ImageView) findViewById(R.id.image);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnAddImage.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnAddImage) {
            image = new Image(this, "Notification,", this);
            image.getImage();
        } else if (v.getId() == R.id.btnSubmit) {
            progressBar.setVisibility(View.VISIBLE);
            title = txtTitle.getText().toString();
            message = txtMessage.getText().toString();

            if (title.equals("")) {
                Toast.makeText(this, "Please Enter Title.", Toast.LENGTH_SHORT).show();
            } else if (title.equals("")) {
                Toast.makeText(this, "Please Enter Message.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Req_Notification GetNotificationList = new Req_Notification(this);
                    GetNotificationList.AddNotification(title, message, imgFile1, progressBar);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        image.getActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        image.getRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onRecyclerImageSet(Bitmap imageToShow, String imageBase64String, File cropFile) {
        imageView.setImageBitmap(imageToShow);
        imgFile1 = cropFile;

    }
}
