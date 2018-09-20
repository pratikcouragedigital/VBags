package com.mobitechs.vbags.Drawer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.My_Custom_Bag;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.Account;
import com.mobitechs.vbags.My_Orders;
import com.mobitechs.vbags.Cart_List;
import com.mobitechs.vbags.WishList;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static ArrayList<DrawerItems> itemsArrayList;
    public View v;
    public ViewHolder viewHolder;
    public DrawerLayout drawer;
    int positionOfItem = 0;
    SessionManager sessionManager;
    private Animation mAnimation;
    private Context mContext;

    private static ArrayList<DrawerItems> itemselectedArrayList;
    public static DrawerListInstance drawerListInstance = new DrawerListInstance();

    public DrawerAdapter(ArrayList<DrawerItems> itemsArrayList, ArrayList<DrawerItems> itemselectedArrayList, DrawerLayout drawer) {
        this.drawer = drawer;
        this.itemsArrayList = itemsArrayList;
        this.itemselectedArrayList = itemselectedArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_items, viewGroup, false);
        }
        else if (i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_item_text, viewGroup, false);
        }
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DrawerItems itemsList = itemsArrayList.get(i);
        DrawerItems itemselectedList = itemselectedArrayList.get(i);
        viewHolder.bindDrawerItemArrayList(i, itemsList, itemselectedList);
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (position <= 7) {
            viewType = 0;
        } else if (position > 7) {
            viewType = 1;
        }
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView vtextView;
        public ImageView vimageView;
        public View drawerDivider;
        public TextView vtext;

        private DrawerItems itemsList;
        private DrawerItems itemsSelectedList;

        public ArrayList<DrawerItems> drawerItemsArrayList = new ArrayList<DrawerItems>();

        public ViewHolder(View itemView) {
            super(itemView);
            vtextView = (TextView) itemView.findViewById(R.id.drawerItemText);
            vimageView = (ImageView) itemView.findViewById(R.id.drawerItemIcon);
            drawerDivider = (View) itemView.findViewById(R.id.drawerDivider);
            vtext = (TextView) itemView.findViewById(R.id.itemText);
            itemView.setOnClickListener(this);
        }

        public void bindDrawerItemArrayList(int i, DrawerItems draweritemsList, DrawerItems draweritemselectedList) {

            this.itemsList = draweritemsList;
            this.itemsSelectedList = draweritemselectedList;

            if (drawerListInstance.getDrawerItemListImagePositionInstances() != null) {
                positionOfItem = drawerListInstance.getDrawerItemListImagePositionInstances();
            }
            if (i <= 5) {
                vimageView.setImageResource(itemsList.getIcon());
                vtextView.setText(itemsList.getTittle());
                //vimageView.setEnabled(true);
//                if (i == 7) {
//                    drawerDivider.setVisibility(View.VISIBLE);
//                }
                if (positionOfItem == 0 && itemsList.getIcon() == R.drawable.home) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                    drawerDivider.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
                }
                else if (positionOfItem == 1 && itemsList.getIcon() == R.drawable.ic_favorite_grey_500_24dp) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                    drawerDivider.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
                }
                else if (positionOfItem == 2 && itemsList.getIcon() == R.drawable.ic_shopping_cart_grey_500_24dp) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                    drawerDivider.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
                }
                else if (positionOfItem == 3 && itemsList.getIcon() == R.drawable.order_completed) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                    drawerDivider.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
                }
                else if (positionOfItem == 4 && itemsList.getIcon() == R.drawable.ic_account_circle_grey_500_24dp) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                    drawerDivider.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
                }
                else if (positionOfItem == 5 && itemsList.getIcon() == R.drawable.logout) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));
                    drawerDivider.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
                }

            }
            else if (i > 7) {
                vtext.setText(itemsList.getTittle());
                vtextView.setTextColor(v.getResources().getColor(R.color.colorAccent));

            }
        }

        @Override
        public void onClick(View view) {
            positionOfItem = this.getAdapterPosition();
            if (this.getAdapterPosition() == 0) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(gotoformhome);
            }
            else if (this.getAdapterPosition() == 1) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), WishList.class);
                gotoformhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(gotoformhome);
            }
            else if (this.getAdapterPosition() == 2) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), Cart_List.class);
                gotoformhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(gotoformhome);
            }
            else if (this.getAdapterPosition() == 3) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), My_Orders.class);
                gotoformhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(gotoformhome);
            }
            else if (this.getAdapterPosition() == 4) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), Account.class);
                gotoformhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(gotoformhome);
            }

            else if (this.getAdapterPosition() == 5) {
                drawer.closeDrawers();
                Intent gotoformhome = new Intent(view.getContext(), My_Custom_Bag.class);
                gotoformhome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(gotoformhome);

//                drawer.closeDrawers();
//                sessionManager = new SessionManager(v.getContext());
//                sessionManager.logoutUser();
            }

            drawerListInstance.setDrawerItemListImagePositionInstances(positionOfItem);
            notifyDataSetChanged();
        }
    }
}
