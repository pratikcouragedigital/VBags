package com.mobitechs.vbags.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.mobitechs.vbags.My_Orders_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.Add_Ratings_Reviews;
import com.mobitechs.vbags.model.My_Order_ProductItems;
import com.shuhart.stepview.StepView;

import java.util.List;

public class My_Oder_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<My_Order_ProductItems> productListItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;


    public My_Oder_Adapter(List<My_Order_ProductItems> items) {
        this.productListItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY )  {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            My_Oder_Adapter.EmptyViewHolder emptyViewHolder = new My_Oder_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_list_items, viewGroup, false);
        viewHolder = new My_Oder_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof My_Oder_Adapter.ViewHolder) {
            My_Oder_Adapter.ViewHolder vHolder = (My_Oder_Adapter.ViewHolder) viewHolder;
            My_Order_ProductItems itemOflist = productListItems.get(position);
            vHolder.bindListDetails(itemOflist );
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

        public TextView txtDelCharges;
        public TextView txtTotalPrice;
        public TextView txtDeliveryStatus;
        public TextView txtDate;
        public TextView txtOrderId;
        public View cardView;
        StepView stepView;
        int deliveryStatusId = 1;

        My_Order_ProductItems productListItems = new My_Order_ProductItems();

        public ViewHolder(View itemView) {
            super(itemView);
            stepView = itemView.findViewById(R.id.step_view);
//            txtDelCharges = (TextView) itemView.findViewById(R.id.txtDelCharges);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txtTotalPrice);
            txtDeliveryStatus = (TextView) itemView.findViewById(R.id.txtDeliveryStatus);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderId);

            cardView = itemView;
            cardView.setOnClickListener(this);

        }

        public void bindListDetails(My_Order_ProductItems productListItems) {
            this.productListItems = productListItems;

            String status  = productListItems.getdeleveryStatus();
            deliveryStatusId  = Integer.parseInt(productListItems.getorderStatusId());

            if(status.equals("Pending") ){
                txtDeliveryStatus.setTextColor(v.getResources().getColor(R.color.lowRating));
            }
            else if(status.equals("Delivered")){
                txtDeliveryStatus.setTextColor(v.getResources().getColor(R.color.goodRating));
            }

//            stepView.go(1, true);

//            txtDelCharges.setText(productListItems.getdeliveryCharges());
            txtDate.setText(productListItems.getordered_Date());
            txtTotalPrice.setText(productListItems.gettotalPrice());
            txtDeliveryStatus.setText(productListItems.getdeleveryStatus());
            txtOrderId.setText(productListItems.getorderBulkId());
        }

        @Override
        public void onClick(View v) {
//            if(v.getId() == R.id.btnAddReview){
////                Intent ratingReview = new Intent(v.getContext(), Add_Ratings_Reviews.class);
////                ratingReview.putExtra("productId", productListItems.getproductId());
////                v.getContext().startActivity(ratingReview);
//            }
             if (this.productListItems != null) {
                Intent productDetails = new Intent(v.getContext(), My_Orders_Details.class);

                productDetails.putExtra("OrderId", productListItems.getorderId());
                productDetails.putExtra("OrderBulkId", productListItems.getorderBulkId());
                productDetails.putExtra("ProductPrice", productListItems.getproductPrice());
                productDetails.putExtra("Qty", productListItems.getqty());
                productDetails.putExtra("DeliveryCharges", productListItems.getdeliveryCharges());
                productDetails.putExtra("TotalPrice", productListItems.gettotalPrice());
                productDetails.putExtra("OrderStatusId", productListItems.getorderStatusId());
                productDetails.putExtra("DeliveryStatus", productListItems.getdeleveryStatus());
                productDetails.putExtra("Date", productListItems.getordered_Date());
                productDetails.putExtra("Username", productListItems.getusername());
                productDetails.putExtra("Address", productListItems.getaddress());
                productDetails.putExtra("UserMobile", productListItems.getmobileNo());
                productDetails.putExtra("UserEmail", productListItems.getEmail());
                productDetails.putExtra("LuckyDrawAmount", productListItems.getluckyDrawPrice());
                v.getContext().startActivity(productDetails);
            }
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