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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mobitechs.vbags.DeliveryAddressDetails;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.connectivity.Req_Cart_List;
import com.mobitechs.vbags.connectivity.Req_Wish_List;
import com.mobitechs.vbags.Cart_List;
import com.mobitechs.vbags.model.CartList_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.singleton.UserWishList_Instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Cart_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CartList_Items> listItems = new ArrayList<CartList_Items>();
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    int wishListCounte = 0;
    int freeDeliveryOn = 0;

    int prodcutCount = 1, payableAmount = 0, allProductPrice = 0, allproductDelCharges = 0, allproductTotalPriceWithDelivery = 0;
    TextView txtAllProductPrice, txtAllProductQty, txtAllProductDelCharges, txtAmountPayable;
    int previousQty ,selectedQty;
    UserWishList_Instance userProductWishList = new UserWishList_Instance();

    ArrayList<String> cartIdArray = new ArrayList<String>();
    ArrayList<String> productIdArray = new ArrayList<String>();
    ArrayList<String> productNameArray = new ArrayList<String>();
    ArrayList<String> productImageArray = new ArrayList<String>();
    ArrayList<String> productPriceArray = new ArrayList<String>();
    ArrayList<String> productPriceWithQtyArray = new ArrayList<String>();
    ArrayList<String> productQtyArray = new ArrayList<String>();
    ArrayList<String> productDeliveryChargesArray = new ArrayList<String>();

    Spinner spinnerQty;
    String[]qtyArray = {"1", "2","3", "4","5","6","7","8","9","10","11", "12","13", "14","15","16","17","18","19","20"};

    SessionManager sessionManager;
    Button btnContinue;
    Activity mActivity;

    int cartBadge;

    public Cart_List_Adapter(Cart_List mActivity,List<CartList_Items> items, TextView txtAllProductPriceC, TextView txtAllProductQtyC, TextView txtAllProductDelChargesC, TextView txtAmountPayableC, Button btnContinueC, int freeDeliveryOnC) {
        this.mActivity = mActivity;
        this.listItems = items;
        this.txtAllProductPrice = txtAllProductPriceC;
        this.txtAllProductQty = txtAllProductQtyC;
        this.txtAllProductDelCharges = txtAllProductDelChargesC;
        this.txtAmountPayable = txtAmountPayableC;
        this.btnContinue = btnContinueC;
        this.freeDeliveryOn = freeDeliveryOnC;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Cart_List_Adapter.EmptyViewHolder emptyViewHolder = new Cart_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_list_items, viewGroup, false);
        viewHolder = new Cart_List_Adapter.ViewHolder(v);
        sessionManager = new SessionManager(v.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Cart_List_Adapter.ViewHolder) {
            Cart_List_Adapter.ViewHolder vHolder = (Cart_List_Adapter.ViewHolder) viewHolder;
            CartList_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);

            productIdArray.add(itemOflist.getproductId());
            productPriceArray.add(itemOflist.getproductPrice());
            productQtyArray.add(itemOflist.getqty());
            productDeliveryChargesArray.add(itemOflist.getdeliveryCharges());
            productPriceWithQtyArray.add(itemOflist.gettotalPrice());
            productNameArray.add(itemOflist.getproductName());
            productImageArray.add(itemOflist.getFirstImagePath());


            int listSize = listItems.size();
            prodcutCount = position + 1;

            int productPrice = Integer.parseInt(itemOflist.getproductPrice());
            int qty = Integer.parseInt(itemOflist.getqty());

            int delCharge = Integer.parseInt(itemOflist.getdeliveryCharges());
            int alltotal = (qty * productPrice) + delCharge;

            allProductPrice = allProductPrice + (qty * productPrice);
            allproductDelCharges = allproductDelCharges + delCharge;
            allproductTotalPriceWithDelivery = allproductTotalPriceWithDelivery + alltotal;
            payableAmount = allproductTotalPriceWithDelivery;
            Cart_List.updatePriceDetails(prodcutCount,allProductPrice,allproductDelCharges);
        //    UpdatePrice(itemOflist);
        }
    }


    public void cartCounterUpdater(){
        String counter = sessionManager.getCartBadge();
        cartBadge = Integer.parseInt(counter);
        sessionManager.setCartBadge(String.valueOf(cartBadge-1));
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public int getItemCount() {

        int itemCount = listItems.size();
        String count = String.valueOf(itemCount);
        sessionManager = new SessionManager(mActivity);
        sessionManager.setCartBadge(count);
        mActivity.invalidateOptionsMenu();

        if (listItems.size() > 0) {


            return listItems.size();
        } else {
            return 1;
        }

//        return cartListItems.size();
    }

    public int getItemViewType(int position) {
        if (listItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener {

        public TextView txtProductName;
        public TextView txtCategory;
        public TextView txtColor;
        public TextView txtSize;
        public ImageView productImage;
        public Spinner spinnerQty;
        public TextView txtProductPrice;
        //public TextView txtProductQty;
        public TextView txtTotalPrice;
        public TextView txtDeliveryCharges;
        public TextView txtMoveToWishList;
        public TextView txtRemoveFromCart;

        public LinearLayout moveToWishListBtnLayout;
        public LinearLayout removeFromCartBtnLayout;

        public View cardView;
        public ArrayList<String> productWishListArray;
        public String productWishListId;
        String userId, productId,qty;
        CoordinatorLayout coordinatorLayout;

        CartList_Items cartListItems = new CartList_Items();
        private ProgressBar imageLoader;

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            userId = user.get(SessionManager.KEY_USERID);

            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            txtColor = (TextView) itemView.findViewById(R.id.txtColor);
            txtSize = (TextView) itemView.findViewById(R.id.txtSize);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txtTotalPrice);
           // txtProductQty = (TextView) itemView.findViewById(R.id.txtProductQty);
            txtDeliveryCharges = (TextView) itemView.findViewById(R.id.txtDelCharges);
            txtRemoveFromCart = (TextView) itemView.findViewById(R.id.txtRemoveFromCart);
            txtMoveToWishList = (TextView) itemView.findViewById(R.id.txtMoveToWishList);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);

            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);
            coordinatorLayout = (CoordinatorLayout) itemView.findViewById(R.id.coordinatorLayout);
            removeFromCartBtnLayout = (LinearLayout) itemView.findViewById(R.id.removeFromCartBtnLayout);
            moveToWishListBtnLayout = (LinearLayout) itemView.findViewById(R.id.moveToWishListBtnLayout);
            moveToWishListBtnLayout = (LinearLayout) itemView.findViewById(R.id.moveToWishListBtnLayout);
            cardView = itemView;
            cardView.setOnClickListener(this);
            btnContinue.setOnClickListener(this);
            removeFromCartBtnLayout.setOnClickListener(this);
            moveToWishListBtnLayout.setOnClickListener(this);

            spinnerQty = (Spinner) v.findViewById(R.id.spinnerQty);
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(v.getContext(),android.
                    R.layout.simple_spinner_dropdown_item ,qtyArray);
            spinnerQty.setAdapter(adapter);

            spinnerQty.setOnItemSelectedListener(this);

        }

        public void bindListDetails(CartList_Items cartListItems) {
            this.cartListItems = cartListItems;

            String cartId = cartListItems.getcartId();
            cartIdArray.add(cartId);

            productWishListArray = userProductWishList.getWishList();
            productWishListId = cartListItems.getproductId();

            if (productWishListArray.contains(productWishListId)) {
                txtMoveToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_red_400_24dp, 0, 0, 0);
                txtMoveToWishList.setText("Delete from wishlist");
            } else {
                txtMoveToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_grey_500_24dp, 0, 0, 0);
            }

            int productTotalPrice = Integer.parseInt(cartListItems.getproductPrice()) * Integer.parseInt(cartListItems.getqty()) + Integer.parseInt(cartListItems.getdeliveryCharges());
            txtProductName.setText(cartListItems.getproductName());
            txtProductPrice.setText(cartListItems.getproductPrice()+"");
            txtTotalPrice.setText(productTotalPrice+"");
            txtDeliveryCharges.setText("+"+cartListItems.getdeliveryCharges());
            //txtProductQty.setText("x"+cartListItems.getqty());

            txtCategory.setText("Category: " + cartListItems.getcategoryName());
            txtColor.setText("Colour: " + cartListItems.getcolor());
            txtSize.setText("Size: " + cartListItems.getsize());

            qty = cartListItems.getqty();
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(v.getContext(),android.
                    R.layout.simple_spinner_dropdown_item ,qtyArray);
            spinnerQty.setAdapter(adapter);
            if (qty != null) {
                int spinnerPosition = adapter.getPosition(qty);
                spinnerQty.setSelection(spinnerPosition);

            }

            String color = cartListItems.getcolor();
            String size = cartListItems.getsize();
            String delCharges = cartListItems.getdeliveryCharges();

            if (color.equals("") || color == null) {
                txtColor.setVisibility(View.GONE);
            }

            if (size.equals("") || size == null) {
                txtSize.setVisibility(View.GONE);
            }

            if (delCharges.equals("") || delCharges == null) {
                txtDeliveryCharges.setText("Free delivery");
            }

            String imagepath = cartListItems.getFirstImagePath();
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.moveToWishListBtnLayout) {

                productId = cartListItems.getproductId();

                if (Arrays.asList(productWishListArray).contains(productId)) {
                    // true
                    wishListCounte = 1;
                } else {
                    wishListCounte = 0;
                }

                if (wishListCounte == 0) {
                    wishListCounte = 1;
                    txtMoveToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_red_400_24dp, 0, 0, 0);
                    try {
                        Req_Wish_List reqWishList_ = new Req_Wish_List(v.getContext());
                        reqWishList_.addToWishList(userId, productId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // also remove from cart List
                    removeFromCart(getPosition());
                    cartCounterUpdater();
                    String cartId = cartListItems.getcartId();
                    try {
                        Req_Cart_List req_CartList = new Req_Cart_List(v.getContext());
                        req_CartList.removeFromCartList(userId, productId,cartId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    wishListCounte = 0;
                    txtMoveToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_grey_500_24dp, 0, 0, 0);
                    try {
                        int pos = productWishListArray.indexOf(productId);
                        productWishListArray.remove(pos);
                        Req_Wish_List reqWishList = new Req_Wish_List(v.getContext());
                        reqWishList.deleteWishList(userId, productId);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            else if (v.getId() == R.id.removeFromCartBtnLayout) {
                productId = cartListItems.getproductId();
                String cartId = cartListItems.getcartId();
                //delete from cart
                try {
                    cartCounterUpdater();

                    Req_Cart_List req_CartList = new Req_Cart_List(v.getContext());
                    req_CartList.removeFromCartList(userId, productId,cartId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                removeFromCart(getPosition());
            }
            else if(v.getId() == R.id.btnContinue){
//                CartList_Items cartList_items = listItems.get(0);
//                String product = cartList_items.getproductName();

                if (allProductPrice > freeDeliveryOn) {
                    payableAmount = allProductPrice;
                }
                else{
                    payableAmount = allProductPrice + allproductDelCharges;
                }

                userProductWishList.setCartList(listItems);
                String payableAmt = String.valueOf(payableAmount);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity);
                Intent gotoAddress = new Intent(v.getContext(),DeliveryAddressDetails.class);
                gotoAddress.putStringArrayListExtra("cartIdArray",cartIdArray);
                gotoAddress.putStringArrayListExtra("productIdArray",productIdArray);
                gotoAddress.putStringArrayListExtra("productNameArray",productNameArray);
                gotoAddress.putStringArrayListExtra("productImageArray",productImageArray);
                gotoAddress.putStringArrayListExtra("productPriceArray",productPriceArray);
                gotoAddress.putStringArrayListExtra("productQtyArray",productQtyArray);
                gotoAddress.putStringArrayListExtra("productDeliveryChargesArray",productDeliveryChargesArray);
                gotoAddress.putStringArrayListExtra("productPriceWithQtyArray",productPriceWithQtyArray);
                gotoAddress.putExtra("totalPayableAmount",payableAmt);
//                v.getContext().startActivity(gotoAddress);
                v.getContext().startActivity(gotoAddress, options.toBundle());
            }

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int pos = getAdapterPosition();
            CartList_Items cartItem = listItems.get(pos);
            int sid = spinnerQty.getSelectedItemPosition();
            selectedQty = Integer.parseInt(qtyArray[sid]);
            previousQty  = Integer.parseInt(cartItem.getqty());
            int productPriceWithQty = Integer.parseInt(cartItem.getproductPrice()) * selectedQty;
            int productPriceWithQtyNDelCharge = Integer.parseInt(cartItem.getproductPrice()) * selectedQty + Integer.parseInt(cartItem.getdeliveryCharges());
            txtTotalPrice.setText(""+productPriceWithQtyNDelCharge);
            payableAmount = productPriceWithQtyNDelCharge;

            cartItem.setqty(String.valueOf(selectedQty));

            if(qty.equals(String.valueOf(selectedQty))){
                String abe = "Nothing Changed.";
            }
            else {
                //4 > 2
                if(selectedQty > previousQty){
                    int newQty = selectedQty - previousQty;
                    int productPriceQty = Integer.parseInt(cartItem.getproductPrice()) * newQty;
                    allProductPrice = allProductPrice + productPriceQty;
                }
                //2 > 4
                else if(selectedQty < previousQty){
                    int newQty = previousQty - selectedQty;
                    int productPriceQty = Integer.parseInt(cartItem.getproductPrice()) * newQty;
                    allProductPrice = allProductPrice - productPriceQty;
                }
                // update all price
                Cart_List.updatePriceDetails(prodcutCount,allProductPrice,allproductDelCharges);
                try {
                    productQtyArray.set(pos, String.valueOf(selectedQty));
                    productPriceWithQtyArray.set(pos, String.valueOf(productPriceWithQty));
                    productId = cartItem.getproductId();
                    userId = cartItem.getuserId();
                    String cartId = cartItem.getcartId();

                    Req_Cart_List req_CartList = new Req_Cart_List(v.getContext());
                    req_CartList.updateCartList(userId,cartId, productId,selectedQty,productPriceWithQtyNDelCharge);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void removeFromCart(int position) {

        prodcutCount = 1;
        payableAmount = 0; allProductPrice = 0; allproductDelCharges =0 ;
        allproductTotalPriceWithDelivery = 0;
        listItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listItems.size());
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
