package com.mobitechs.vbags.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.connectivity.Req_Notification;
import com.mobitechs.vbags.connectivity.Req_Wish_List;
import com.mobitechs.vbags.model.Notification_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.HashMap;
import java.util.List;

public class Notification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Notification_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    SessionManager sessionManager;
    String userType;

    private static final int VIEW_TYPE_EMPTY = 1;

    public Notification_Adapter(List<Notification_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (i == VIEW_TYPE_EMPTY) {
//            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_items, viewGroup, false);
//            Notification_Adapter.EmptyViewHolder emptyViewHolder = new Notification_Adapter.EmptyViewHolder(v);
//            return emptyViewHolder;
//        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_items, viewGroup, false);
        viewHolder = new Notification_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Notification_Adapter.ViewHolder) {
            Notification_Adapter.ViewHolder vHolder = (Notification_Adapter.ViewHolder) viewHolder;
            Notification_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }
    }

    @Override
    public int getItemCount() {
//        if(listItems.size() > 0){
//            return listItems.size();
//        }else {
//            return 1;
//        }
        return listItems.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (listItems.size() == 0) {
//            return VIEW_TYPE_EMPTY;
//        }
//        return super.getItemViewType(position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView txtMessage,txtTitle;
        ImageView image,imgDelete;
        public View cardView;

        Notification_Items nitificationListItems = new Notification_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            userType = user.get(SessionManager.KEY_USERTYPE);

            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            image = (ImageView) itemView.findViewById(R.id.image);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            cardView = itemView;

            if(userType.equals("Admin")){
                imgDelete.setVisibility(View.VISIBLE);
                imgDelete.setOnClickListener(this);
            }
            else{
                imgDelete.setVisibility(View.GONE);
            }
        }

        public void bindListDetails(Notification_Items listItems) {
            this.nitificationListItems = listItems;
            
            txtMessage.setText(listItems.getmessage());
            txtTitle.setText(listItems.gettitle());

            String imagePath = listItems.getImage();
            if(imagePath.equals("")){
                image.setVisibility(View.GONE);
            }
            else{
                image.setVisibility(View.VISIBLE);
                Glide.with(image.getContext())
                        .asBitmap()
                        .load(imagePath)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                               // image.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                               // image.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(image);
            }

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.imgDelete){
                try {
                    String notificationId = nitificationListItems.getListId();
                    removeFromCart(getPosition());
                    Req_Notification reqWishList = new Req_Notification(v.getContext());
                    reqWishList.deleteNotification(notificationId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void removeFromCart(int position) {

            listItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listItems.size());
            notifyDataSetChanged();
        }
    }

//    public class EmptyViewHolder extends RecyclerView.ViewHolder {
//        public EmptyViewHolder(View v) {
//            super(v);
//            TextView emptyTextView;
//            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
//            emptyTextView.setText("Notification Not Available.");
//        }
//    }
}