package com.mobitechs.vbags.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mobitechs.vbags.Generated_Order_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.connectivity.Req_Generated_Order_Details;
import com.mobitechs.vbags.model.My_Order_ProductItems;

import java.util.ArrayList;
import java.util.List;

public class Generated_Order_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<My_Order_ProductItems> productListItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;


    String OrderStatusName = "";
    String OrderStatusId = "";
    private String[] OrderStatusArrayList;
    private Spinner_Filter_Adapter spinnerAdapter;
    private ProgressDialog OrderStatusDialogBox;
    private List<String> OrderStatusIdList = new ArrayList<String>();
    private List<String> OrderStatusNameList = new ArrayList<String>();
    private ProgressDialog progressDialog;


    public Generated_Order_List_Adapter(List<My_Order_ProductItems> items) {
        this.productListItems = items;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            Generated_Order_List_Adapter.EmptyViewHolder emptyViewHolder = new Generated_Order_List_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.generated_order_list_items, viewGroup, false);
        viewHolder = new Generated_Order_List_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Generated_Order_List_Adapter.ViewHolder) {
            Generated_Order_List_Adapter.ViewHolder vHolder = (Generated_Order_List_Adapter.ViewHolder) viewHolder;
            My_Order_ProductItems itemOflist = productListItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        if (productListItems.size() > 0) {
            return productListItems.size();
        } else {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        //        public TextView txtDelCharges;
        // public TextView txtTotalPrice;
        //  public TextView txtDeliveryStatus;
        public TextView txtStatus, txtTotalRs,txtEmail,txtMobileNo,txtAddress,txtName;
        public TextView txtDate;
        public TextView txtOrderId;
        Spinner OrderStatus;
        public View cardView;
        //        Button btnConfirm,btnCancel;
        String orderId, orderBulkId;
        RelativeLayout relLayoutStatus, relLayoutTotalRs;

        My_Order_ProductItems productListItems = new My_Order_ProductItems();


        public ViewHolder(View itemView) {
            super(itemView);

//            txtDelCharges = (TextView) itemView.findViewById(R.id.txtDelCharges);
//            txtTotalPrice = (TextView) itemView.findViewById(R.id.txtTotalPrice);
//            txtDeliveryStatus = (TextView) itemView.findViewById(R.id.txtDeliveryStatus);

            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtTotalRs = (TextView) itemView.findViewById(R.id.txtTotalRs);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtName = (TextView) itemView.findViewById(R.id.txtName);

            relLayoutStatus = (RelativeLayout) itemView.findViewById(R.id.relLayoutStatus);
            relLayoutTotalRs = (RelativeLayout) itemView.findViewById(R.id.relLayoutTotalRs);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtOrderId = (TextView) itemView.findViewById(R.id.txtOrderId);
            OrderStatus = (Spinner) itemView.findViewById(R.id.spinnerOrderStatus);
//            btnConfirm = (Button) itemView.findViewById(R.id.btnConfirm);
//            btnCancel = (Button) itemView.findViewById(R.id.btnCancel);

            OrderStatus.setVisibility(View.GONE);

            cardView = itemView;
            cardView.setOnClickListener(this);
//            btnConfirm.setOnClickListener(this);
//            btnCancel.setOnClickListener(this);
            relLayoutStatus.setOnClickListener(this);
//            relLayoutTotalRs.setOnClickListener(this);

        }

        public void bindListDetails(My_Order_ProductItems productListItems) {
            this.productListItems = productListItems;

            String status = productListItems.getdeleveryStatus();

//            if(status.equals("Pending") ){
//                txtDeliveryStatus.setTextColor(v.getResources().getColor(R.color.lowRating));
//            }
//            else if(status.equals("Delivered")){
//                txtDeliveryStatus.setTextColor(v.getResources().getColor(R.color.goodRating));
//            }

//            txtDelCharges.setText(productListItems.getdeliveryCharges());
//            txtTotalPrice.setText(productListItems.gettotalPrice());
//            txtDeliveryStatus.setText(productListItems.getdeleveryStatus());


            orderId = productListItems.getorderId();
            orderBulkId = productListItems.getorderBulkId();

            txtOrderId.setText(productListItems.getorderBulkId());
            txtDate.setText(productListItems.getordered_Date());
            txtEmail.setText(productListItems.getEmail());
            txtMobileNo.setText(productListItems.getmobileNo());
            txtAddress.setText(productListItems.getaddress());
            txtName.setText(productListItems.getusername());

//            btnConfirm.setText("Status: "+productListItems.getdeleveryStatus());
//            btnCancel.setText("Total Rs. : "+productListItems.gettotalPrice());
//
            txtStatus.setText("Status: " + productListItems.getdeleveryStatus());
            txtTotalRs.setText("Total Rs. : " + productListItems.gettotalPrice());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.relLayoutStatus) {
                Drawable icon = v.getResources().getDrawable(R.drawable.ic_launcher);

                new MaterialStyledDialog.Builder(v.getContext())
                        .setTitle("Confirmation!")
                        .setDescription("Do you really want to change order status?")
                        .setPositiveText("OK")
                        .setIcon(icon)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                OrderStatus.setVisibility(View.VISIBLE);
                                getOrderStatusList();
                            }
                        })
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
            }
