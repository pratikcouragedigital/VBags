package com.mobitechs.vbags.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.Product_List;
import com.mobitechs.vbags.Product_List_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.connectivity.Req_Wish_List;
import com.mobitechs.vbags.model.Product_ListItems;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.singleton.UserWishList_Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product_List_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Product_ListItems> productFilterListItems;
    private List<Product_ListItems> productListItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
//    private static final int VIEW_TYPE_EMPTY = 1;
    int wishListCounte = 0;
    Activity mActivity;
    int gridCount;


    public Product_List_adapter(MainActivity activity, List<Product_ListItems> items, int gridCount) {
        this.mActivity = activity;
        this.productFilterListItems = items;
        this.productListItems = items;
        this.gridCount = gridCount;

    }

    public Product_List_adapter(Product_List product_list, List<Product_ListItems> listItems, int gridCount) {
        this.mActivity = product_list;
        this.productFilterListItems = listItems;
        this.productListItems = listItems;
        this.gridCount = gridCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
//            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
//            return emptyViewHolder;
//        }

        if(gridCount > 1 ){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_gridview_items, viewGroup, false);
        }else{
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_listview_items, viewGroup, false);
        }

        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Product_ListItems itemOflist = productFilterListItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
//        if (productFilterListItems.size() > 0) {
//            return productFilterListItems.size();
//        } else {
//            return 1;
//        }
        return productFilterListItems.size();
    }

//    public int getItemViewType(int position) {
//        if (productFilterListItems.size() == 0) {
//            return VIEW_TYPE_EMPTY;
//        }
//        return super.getItemViewType(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener, OnAnimationEndListener {


        LikeButton btnWishList;
        public TextView lblProductName;
        public TextView lblPrice;
        public TextView lblRatings;
        public ImageView productImage;
        public View cardView;
        public ArrayList<String> productWishListArray;
        public String productWishListId;
        String userId ="", productId="";
        CoordinatorLayout coordinatorLayout;

        UserWishList_Instance userProductWishList = new UserWishList_Instance();
        Product_ListItems productFilterListItems = new Product_ListItems();
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
            lblRatings.setVisibility(View.VISIBLE);

            cardView.setOnClickListener(this);
            btnWishList.setOnLikeListener(this);
            btnWishList.setOnAnimationEndListener(this);
        }

        public void bindListDetails(Product_ListItems productFilterListItems) {
            this.productFilterListItems = productFilterListItems;

            productWishListArray = userProductWishList.getWishList();
            productWishListId = productFilterListItems.getProductId();
            lblProductName.setText(productFilterListItems.getproductName());
            lblPrice.setText(productFilterListItems.getProductPrice());
            lblRatings.setText(productFilterListItems.getRatings() + "*");

            if (productWishListArray.contains(productWishListId)) {
                btnWishList.setLiked(true);
            } else {
                btnWishList.setLiked(false);
            }

            String rating = productFilterListItems.getRatings();
            if (rating.equals("0.00")) {
                lblRatings.setText("Ratings Not Yet");
                lblRatings.setVisibility(View.GONE);
            } else {
                lblRatings.setVisibility(View.VISIBLE);

                double ratings = Double.parseDouble(productFilterListItems.getRatings());

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

            String imagepath = productFilterListItems.getFirstImagePath();
            if (imagepath == null || imagepath.equals(" ")){
                productImage.setBackground(v.getResources().getDrawable(R.drawable.a));
                imageLoader.setVisibility(View.GONE);
            }
            else{

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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
             if (this.productFilterListItems != null) {

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity);
                Intent productDetails = new Intent(v.getContext(), Product_List_Details.class);

                productDetails.putExtra("ProductImage", productFilterListItems.getFirstImagePath());
                productDetails.putExtra("Image", productFilterListItems.getFirstImagePath());
                productDetails.putExtra("Image2", productFilterListItems.get2ndImagePath());
                productDetails.putExtra("Image3", productFilterListItems.get3rdImagePath());
                productDetails.putExtra("Image4", productFilterListItems.get4thImagePath());
                productDetails.putExtra("ProductId", productFilterListItems.getProductId());
                productDetails.putExtra("ProductName", productFilterListItems.getproductName());
                productDetails.putExtra("CategoryId", productFilterListItems.getCategoryId());
                productDetails.putExtra("CategoryName", productFilterListItems.getCategoryName());
                productDetails.putExtra("ProductPrice", productFilterListItems.getProductPrice());
                productDetails.putExtra("LongDescription", productFilterListItems.getProductLongDescription());
                productDetails.putExtra("ShortDescription", productFilterListItems.getProductShortDescription());
                productDetails.putExtra("Rating", productFilterListItems.getRatings());
                productDetails.putExtra("Colour", productFilterListItems.getColor());
                productDetails.putExtra("Size", productFilterListItems.getSize());
                productDetails.putExtra("DeliveryCharges", productFilterListItems.getDeliveryCharges());
                productDetails.putExtra("ImFrom","ProductList");

               // v.getContext().startActivity(productDetails);
                 v.getContext().startActivity(productDetails, options.toBundle());
            }
        }

        @Override
        public void liked(LikeButton likeButton) {
            SessionManager sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            userId = user.get(SessionManager.KEY_USERID);
            productId = productFilterListItems.getProductId();

            if (userId == null || userId.equals("") ) {
//                    Snackbar snackbar = Snackbar
//                            .make(coordinatorLayout, "You are not logged in,You need to login first.", Snackbar.LENGTH_LONG);
//                    snackbar.show();
                Toast.makeText(v.getContext(), "You are not logged in,You need to login first.", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    Req_Wish_List reqWishList = new Req_Wish_List(v.getContext());
                    reqWishList.addToWishList(userId, productId);
                    productWishListArray.add(productId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            try {
                int pos = productWishListArray.indexOf(productId);
                productWishListArray.remove(pos);
                Req_Wish_List reqWishList_ = new Req_Wish_List(v.getContext());
                reqWishList_.deleteWishList(userId, productId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAnimationEnd(LikeButton likeButton) {

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


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productFilterListItems = productListItems;
                } else {
                    List<Product_ListItems> filteredList = new ArrayList<>();
                    for (Product_ListItems row : productListItems) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getproductName().toLowerCase().contains(charString.toLowerCase()) || row.getProductPrice().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    productFilterListItems = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productFilterListItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productFilterListItems = (ArrayList<Product_ListItems>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}