package com.mobitechs.vbags.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mobitechs.vbags.PaymentActivity;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.model.CartList_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.singleton.UserWishList_Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Payment_ProductList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CartList_Items> listItems = new ArrayList<CartList_Items>();
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int wishListCounte = 0;

    int prodcutCount = 1, payableAmount = 0, allProductPrice = 0, allproductDelCharges = 0, allproductTotalPriceWithDelivery = 0;
    TextView txtAllProductPrice, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable;
    int previousQty ,selectedQty;
    UserWishList_Instance userProductWishList = new UserWishList_Instance();

    ArrayList<String> productIdArray = new ArrayList<String>();
    ArrayList<String> productNameArray = new ArrayList<String>();
    ArrayList<String> productPriceArray = new ArrayList<String>();
    ArrayList<String> productPriceWithQtyArray = new ArrayList<String>();
    ArrayList<String> productQtyArray = new ArrayList<String>();
    ArrayList<String> productDeliveryChargesArray = new ArrayList<String>();

    Spinner spinnerQty;
    String[]qtyArray = {"1", "2","3", "4","5","6","7","8","9","10","11", "12","13", "14","15","16","17","18","19","20"};

    SessionManager sessionManager;;

    public Payment_ProductList_Adapter(List<CartList_Items> items, TextView txtAllProductPriceC, TextView txtAllProductQtyC, TextView txtAllProductDelChargesC, TextView txtAmountPayableC) {
        this.listItems = items;
        this.txtAllProductPrice = txtAllProductPriceC;
        this.txtAllProductQty = txtAllProductQtyC;
        this.txtAllProductDelCharges = txtAllProductDelChargesC;
        this.txtAmountPayable = txtAmountPayableC;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Payment_ProductList_Adapter.EmptyViewHolder emptyViewHolder = new Payment_ProductList_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_product_list_items, viewGroup, false);
        viewHolder = new Payment_ProductList_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Payment_ProductList_Adapter.ViewHolder) {
            Payment_ProductList_Adapter.ViewHolder vHolder = (Payment_ProductList_Adapter.ViewHolder) viewHolder;
            CartList_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);

            //productIdArray,productNameArray,productPriceArray,productQtyArray,productDeliveryChargesArray
            productIdArray.add(itemOflist.getproductId());
            productPriceArray.add(itemOflist.getproductPrice());
            productQtyArray.add(itemOflist.getqty());
            productDeliveryChargesArray.add(itemOflist.getdeliveryCharges());
            productPriceWithQtyArray.add(itemOflist.gettotalPrice());
            productNameArray.add(itemOflist.getproductName());

            int listSize = listItems.size();
            prodcutCount = position + 1;
            int productPrice = Integer.parseInt(itemOflist.getproductPrice());
            int qty = Integer.parseInt(itemOflist.getqty());

            int delCharge = Integer.parseInt(itemOflist.getdeliveryCharges());
            int alltotal = (qty * productPrice) + delCharge;

            allProductPrice = allProductPrice + (qty * productPrice);
            allproductDelCharges = allproductDelCharges + delCharge;
            allproductTotalPriceWithDelivery = allproductTotalPriceWithDelivery + alltotal;
            payableAmount = allproductTotalPriceWithDelivery;
            PaymentActivity.updatePriceDetails(prodcutCount,allProductPrice,allproductDelCharges);
        }
    }

    @Override
    public int getItemCount() {
        if (listItems.size() > 0) {
            return listItems.size();
        } else {
            return 1;
        }
//        return cartListItems.size();
    }

    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName;
        public TextView txtProductPrice;
        public TextView txtProductQty;

        public TextView txtTotalPrice;
        public TextView txtDeliveryCharges;
        public ImageView productImage;
        public ProgressBar imageLoader;

        public View cardView;
        public ArrayList<String> productWishListArray;
        public String productWishListId;
        String userId, productId, qty;
        CoordinatorLayout coordinatorLayout;

        CartList_Items cartListItems = new CartList_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            userId = user.get(SessionManager.KEY_USERID);

            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtProductQty = (TextView) itemView.findViewById(R.id.txtProductQty);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txtTotalPrice);
            txtDeliveryCharges = (TextView) itemView.findViewById(R.id.txtDelCharges);
            coordinatorLayout = (CoordinatorLayout) itemView.findViewById(R.id.coordinatorLayout);
            cardView = itemView;
            cardView.setOnClickListener(this);

        }

        public void bindListDetails(CartList_Items cartListItems) {
            this.cartListItems = cartListItems;

            int productTotalPrice = Integer.parseInt(cartListItems.getproductPrice()) * Integer.parseInt(cartListItems.getqty()) + Integer.parseInt(cartListItems.getdeliveryCharges());
            txtProductName.setText(cartListItems.getproductName());
            txtProductPrice.setText(cartListItems.getproductPrice() + "");
            txtTotalPrice.setText(productTotalPrice + "");
            txtDeliveryCharges.setText("+" + cartListItems.getdeliveryCharges());
            txtProductQty.setText("x" + cartListItems.getqty());

            String imagepath = cartListItems.getImagePath();
            RequestOptions options = new RequestOptions().error(R.drawable.a);
            Glide.with(productImage.getContext())
                    .asBitmap()
                    .load(imagepath)
                    .apply(options)
                    .listener(new RequestListener<Bitmap>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            imageLoader.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            imageLoader.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into((productImage));

        }

        @Override
        public void onClick(View v) {
            
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("List Not Available.");
        }
    }
}