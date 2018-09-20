package com.mobitechs.vbags.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
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
import com.mobitechs.vbags.My_Orders_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.model.My_Order_ProductItems;

import java.util.List;


public class My_Order_ProductList_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<My_Order_ProductItems> productListItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int prodcutCount = 1, payableAmount = 0, allProductPrice = 0, allproductDelCharges = 0, allproductTotalPriceWithDelivery = 0;

    public My_Order_ProductList_Adapter(List<My_Order_ProductItems> items) {
        this.productListItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            My_Order_ProductList_Adapter.EmptyViewHolder emptyViewHolder = new My_Order_ProductList_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_product_list_details_items, viewGroup, false);
        viewHolder = new My_Order_ProductList_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof My_Order_ProductList_Adapter.ViewHolder) {
            My_Order_ProductList_Adapter.ViewHolder vHolder = (My_Order_ProductList_Adapter.ViewHolder) viewHolder;
            My_Order_ProductItems itemOflist = productListItems.get(position);
            vHolder.bindListDetails(itemOflist );


            int listSize = productListItems.size();
            prodcutCount = position + 1;
            int productPrice = Integer.parseInt(itemOflist.getproductPrice());
            int qty = Integer.parseInt(itemOflist.getqty());

            int delCharge = Integer.parseInt(itemOflist.getdeliveryCharges());
            int alltotal = (qty * productPrice) + delCharge;


            allProductPrice = allProductPrice + (qty * productPrice);
            allproductDelCharges = allproductDelCharges + delCharge;
            allproductTotalPriceWithDelivery = allproductTotalPriceWithDelivery + alltotal;
            payableAmount = allproductTotalPriceWithDelivery;

            My_Orders_Details.updatePriceDetails(prodcutCount,allProductPrice,allproductDelCharges);
        }
    }

    @Override
    public int getItemCount() {
        if(productListItems.size() > 0){
            return productListItems.size();
        }else {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        public Button btnAddReview;
        public TextView txtProductName,txtProductPrice,txtProductQty,txtDelCharges;
        public TextView txtColor;
        public TextView txtDeliveryStatus;
        public TextView txtSize;
        public TextView txtShortDescription;
        public TextView txtLongDescription;
        public ImageView productImage;
        public View cardView;

        My_Order_ProductItems productListItems = new My_Order_ProductItems();
        private ProgressBar imageLoader;

        public ViewHolder(View itemView) {
            super(itemView);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            txtProductQty = (TextView) itemView.findViewById(R.id.txtProductQty);
            txtDelCharges = (TextView) itemView.findViewById(R.id.txtDelCharges);
            txtDeliveryStatus = (TextView) itemView.findViewById(R.id.txtDeliveryStatus);
            txtColor = (TextView) itemView.findViewById(R.id.txtColor);
            txtSize = (TextView) itemView.findViewById(R.id.txtSize);
            txtShortDescription = (TextView) itemView.findViewById(R.id.txtShortDescription);
            txtLongDescription = (TextView) itemView.findViewById(R.id.txtLongDescription);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);
            //btnAddReview = (Button) itemView.findViewById(R.id.btnAddReview);

            cardView = itemView;
            cardView.setOnClickListener(this);
          //  btnAddReview.setOnClickListener(this);
        }

        public void bindListDetails(My_Order_ProductItems productListItems) {
            this.productListItems = productListItems;

            String status  = productListItems.getdeleveryStatus();

            int price = Integer.parseInt(productListItems.getproductPrice());
            int delChagre = Integer.parseInt(productListItems.getdeliveryCharges());
            int quantity = Integer.parseInt(productListItems.getqty());
            String colors = productListItems.getcolor();
            String size = productListItems.getsize();
            String shortDescription = productListItems.getproductShortDescription();
            String longDescription = productListItems.getproductLongDescription();

            if(status.equals("Pending") ){
                txtDeliveryStatus.setTextColor(v.getResources().getColor(R.color.lowRating));
            }
            else if(status.equals("Delivered")){
                txtDeliveryStatus.setTextColor(v.getResources().getColor(R.color.goodRating));
            }

            if(colors.equals("") || colors == null || colors.equals(" ")){
                txtColor.setVisibility(View.GONE);
            }
            else{
                txtColor.setVisibility(View.VISIBLE);
            }

            if(size.equals("") || size == null || size.equals(" ")){
                txtSize.setVisibility(View.GONE);
            }
            else{
                txtSize.setVisibility(View.VISIBLE);
            }

            if(shortDescription.equals("") || shortDescription == null || shortDescription.equals(" ")){
                txtShortDescription.setVisibility(View.GONE);
            }
            else{
                txtShortDescription.setVisibility(View.VISIBLE);
                txtShortDescription.setText(productListItems.getproductShortDescription());
            }
            if(longDescription.equals("") || longDescription == null || longDescription.equals(" ")){
                txtLongDescription.setVisibility(View.GONE);
            }
            else{
                txtLongDescription.setVisibility(View.VISIBLE);
                txtLongDescription.setText(productListItems.getproductLongDescription());
            }

            txtProductName.setText(productListItems.getproductName());
            txtDeliveryStatus.setText(productListItems.getdeleveryStatus());
            txtProductPrice.setText("Rs. "+productListItems.getproductPrice());
            txtProductQty.setText("Qty: "+productListItems.getqty());

            String imagepath = productListItems.getimagePath();

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
//            if(v.getId() == R.id.btnAddReview){
//                Intent ratingReview = new Intent(v.getContext(), Add_Ratings_Reviews.class);
//                ratingReview.putExtra("productId", productListItems.getproductId());
//                v.getContext().startActivity(ratingReview);
//            }
        }
    }
    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("No Order Yet");
        }
    }
}