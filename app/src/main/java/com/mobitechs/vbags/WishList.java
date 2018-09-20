package com.mobitechs.vbags;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Wish_List_Adapter;
import com.mobitechs.vbags.animation.RevealAnimation;
import com.mobitechs.vbags.connectivity.Req_Wish_List;
import com.mobitechs.vbags.model.Product_ListItems;
import com.mobitechs.vbags.scrollListeners.ProductListScrollListener;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WishList extends BaseActivity {
    

    private static int currentPage = 1;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    public List<Product_ListItems> listItems = new ArrayList<Product_ListItems>();
    private Wish_List_Adapter listAdapter;
    SessionManager sessionManager;
    String userId="";
    RelativeLayout notLoggedInLayout,containerLayout;
    CoordinatorLayout coordinatorLayout;
    RevealAnimation mRevealAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_list);
        sessionManager  = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        Intent intent = this.getIntent();   //get the intent to recieve the x and y coords, that you passed before
        CoordinatorLayout rootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout); //there you have to get the root layout of your second activity
        mRevealAnimation = new RevealAnimation(rootLayout, intent, this);

        notLoggedInLayout = (RelativeLayout) findViewById(R.id.notLoggedInLayout);
        containerLayout = (RelativeLayout) findViewById(R.id.containerLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        if (userId == null) {
            userId = "";
        }
        if(userId.equals("") || userId == null){
            notLoggedInLayout.setVisibility(View.VISIBLE);
            containerLayout.setVisibility(View.GONE);
        }else{
            containerLayout.setVisibility(View.VISIBLE);
            notLoggedInLayout.setVisibility(View.GONE);

            recyclerView = (RecyclerView) findViewById(R.id.wishListRecyclerView);
            recyclerView.setHasFixedSize(true);
            gridLayoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);

            recyclerView.smoothScrollToPosition(0);
            listAdapter = new Wish_List_Adapter(this,listItems);
            recyclerView.setAdapter(listAdapter);

            getProductList(currentPage);

            recyclerView.addOnScrollListener(new ProductListScrollListener(gridLayoutManager, currentPage) {

                @Override
                public void onLoadMore(int currentPage) {
                    getProductList(currentPage);
                }
            });
            recyclerView.smoothScrollToPosition(0);
        }

    }
    private void getProductList(int currentPage) {


        try{
            Req_Wish_List reqWishList_ = new Req_Wish_List(this);
            reqWishList_.getWishListProductList(listItems, recyclerView, listAdapter,currentPage,userId);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed()
    {
        mRevealAnimation.unRevealActivity();
    }

}
