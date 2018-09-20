package com.mobitechs.vbags.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.model.Rating_Review_Items;

import java.util.List;

public class Rating_Review_Adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Rating_Review_Items> ratingReviewsItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int wishListCounte = 0;
    TextView lblNoRatings;

    public Rating_Review_Adapter(List<Rating_Review_Items> items, TextView lblNoRatings) {
        this.ratingReviewsItems = items;
        this.lblNoRatings = lblNoRatings;

    }
//    public Rating_Review_Adapter(List<Rating_Review_Items> items ) {
//        this.ratingReviewsItems = items;
//
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
//            Rating_Review_Adapter.EmptyViewHolder emptyViewHolder = new Rating_Review_Adapter.EmptyViewHolder(v);
//            return emptyViewHolder;
//        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rating_review_items, viewGroup, false);
        viewHolder = new Rating_Review_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Rating_Review_Adapter.ViewHolder) {
            Rating_Review_Adapter.ViewHolder vHolder = (Rating_Review_Adapter.ViewHolder) viewHolder;
            Rating_Review_Items itemOflist = ratingReviewsItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        if (ratingReviewsItems.size() == 0) {

        }else{
            lblNoRatings.setVisibility(View.GONE);
        }
        return ratingReviewsItems.size();
    }

//    public int getItemViewType(int position) {
//        if (ratingReviewsItems.size() == 0) {
//            return VIEW_TYPE_EMPTY;
//        }
//        return super.getItemViewType(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView lblUserName;
        public TextView lblReview;
        public TextView lblRating;
        public TextView lblPostDate;

        Rating_Review_Items ratingReviewsItems = new Rating_Review_Items();


        public ViewHolder(View itemView) {
            super(itemView);
            lblUserName = (TextView) itemView.findViewById(R.id.lblUserName);
            lblReview = (TextView) itemView.findViewById(R.id.lblReview);
            lblRating = (TextView) itemView.findViewById(R.id.lblRating);
            lblPostDate = (TextView) itemView.findViewById(R.id.lblPostDate);

        }

        public void bindListDetails(Rating_Review_Items ratingReviewsItems) {
            this.ratingReviewsItems = ratingReviewsItems;

            double ratings = Double.parseDouble(ratingReviewsItems.getRating());

            if(ratings == 0.00 || ratings == 0.0){
                lblRating.setText("Ratings Not Yet");
            }
            if(ratings > 0.00 && ratings < 2 ){
                //red
                lblRating.setBackgroundColor(v.getResources().getColor(R.color.lowRating));
            }
            else if(ratings > 2 && ratings < 3.5){
                //orange
                lblRating.setBackgroundColor(v.getResources().getColor(R.color.mediumRating));
            }
            else if(ratings > 3.5){
                //Green
                lblRating.setBackgroundColor(v.getResources().getColor(R.color.goodRating));
            }

            String userName = ratingReviewsItems.getUserFirstName() + " "+ ratingReviewsItems.getUserLastName();
            lblUserName.setText(userName);
            lblReview.setText(ratingReviewsItems.getReview());
            lblRating.setText(ratings +"*");
            lblPostDate.setText(ratingReviewsItems.getPostDate()+"");

        }

        @Override
        public void onClick(View v) {
            
        }
    }

//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public EmptyViewHolder(View v) {
//            super(v);
//            TextView emptyTextView;
//            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
//            emptyTextView.setText("List not available.");
//        }
//    }
}
