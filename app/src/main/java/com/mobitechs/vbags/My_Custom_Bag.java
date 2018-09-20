package com.mobitechs.vbags;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.My_Custom_Bags_Adapter;
import com.mobitechs.vbags.adapter.My_Custom_Bags_Color_Adapter;
import com.mobitechs.vbags.model.Rating_Review_Items;
import com.mobitechs.vbags.singleton.Address_Instance;
import com.mobitechs.vbags.singleton.My_Custom_Bag_Instance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class My_Custom_Bag extends BaseActivity {

    static Context myCustomBag;
    ArrayList<String> mNames = new ArrayList<>();
    ArrayList<String> mImageUrls = new ArrayList<>();

    ArrayList<String> colorNames = new ArrayList<>();
    ArrayList<String> colorImage = new ArrayList<>();

    RecyclerView recyclerView,recyclerViewColor;
    LinearLayoutManager layoutManager,layoutManagerColor;
    My_Custom_Bags_Adapter adapter;
    My_Custom_Bags_Color_Adapter adapterColor;

    static ImageView customImage;
    static String imageName = "",imageColor = "";
    String imagePath,webMethod;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_custom_bag);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewColor = findViewById(R.id.recyclerViewColor);
        customImage = findViewById(R.id.customImage);

        Activity activity = this;
        My_Custom_Bag my_custom_bag = new My_Custom_Bag();
        my_custom_bag.setContext(activity.getApplicationContext());

        imagePath = URL_WebService.getImageUrl();


        getPatternImages();
        getColorImages();
    }

    private void getPatternImages(){
        webMethod = "PatternImagesList";
        String params = "?method="+webMethod;
        String url = getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("productList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("ImagesListResponse");

                            if (arr.length() == 0) {
                                // Toast.makeText(context, "Ratings &amp; Reviews Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    int srNo = i+1;
                                    String path = imagePath+obj.getString("imagePath");

                                    mImageUrls.add(path);
                                    mNames.add(""+srNo);
                                    adapter.notifyDataSetChanged();
                                }
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
        initRecyclerView();

    }

    private void initRecyclerView(){

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new My_Custom_Bags_Adapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

    private void getColorImages(){

        colorImage.add("#FF0000");
        colorNames.add("Red");

        colorImage.add("#0000FF");
        colorNames.add("Blue");

        colorImage.add("#000080");
        colorNames.add("Navy Blue");

        colorImage.add("#008000");
        colorNames.add("Green");

        colorImage.add("#FFFF00");
        colorNames.add("Yellow");

        colorImage.add("#FFC0CB");
        colorNames.add("Pink");

        colorImage.add("#800080");
        colorNames.add("Purple");

        colorImage.add("Aqua");
        colorNames.add("#00FFFF");

        colorImage.add("#FF5733");
        colorNames.add("Orange");

        colorImage.add("#800000");
        colorNames.add("Maroon");

        initColorRecyclerView();

    }

    private void initColorRecyclerView(){

        layoutManagerColor = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewColor.setLayoutManager(layoutManagerColor);
        adapterColor = new My_Custom_Bags_Color_Adapter(this, colorNames, colorImage);
        recyclerViewColor.setAdapter(adapterColor);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void UpdateCustomBag() {

        My_Custom_Bag_Instance customBagsInstance = new My_Custom_Bag_Instance();
        imageColor = customBagsInstance.getBagColor();
        imageName = customBagsInstance.getBagImage();

        if(!imageName.equals("")){
            Glide.with(myCustomBag)
                    .asBitmap()
                    .load(imageName)
                    .into(customImage);

        }
        if(!imageColor.equals("")) {

            int color = Color.parseColor(imageColor);
            customImage.setColorFilter(color);

            float x = customImage.getX();
            float y = customImage.getY();

        }
    }

    public void setContext(Context context) {
        myCustomBag = context;
    }

}
