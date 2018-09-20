package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Add_Category;
import com.mobitechs.vbags.Add_Product;
import com.mobitechs.vbags.Generated_Order_List;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.adapter.Generated_Order_List_Adapter;
import com.mobitechs.vbags.adapter.My_Order_ProductList_Adapter;
import com.mobitechs.vbags.adapter.Spinner_Filter_Adapter;
import com.mobitechs.vbags.model.My_Order_ProductItems;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.sqlite_db.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Req_Category_Details {

    String categoryId, categoryName, userId;

    private DataBaseHelper mydb;
    private String webMethName;
    private static Context context;

    private static ProgressDialog progressDialogBox;
    SessionManager sessionManager;

    private static Spinner_Filter_Adapter SpinnerAdapter;

    private static List<String> allNameList = new ArrayList<String>();
    private static List<String> allIdList = new ArrayList<String>();

    private String webMethod;
    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<My_Order_ProductItems> ItemsArrayForAsyncTask;


    public Req_Category_Details(Add_Product add_product) {
        context = add_product;
    }

    public Req_Category_Details(Add_Category add_category) {
        context = add_category;
    }

    public Req_Category_Details(Generated_Order_List generated_order_list) {
        context = generated_order_list;
    }

    public void FetchCategory(List<String> categoryNameList, List<String> categoryIdList, Spinner_Filter_Adapter spinnerAdapter) {

        allNameList = categoryNameList;
        allIdList = categoryIdList;
        SpinnerAdapter = spinnerAdapter;
        mydb = new DataBaseHelper(context);

        webMethName = "showCategoryList";

        String url = context.getString(R.string.url) + "?method=" + webMethName;
        AndroidNetworking.get(url)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res = response.getString("showCategoriesResponse");
                            if (res.equals("Category Not Available.")) {

                            } else {

                                try {
                                    JSONArray jArr = response.getJSONArray("showCategoriesResponse");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        try {
                                            JSONObject obj = jArr.getJSONObject(count);
                                            categoryId = (obj.getString("categoryId"));
                                            categoryName = (obj.getString("categoryName"));
                                            mydb.insertCategory(categoryId, categoryName);
                                            allNameList.add(obj.getString("categoryName"));
                                            allIdList.add(obj.getString("categoryId"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    SpinnerAdapter.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(context, "Exception" + ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}
