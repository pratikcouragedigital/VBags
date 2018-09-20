package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Spinner_Filter_Adapter;
import com.mobitechs.vbags.connectivity.Req_Add_Admin_Right_Details;
import com.mobitechs.vbags.connectivity.Req_Category_Details;
import com.mobitechs.vbags.imageSelector.Image;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Add_Product extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,Image.OnRecyclerSetImageListener {

    String[]sizeArray = {"Select Size","Small", "Medium","Large",};
    SessionManager sessionManager;
    TextView txtProductName,txtProductPrice, txtLongDescription,txtShortDescription,txtColor;
    Spinner spinnerSize,spinnerCategory;
    Button btnSubmit;
    ImageView img1,img2,img3,img4;

    private String[] categoryArrayList;
    private Spinner_Filter_Adapter spinnerAdapter;
    private List<String> categoryIdList = new ArrayList<String>();
    private List<String> categoryNameList = new ArrayList<String>();
    private ProgressDialog progressDialog = null;
    int imgCounter = 0;
    Image image;
    File imgFile1,imgFile2,imgFile3,imgFile4;

    String userId,productName,categoryId,categoryName,productPrice,shortDescription,longDescription,color,size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        sessionManager  = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtShortDescription = (TextView) findViewById(R.id.txtShortDescription);
        txtLongDescription = (TextView) findViewById(R.id.txtLongDescription);
        txtColor = (TextView) findViewById(R.id.txtColor);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);

        spinnerSize = (Spinner) findViewById(R.id.spinnerSize);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,sizeArray);
        spinnerSize.setAdapter(adapter);

        spinnerSize.setOnItemSelectedListener(this);

        getCategoryDetails();

    }

    private void getCategoryDetails() {

        categoryArrayList = new String[]{
                "Select Category"
        };
        categoryIdList.add("0");

        categoryNameList = new ArrayList<>(Arrays.asList(categoryArrayList));
        spinnerAdapter = new Spinner_Filter_Adapter(this, R.layout.spinner_item, categoryNameList);
        getListOfStandard();
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoryName = parent.getItemAtPosition(position).toString();
                    categoryId = categoryIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getListOfStandard() {
        Req_Category_Details getCategoryDetails = new Req_Category_Details(this);
        getCategoryDetails.FetchCategory(categoryNameList, categoryIdList, spinnerAdapter);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnSubmit){
            productName = txtProductName.getText().toString();
            productPrice = txtProductPrice.getText().toString();
            shortDescription = txtShortDescription.getText().toString();
            longDescription = txtLongDescription.getText().toString();
            color = txtColor.getText().toString();

            if (productName.equals("") || productName == null) {
                Toast.makeText(this, "Please Enter Product Name.", Toast.LENGTH_SHORT).show();
            }
            else if (productPrice.equals("")) {
                Toast.makeText(this, "Please Enter Product Price.", Toast.LENGTH_SHORT).show();
            }
            else if (imgFile1 == null && imgFile2 == null && imgFile3 == null && imgFile4 == null) {
                Toast.makeText(this, "Please Add At least One Image.", Toast.LENGTH_SHORT).show();
            }
            else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait. Product Is Adding.");
                progressDialog.show();
//                progressDialog.setCancelable(false);
//                progressDialog.setCanceledOnTouchOutside(false);
                try {
                    Req_Add_Admin_Right_Details addProduct = new Req_Add_Admin_Right_Details(this);
                    addProduct.AddProduct(userId,productName,categoryId,categoryName,productPrice,shortDescription,longDescription,color,size,imgFile1,imgFile2,imgFile3,imgFile4,progressDialog);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(v.getId() == R.id.img1){
            imgCounter = 1;
            image = new Image(this,"Product,",this);
            image.getImage();
        }
        else if(v.getId() == R.id.img2){
            imgCounter = 2;
            image = new Image(this,"Product,",this);
            image.getImage();
        }
        else if(v.getId() == R.id.img3){
            imgCounter = 3;
            image = new Image(this,"Product,",this);
            image.getImage();
        }
        else if(v.getId() == R.id.img4){
            imgCounter = 4;
            image = new Image(this,"Product,",this);
            image.getImage();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid = spinnerSize.getSelectedItemPosition();
        size = sizeArray[sid];
        if(size.equals("Select Size")){
            size = "";
        }else{
            size = sizeArray[sid];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        if(imgCounter == 1){
            img1.setImageBitmap(imageToShow);
            imgFile1 = cropFile;
        }
        else if(imgCounter == 2){
            img2.setImageBitmap(imageToShow);
            imgFile2 = cropFile;
        }
        else if(imgCounter == 3){
            img3.setImageBitmap(imageToShow);
            imgFile3 = cropFile;
        }
        else if(imgCounter == 4){
            img4.setImageBitmap(imageToShow);
            imgFile4 = cropFile;
        }

    }
}
