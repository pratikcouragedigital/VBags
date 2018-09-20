package com.mobitechs.vbags.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobitechs.vbags.My_Custom_Bag;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.singleton.My_Custom_Bag_Instance;

import java.util.ArrayList;

public class My_Custom_Bags_Color_Adapter extends RecyclerView.Adapter<My_Custom_Bags_Color_Adapter.ViewHolder> {

    //vars
    private ArrayList<String> colorNames = new ArrayList<>();
    private ArrayList<String> colorImage = new ArrayList<>();
    private Context mContext;

    public My_Custom_Bags_Color_Adapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls) {
        colorNames = names;
        colorImage = imageUrls;
        mContext = context;
    }

    @Override
    public My_Custom_Bags_Color_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_custom_bags_item, parent, false);
        return new My_Custom_Bags_Color_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(My_Custom_Bags_Color_Adapter.ViewHolder holder, final int position) {
//        Glide.with(mContext)
//                .asBitmap()
//                .load(colorImage.get(position))
//                .into(holder.image);

//        holder.image.setColorFilter(Integer.parseInt(colorImage.get(position)));

        holder.image.setColorFilter(Color.parseColor(colorImage.get(position)));

        holder.name.setText(colorNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, colorNames.get(position), Toast.LENGTH_SHORT).show();

                String imageColor = colorImage.get(position);

                My_Custom_Bag_Instance customBagsInstance = new My_Custom_Bag_Instance();
                customBagsInstance.setBagColor(imageColor);
                My_Custom_Bag.UpdateCustomBag();
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorImage.size();
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