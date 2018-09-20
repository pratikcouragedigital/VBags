package com.mobitechs.vbags;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Add_Images_Adapter;
import com.mobitechs.vbags.connectivity.Req_Add_Admin_Right_Details;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;

public class Add_Pattern_Material_Handle extends BaseActivity implements View.OnClickListener {

    String imFor="";
    Button btnAddImages;
    FloatingActionButton fabSubmit;

    private Add_Images_Adapter eventAdapter;
    private RecyclerView imageRecyclerView;
    private GridLayoutManager gridLayoutManager;
    List<File> imgFileList = new ArrayList<File>();
    int i = 0;
    private String timeStamp;
    private File storageDir;
    private ProgressDialog progressDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pattern_material_handle);

        Intent intent = getIntent();
        if(intent != null){
            imFor = intent.getStringExtra("imFor");
        }

        fabSubmit = (FloatingActionButton) findViewById(R.id.fabSubmit);
        btnAddImages = (Button) findViewById(R.id.btnAddImages);
        imageRecyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);

        fabSubmit.setOnClickListener(this);
        btnAddImages.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fabSubmit){

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait. Images Are Adding..");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            Req_Add_Admin_Right_Details addImages = new Req_Add_Admin_Right_Details(this);
            addImages.AddImages(imFor,imgFileList,progressDialog);
        }
        else if(v.getId() == R.id.btnAddImages){
            FishBun.with(this)
                    .setImageAdapter(new GlideAdapter())
                    .setAllViewTitle("All")
                    .setMaxCount(10)
                    .setActionBarColor(Color.parseColor("#000000"), Color.parseColor("#000000"), false)
                    .setActionBarTitle("Image Library")
                    .setActionBarTitleColor(Color.parseColor("#48C3D9"))
                    .textOnImagesSelectionLimitReached("You are not allowed to select more images!")
                    .textOnNothingSelected("You did not select any images")
                    .startAlbum();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == Define.ALBUM_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
                ArrayList<Parcelable> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                for (i = 0; i < path.size(); i++) {
                    Uri uri = (Uri) path.get(i);
                    String filePath = getRealPathFromURI(uri);
                    uri = Uri.fromFile(new File(filePath));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM);
                    File newFile = new File(storageDir + "/" + timeStamp + i + ".png");
                    try {
                        OutputStream os = new FileOutputStream(newFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
                    imageCompress(newFile);
                }

                imageRecyclerView.setHasFixedSize(true);
                gridLayoutManager = new GridLayoutManager(this, 3);
                imageRecyclerView.setLayoutManager(gridLayoutManager);

                imageRecyclerView.smoothScrollToPosition(0);
                eventAdapter = new Add_Images_Adapter(imgFileList, fabSubmit);
                imageRecyclerView.setAdapter(eventAdapter);

                fabSubmit.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public void imageCompress(File sourceFile) {
        Bitmap tempBitmap = null;
        try {
            tempBitmap = new Compressor(this)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .compressToBitmap(sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream os = new FileOutputStream(sourceFile);
            tempBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        imgFileList.add(sourceFile);
    }

}
