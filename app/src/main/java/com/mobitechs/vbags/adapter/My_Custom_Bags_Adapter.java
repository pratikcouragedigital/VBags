package com.mobitechs.vbags.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobitechs.vbags.Cart_List;
import com.mobitechs.vbags.My_Custom_Bag;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.singleton.Address_Instance;
import com.mobitechs.vbags.singleton.My_Custom_Bag_Instance;

import java.util.ArrayList;


/**
 * Created by User on 2/12/2018.
 */

public class My_Custom_Bags_Adapter extends RecyclerView.Adapter<My_Custom_Bags_Adapter.ViewHolder> {

    private static final String TAG = "My_Custom_Bags_Adapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public My_Custom_Bags_Adapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_custom_bags_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                String imageName = mImageUrls.get(position);

                My_Custom_Bag_Instance customBagsInstance = new My_Custom_Bag_Instance();
                customBagsInstance.setBagImage(imageName);
                My_Custom_Bag.UpdateCustomBag();



            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
