package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.Notification;
import com.mobitechs.vbags.Notification_Add;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.model.Notification_Items;
import com.mobitechs.vbags.model.Product_ListItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class Req_Notification {

    private static Context context;
    private static String ResponseResult;
    private static String webMethod,params,url ;


    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Notification_Items> ItemsArrayForAsyncTask;

    public Req_Notification(Notification notification) {
        context = notification;
    }

    public Req_Notification(Notification_Add notification_add) {
        context = notification_add;
    }

    public Req_Notification(Context con) {
        context = con;
    }


    public void NotificationList(List<Notification_Items> pdfItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, final ProgressBar progressBar) {

        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = pdfItems;


        webMethod = "showNotificationList";
        params = "?method=" + webMethod;
        url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("showWishList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {


                            if (response.getString("showNotificationListResponse").equals("Notification Not Available.")) {
                                Toast.makeText(context, "Notification Not Available.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JSONArray arr = response.getJSONArray("showNotificationListResponse");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Notification_Items item = new Notification_Items();
                                    item.setListId(obj.getString("notificationId"));
                                    item.settitle(obj.getString("title"));
                                    item.setmessage(obj.getString("message"));
                                    item.setImage(obj.getString("imagePath"));

                                    ItemsArrayForAsyncTask.add(item);
                                }
                                adapterForAsyncTask.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(View.GONE);
                        error.getErrorDetail();
                    }
                });
    }

    public void AddNotification(String title, String message, File imgFile1, final ProgressBar progressBar) {
        webMethod = "Add_Notification";
        String url = context.getString(R.string.url);
        AndroidNetworking.upload(url)
                .addMultipartFile("imgFile1", imgFile1)
                .addMultipartParameter("title",title)
                .addMultipartParameter("message",message)
                .addMultipartParameter("method",webMethod)
                .setTag(webMethod)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            String res = response.getString("addNotificationDetailsResponse");
                            if(res.equals("NOTIFICATION_ADDED")){
                                Toast.makeText(context, "Notification Added Successfully.", Toast.LENGTH_SHORT).show();
                                Intent gotoMainActivity = new Intent(context, MainActivity.class);
                                gotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoMainActivity);
                            }
                            else{
                                Toast.makeText(context, "Notification Not Added.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error: "+anError.getErrorBody(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public static void deleteNotification(String notificationId) {
        webMethod = "deleteNotification";
        params = "?method=" + webMethod + "&notificationId=" + notificationId;
        url = context.getString(R.string.url) + params;

             AndroidNetworking.get(url)
                .setTag("deleteNotification")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("deleteNotificationResponse");
                            if (result.equals("Notification_Deleted")) {
                                Toast.makeText(context, "Notification Successfully Deleted.", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }
}
