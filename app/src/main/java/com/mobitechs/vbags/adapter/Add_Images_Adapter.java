package com.mobitechs.vbags.adapter;

import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobitechs.vbags.R;

import java.io.File;
import java.util.List;

public class Add_Images_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<File> imgFileList;
    private final FloatingActionButton postEventImagesFab;
    private View v;
    private RecyclerView.ViewHolder viewHolder;

    public Add_Images_Adapter(List<File> imgFileList, FloatingActionButton postEventImagesFab) {
        this.imgFileList = imgFileList;
        
        this.postEventImagesFab = postEventImagesFab;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_images_items, viewGroup, false);
        viewHolder = new Add_Images_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Add_Images_Adapter.ViewHolder vHolder = (Add_Images_Adapter.ViewHolder) viewHolder;
        File imgFile = imgFileList.get(position);
        vHolder.bindListDetails(imgFile);
    }

    @Override
    public int getItemCount() {
        return imgFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView eventImage;

        public ViewHolder(View itemView) {
            super(itemView);

            eventImage = (ImageView) itemView.findViewById(R.id.image);
        }


        public void bindListDetails(File imgFile) {
            eventImage.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }
    }
}
