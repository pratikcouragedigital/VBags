package com.mobitechs.vbags.scrollListeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

public abstract class ProductListScrollListener extends RecyclerView.OnScrollListener{


    private int previousTotal = -1;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page;

    private RecyclerView.LayoutManager layoutManager;

    int lastInScreen;

    private boolean isUseLinearLayoutManager;
    private boolean isUseGridLayoutManager;

    public ProductListScrollListener(LinearLayoutManager linearLayoutManager, int current_page) {
        this.layoutManager = linearLayoutManager;
        this.current_page = current_page;
        isUseLinearLayoutManager = true;
    }

    public ProductListScrollListener(GridLayoutManager gridLayoutManager, int current_page) {
        this.layoutManager = gridLayoutManager;
        this.current_page = current_page;
        isUseGridLayoutManager = true;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            loading = true;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

//        visibleItemCount = layoutManager.getChildCount();
//        totalItemCount = layoutManager.getItemCount();
//
//        if(isUseLinearLayoutManager && layoutManager instanceof LinearLayoutManager){
//            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//
//        if(isUseGridLayoutManager && layoutManager instanceof GridLayoutManager){
//            firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//            }
//        }
//        //lastInScreen = firstVisibleItem + visibleItemCount;
////        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 2)) {
//        if (!loading && totalItemCount <= (firstVisibleItem + visibleItemCount)) {
//            // End has been reached
//
//            current_page++;
//            onLoadMore(current_page);
//            loading = true;
//        }

//        super.onScrolled(recyclerView, dx, dy);
//
//        visibleItemCount = recyclerView.getChildCount();
//        totalItemCount = layoutManager.getItemCount();
//        firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
//
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//            }
//        }
//
//        lastInScreen = firstVisibleItem + visibleItemCount;
//
//        if (!loading && (lastInScreen == totalItemCount - 2)) {
//            // End has been reached
//
//            current_page++;
//
//            onLoadMore(current_page);
//
//            loading = true;
//        }

        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();

        if(isUseLinearLayoutManager && layoutManager instanceof LinearLayoutManager){
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        if(isUseGridLayoutManager && layoutManager instanceof GridLayoutManager){
            firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        if (loading && (visibleItemCount + firstVisibleItem == totalItemCount)) {
            loading = false;
            current_page++;
            onLoadMore(current_page);
        }
    }

    public abstract void onLoadMore(int current_page);
}
