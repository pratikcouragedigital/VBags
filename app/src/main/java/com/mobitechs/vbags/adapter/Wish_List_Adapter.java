package com.mobitechs.vbags.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.mobitechs.vbags.Product_List_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.WishList;
import com.mobitechs.vbags.connectivity.Req_Wish_List;
import com.mobitechs.vbags.model.Product_ListItems;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.singleton.UserWishList_Instance;

import java.util.HashMap;
import java.util.List;

public class Wish_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Product_ListItems> productListItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int wishListCounte = 0;
    Activity mActivity;


    public Wish_List_Adapter(WishList mActivity, List<Product_ListItems> items) {
        this.mActivity = mActivity;
        this.productListItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Wish_List_Adapter.EmptyViewHolder emptyViewHolder = new Wish_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_items, viewGroup, false);
        viewHolder = new Wish_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Wish_List_Adapter.ViewHolder) {
            Wish_List_Adapter.ViewHolder vHolder = (Wish_List_Adapter.ViewHolder) viewHolder;
            Product_ListItems itemOflist = productListItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        if (productListItems.size() > 0) {
            return productListItems.size();
        } else {
            return 1;
        }
//        return productListItems.size();
    }

    public int getItemViewType(int position) {
        if (productListItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener, OnAnimationEndListener {

        LikeButton btnWishList;
        public TextView lblProductName;
        public TextView lblPrice;
        public TextView lblRatings;
        public ImageView productImage;
        public View cardView;
        String userId, productId;
        CoordinatorLayout coordinatorLayout;

        UserWishList_Instance userProductWishList = new UserWishList_Instance();
        Product_ListItems productListItems = new Product_ListItems();
        private ProgressBar imageLoader;

        public ViewHolder(View itemView) {
            super(itemView);
            lblProductName = (TextView) itemView.findViewById(R.id.lblProductName);
            lblPrice = (TextView) itemView.findViewById(R.id.lblPrice);
            lblRatings = (TextView) itemView.findViewById(R.id.lblRatings);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            btnWishList = (LikeButton) itemView.findViewById(R.id.btnWishList);
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);
            coordinatorLayout = (CoordinatorLayout) itemView.findViewById(R.id.coordinatorLayout);
            cardView = itemView;
            cardView.setOnClickListener(this);
            btnWishList.setOnLikeListener(this);
            btnWishList.setOnAnimationEndListener(this);
        }

        public void bindListDetails(Product_ListItems productListItems) {
            this.productListItems = productListItems;

            lblProductName.setText(productListItems.getproductName());
            lblPrice.setText(productListItems.getProductPrice());
            lblRatings.setText(productListItems.getRatings() + "*");

            btnWishList.setLiked(true);

            String rating = productListItems.getRatings();
            if (rating.equals("0.00")) {
                lblRatings.setText("Ratings Not Yet");
                lblRatings.setVisibility(View.GONE);
            } else {
                lblRatings.setVisibility(View.VISIBLE);

                double ratings = Double.parseDouble(productListItems.getRatings());

                if (ratings < 2) {
                    //red
                    lblRatings.setBackgroundColor(v.getResources().getColor(R.color.lowRating));
                } else if (ratings > 2 && ratings < 3.5) {
                    //orange
                    lblRatings.setBackgroundColor(v.getResources().getColor(R.color.mediumRating));
                } else if (ratings > 3.5) {
                    //Green
                    lblRatings.setBackgroundColor(v.getResources().getColor(R.color.goodRating));
                }
            }

            String imagepath = productListItems.getFirstImagePath();
            if (imagepath == null || imagepath.equals(" ")) {
                productImage.setBackground(v.getResources().getDrawable(R.drawable.a));
                imageLoader.setVisibility(View.GONE);
            } else {
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
                        .into(productImage);
            }
        }

        @Override
        public void onClick(View v) {

            if (this.productListItems != null) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity);
                Intent productDetails = new Intent(v.getContext(), Product_List_Details.class);
                productDetails.putExtra("Image", productListItems.getFirstImagePath());
                productDetails.putExtra("Image2", productListItems.get2ndImagePath());
                productDetails.putExtra("Image3", productListItems.get3rdImagePath());
                productDetails.putExtra("Image4", productListItems.get4thImagePath());
                productDetails.putExtra("ProductId", productListItems.getProductId());
                productDetails.putExtra("ProductName", productListItems.getproductName());
                productDetails.putExtra("ProductImage", productListItems.getFirstImagePath());
                productDetails.putExtra("CategoryId", productListItems.getCategoryId());
                productDetails.putExtra("CategoryName", productListItems.getCategoryName());
                productDetails.putExtra("ProductPrice", productListItems.getProductPrice());
                productDetails.putExtra("LongDescription", productListItems.getProductLongDescription());
                productDetails.putExtra("ShortDescription", productListItems.getProductShortDescription());
                productDetails.putExtra("Rating", productListItems.getRatings());
                productDetails.putExtra("Colour", productListItems.getColor());
                productDetails.putExtra("Size", productListItems.getSize());
                productDetails.putExtra("DeliveryCharges", productListItems.getDeliveryCharges());
                productDetails.putExtra("ImFrom", "WishList");

//                v.getContext().startActivity(productDetails);
                v.getContext().startActivity(productDetails, options.toBundle());
            }

        }


        @Override
        public void liked(LikeButton likeButton) {

        }

        @Override
        public void unLiked(LikeButton likeButton) {
            SessionManager sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            userId = user.get(SessionManager.KEY_USERID);

            productId = productListItems.getProductId();
            deleteWishList(getPosition());
            try {
                Req_Wish_List.deleteWishList(userId, productId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAnimationEnd(LikeButton likeButton) {

        }
    }

    public void deleteWishList(int position) {
        productListItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productListItems.size());
        notifyDataSetChanged();
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
