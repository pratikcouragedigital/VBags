package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Generated_Order_List_Adapter;
import com.mobitechs.vbags.connectivity.Req_Generated_Order_Details;
import com.mobitechs.vbags.model.My_Order_ProductItems;
import com.mobitechs.vbags.scrollListeners.ProductListScrollListener;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Generated_Order_List extends BaseActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public List<My_Order_ProductItems> listItems = new ArrayList<My_Order_ProductItems>();
    private ProgressDialog progressDialog;
    private Generated_Order_List_Adapter listAdapter;
    String userId="";
    private static int currentPage = 1;
    SessionManager sessionManager;
    RelativeLayout notLoggedInLayout,containerLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_order_list);

        sessionManager  = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        if (userId == null) {
            userId = "";
        }

        notLoggedInLayout = (RelativeLayout) findViewById(R.id.notLoggedInLayout);
        containerLayout = (RelativeLayout) findViewById(R.id.containerLayout);

        if(userId.equals("") || userId == null){
            notLoggedInLayout.setVisibility(View.VISIBLE);
            containerLayout.setVisibility(View.GONE);
        }
        else{
            containerLayout.setVisibility(View.VISIBLE);
            notLoggedInLayout.setVisibility(View.GONE);

            recyclerView = (RecyclerView) findViewById(R.id.previousOrderedListRecyclerView);
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.smoothScrollToPosition(0);
            listAdapter = new Generated_Order_List_Adapter(listItems);
            recyclerView.setAdapter(listAdapter);

            getProductList(currentPage);

            recyclerView.addOnScrollListener(new ProductListScrollListener(linearLayoutManager, currentPage) {

                @Override
                public void onLoadMore(int currentPage) {
                    getProductList(currentPage);
                }
            });
            recyclerView.smoothScrollToPosition(0);
        }
    }
    private void getProductList(int currentPage) {

        try {
            Req_Generated_Order_Details GetCategoryDetails = new Req_Generated_Order_Details(this);
            GetCategoryDetails.getGeneratedOrderList(listItems, recyclerView, listAdapter,currentPage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
