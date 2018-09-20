package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Add_Category;
import com.mobitechs.vbags.Add_Pattern_Material_Handle;
import com.mobitechs.vbags.Add_Product;
import com.mobitechs.vbags.Admin_Rights_Add;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.sqlite_db.DataBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class Req_Add_Admin_Right_Details {
    Context context;
    String categoryId, categoryName, userId;

    private DataBaseHelper mydb;
    private String webMethName;
    private File storageDir;

    public Req_Add_Admin_Right_Details(Add_Category add_category) {
        context = add_category;
    }

    public Req_Add_Admin_Right_Details(Add_Product add_product) {
        context = add_product;
    }

    public Req_Add_Admin_Right_Details(Add_Pattern_Material_Handle add_pattern_material_handle) {
        context = add_pattern_material_handle;
    }

    public void AddCategory(String categoryName, final ProgressDialog progressDialog) {

        webMethName= "AddCategory";

        String url = context.getString(R.string.url)+ "?method=" + webMethName+"&categoryName="+categoryName;
        AndroidNetworking.get(url)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("showCategoriesResponse");
                            if(res.equals("CATEGORY_SAVED")){
                                Toast.makeText(context, "Category Added Successfully.", Toast.LENGTH_SHORT).show();
                                Intent gotoAddCategory = new Intent(context, Add_Category.class);
                                gotoAddCategory.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoAddCategory);
                            }
                            else{
                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void AddProduct(String userId, String productName, String categoryId, String categoryName, String productPrice, String shortDescription, String longDescription, String color, String size, File imgFile1, File imgFile2, File imgFile3, File imgFile4, final ProgressDialog progressDialog) {

        webMethName = "addProductDetails";
        String url = context.getString(R.string.url);
        AndroidNetworking.upload(url)
                .addMultipartFile("imgFile4", imgFile4)
                .addMultipartFile("imgFile3", imgFile3)
                .addMultipartFile("imgFile2", imgFile2)
                .addMultipartFile("imgFile1", imgFile1)
                .addMultipartParameter("userId",userId)
                .addMultipartParameter("productName",productName)
                .addMultipartParameter("categoryId",categoryId)
                .addMultipartParameter("categoryName",categoryName)
                .addMultipartParameter("productPrice",productPrice)
                .addMultipartParameter("shortDescription",shortDescription)
                .addMultipartParameter("longDescription",longDescription)
                .addMultipartParameter("color",color)
                .addMultipartParameter("size",size)
                .addMultipartParameter("method",webMethName)
                .setTag(webMethName)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            String res = response.getString("addProductDetailsResponse");
                            if(res.equals("PRODUCT_DETAILS_SAVED")){
                                Toast.makeText(context, "Product Added Successfully.", Toast.LENGTH_SHORT).show();
                                Intent gotoMainActivity = new Intent(context, MainActivity.class);
                                gotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoMainActivity);
                            }
                            else{
                                Toast.makeText(context, "Product Not Added.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error: "+anError.getErrorBody(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void AddImages(String imFor, final List<File> imgFileList, final ProgressDialog progressDialog) {

        webMethName = "ImagesAdd";

        if(imgFileList.size() < 10) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
            File newFile = new File(storageDir + "/no_image.png");
            try {
                OutputStream os = new FileOutputStream(newFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            for(int i = imgFileList.size(); i < 10; i++) {
                imgFileList.add(newFile);
            }
        }

        String url = context.getString(R.string.url);
        AndroidNetworking.upload(url)
                .addMultipartFile("imgFile10", imgFileList.get(9))
                .addMultipartFile("imgFile9", imgFileList.get(8))
                .addMultipartFile("imgFile8", imgFileList.get(7))
                .addMultipartFile("imgFile7", imgFileList.get(6))
                .addMultipartFile("imgFile6", imgFileList.get(5))
                .addMultipartFile("imgFile5", imgFileList.get(4))
                .addMultipartFile("imgFile4", imgFileList.get(3))
                .addMultipartFile("imgFile3", imgFileList.get(2))
                .addMultipartFile("imgFile2", imgFileList.get(1))
                .addMultipartFile("imgFile1", imgFileList.get(0))
                .addMultipartParameter("imFor",imFor)
                .addMultipartParameter("method",webMethName)
                .setTag(webMethName)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String res = response.getString("AddImagesResponse");
                            if (res.equals("IMAGES_ADDED")) {
                                for(int i = 0; i < imgFileList.size(); i++) {
                                    imgFileList.get(i).delete();
                                }
                                Toast.makeText(context, "Images Successfully Added.", Toast.LENGTH_LONG).show();
                                Intent gotoNews = new Intent(context, Admin_Rights_Add.class);
                                gotoNews.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoNews);

                            } else {
                                Toast.makeText(context," "+res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialog.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
