package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Add_Ratings_Reviews;
import com.mobitechs.vbags.Product_List_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.URL_WebService;
import com.mobitechs.vbags.adapter.Rating_Review_Adapter;
import com.mobitechs.vbags.model.Rating_Review_Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Req_Product_Rating_Reviews {

    private static Context context;
    private String productCategory;
    private String webMethod;
    private URL_WebService URL;
    public String ResponseResult;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private RecyclerView recyclerViewForAsyncTask;
    private static List<Rating_Review_Items> ItemsArrayForAsyncTask;

    public Req_Product_Rating_Reviews(Product_List_Details product_list_details) {
        context = product_list_details;
    }

    public Req_Product_Rating_Reviews(Add_Ratings_Reviews add_ratings_reviews) {
        context = add_ratings_reviews;
    }

    public void Add_Rating_Review(String productId, String userId, String ratingValue, String userFeedback, final ProgressDialog pd) {

        JSONObject jsonObject = new JSONObject();
        try {
            webMethod ="addProductRatingReview";
            jsonObject.put("method", webMethod);
            jsonObject.put("productId", productId);
            jsonObject.put("userId ", userId);
            jsonObject.put("rating", ratingValue);
            jsonObject.put("review", userFeedback);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("addProductRatingReviews")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        try {
                            String result = response.getString("addProductRatingReviewResponse");
                            if (!result.equals("RATING_REVIEW_ADDED_SUCCESSFULLY")) {
                                Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Rating successfully added.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch(Exception e) {
                            e.getMessage();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        error.getErrorDetail();
                    }
                });
    }

    public void getRatingReviews(List<Rating_Review_Items> listItems, RecyclerView recyclerView, Rating_Review_Adapter listAdapter, String productId) {
        webMethod = "GetProductRatingReviews";
        adapterForAsyncTask = listAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = listItems;

        String params = "?method="+webMethod+"&productId="+productId;
        String url = context.getString(R.string.url) + params;

        AndroidNetworking.get(url)
                .setTag("productList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ItemsArrayForAsyncTask.clear();
                            JSONArray arr = response.getJSONArray("productRatingReviewsResponse");

                            if (arr.length() == 0) {
                               // Toast.makeText(context, "Ratings &amp; Reviews Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Rating_Review_Items rating_Review_Items = new Rating_Review_Items();
                                    rating_Review_Items.setUserid(obj.getString("userId"));
                                    rating_Review_Items.setRating(obj.getString("rating"));
                                    rating_Review_Items.setReview(obj.getString("review"));
                                    rating_Review_Items.setPostDate(obj.getString("entryDate"));
                                    rating_Review_Items.setUserFirstName(obj.getString("userFirstname"));
                                    rating_Review_Items.setUserLastName(obj.getString("userLastName"));

                                    ItemsArrayForAsyncTask.add(rating_Review_Items);
                                }
                                adapterForAsyncTask.notifyDataSetChanged();
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
}