//            else if (v.getId() == R.id.btnCancel) {
//
//            }
//           else if(v.getId() == R.id.btnAddReview){
//                Intent ratingReview = new Intent(v.getContext(), Add_Ratings_Reviews.class);
//                ratingReview.putExtra("productId", productListItems.getproductId());
//                v.getContext().startActivity(ratingReview);
//            }
            else if (this.productListItems != null) {
                Intent productDetails = new Intent(v.getContext(), Generated_Order_Details.class);

                productDetails.putExtra("OrderId", productListItems.getorderId());
                productDetails.putExtra("OrderBulkId", productListItems.getorderBulkId());
                productDetails.putExtra("orderStatusId", productListItems.getorderStatusId());
                productDetails.putExtra("ProductPrice", productListItems.getproductPrice());
                productDetails.putExtra("Qty", productListItems.getqty());
                productDetails.putExtra("DeliveryCharges", productListItems.getdeliveryCharges());
                productDetails.putExtra("TotalPrice", productListItems.gettotalPrice());
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

        private void getOrderStatusList() {
            txtStatus.setText("Select Status ");
//            OrderStatusArrayList = new String[]{
//                    "Order Status"
//            };

            //OrderStatusNameList = new ArrayList<>(Arrays.asList(OrderStatusArrayList));
            spinnerAdapter = new Spinner_Filter_Adapter(v.getContext(), R.layout.spinner_item, OrderStatusNameList);
            getListOfOrderStatus();
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            OrderStatus.setAdapter(spinnerAdapter);
//        if (orderStatusId != null) {
//            int spinnerPosition = spinnerAdapter.getPosition(orderStatusId);
//            OrderStatus.setSelection(spinnerPosition);
//
//        }
            OrderStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        OrderStatusName = parent.getItemAtPosition(position).toString();
                        OrderStatusId = OrderStatusIdList.get(position);
//                        btnConfirm.setText("Status: "+OrderStatusName);
                        txtStatus.setText("Status: "+OrderStatusName+OrderStatusId);
                        OrderStatus.setVisibility(View.GONE);
                        ChangeOrderStatus();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        private void getListOfOrderStatus() {
            Req_Generated_Order_Details statusSpinnerList = new Req_Generated_Order_Details(v.getContext());
            statusSpinnerList.FetchOrderStatus(OrderStatusNameList, OrderStatusIdList, spinnerAdapter);
        }

        private void ChangeOrderStatus() {
            progressDialog = new ProgressDialog(v.getContext());
            progressDialog.setMessage("Please Wait. Updating Order Status..");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            try {
                Req_Generated_Order_Details statusSpinnerList = new Req_Generated_Order_Details(v.getContext());
                statusSpinnerList.ChangeOrderStatus(OrderStatusId, orderId, orderBulkId, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
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