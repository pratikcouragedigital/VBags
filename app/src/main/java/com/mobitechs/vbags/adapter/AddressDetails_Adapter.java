package com.mobitechs.vbags.adapter;

import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.R;
import com.mobitechs.vbags.connectivity.Req_Address_Details;
import com.mobitechs.vbags.model.AddressDetails_Items;
import com.mobitechs.vbags.sessionManager.SessionManager;
import com.mobitechs.vbags.singleton.Address_Instance;

import java.util.HashMap;
import java.util.List;

public class AddressDetails_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AddressDetails_Items> addressDetailsItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;
    private int lastSelectedPosition = -1;
    Button btnDeliverHere;
    View dialogView;
    SessionManager sessionManager;
    String userId = "",userEmail,userMobile,userName;
    public EditText txtAddressLine1, txtAddressLine2, txtCity, txtState, txtCountry, txtPincode;
    public Button btnUpdateAddress;
    //RelativeLayout formPart1RelativeLayout;
    Button btnAddAddress;
    String addressId = "";
    String fullAddress,addressLine1 = "", addressLine2 = "", city = "", state = "", pincode = "", country = "";


    public AddressDetails_Adapter(List<AddressDetails_Items> items, Button btnAddDeliverHere, Button btnAddAddress) {
        this.addressDetailsItems = items;
        this.btnDeliverHere = btnAddDeliverHere;
        this.btnAddAddress = btnAddAddress;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            AddressDetails_Adapter.EmptyViewHolder emptyViewHolder = new AddressDetails_Adapter.EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_address_details_items, viewGroup, false);
        viewHolder = new AddressDetails_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AddressDetails_Adapter.ViewHolder) {
            AddressDetails_Adapter.ViewHolder vHolder = (AddressDetails_Adapter.ViewHolder) viewHolder;
            AddressDetails_Items itemOflist = addressDetailsItems.get(position);
            vHolder.rdoAddress.setChecked(lastSelectedPosition == position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
        if (addressDetailsItems.size() > 0) {
            return addressDetailsItems.size();
        } else {
            return 1;
        }
//        return addressDetailsItems.size();
    }

    public int getItemViewType(int position) {
        if (addressDetailsItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView txtUserName;
        public TextView txtAddress;
        public RadioButton rdoAddress;

        public View cardView;
        public ImageButton btnEdit;

        AddressDetails_Items addressDetailsItems = new AddressDetails_Items();

        public ViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            rdoAddress = (RadioButton) itemView.findViewById(R.id.rdoAddress);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);

            cardView = itemView;
            cardView.setOnClickListener(this);
            btnEdit.setOnClickListener(this);

            rdoAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    addressId = addressDetailsItems.getAddressId();
                    fullAddress =  addressDetailsItems.getAddressLine1()+" "+addressDetailsItems.getAddressLine2()+" "+addressDetailsItems.getCity()+" "+addressDetailsItems.getState()+" "+addressDetailsItems.getPincode()+" "+addressDetailsItems.getCountry();
                    // Toast.makeText(v.getContext(),"selected Address is " + addressId,Toast.LENGTH_LONG).show();
                    //create instace and set address here
                    btnDeliverHere.setVisibility(View.VISIBLE);
                    btnAddAddress.setVisibility(View.GONE);
                    Address_Instance address_Instance = new Address_Instance();
                    address_Instance.setAddressId(addressId);
                    address_Instance.setfullAddress(fullAddress);

                }
            });
        }

        public void bindListDetails(AddressDetails_Items addressDetailsItems) {
            this.addressDetailsItems = addressDetailsItems;
            txtUserName.setText(addressDetailsItems.getUsername());

            addressLine1 = addressDetailsItems.getAddressLine1();
            addressLine2 = addressDetailsItems.getAddressLine2();
            city = addressDetailsItems.getCity();
            state = addressDetailsItems.getState();
            pincode = addressDetailsItems.getPincode();
            country = addressDetailsItems.getCountry();

            if(addressLine2 == null){
                addressLine2 = "";
            }

            String address = addressLine1 + " " + addressLine2 + " " + city + " " + state + " " + country + " " + pincode;
            txtAddress.setText(address);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnEdit) {

                //call address layout to update it
                addressLine1 = addressDetailsItems.getAddressLine1();
                addressLine2 = addressDetailsItems.getAddressLine2();
                city = addressDetailsItems.getCity();
                state = addressDetailsItems.getState();
                pincode = addressDetailsItems.getPincode();
                country = addressDetailsItems.getCountry();

                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.address_update_dialog, null);
                getLayoutDetails(addressLine1, addressLine2, city, state, pincode, country);
                alert.setView(dialogView);
                alert.show();
            }
//            else if (v.getId() == R.id.btnDeliverHere){
//                if(addressId.equals("") || addressId == null){
//                    Toast.makeText(v.getContext(), "Please select address", Toast.LENGTH_SHORT).show();
//                }
//                else{
//
//                    Req_Generate_Order genarateOrder = new Req_Generate_Order(v.getContext());
//                    genarateOrder.GenarateOrder(userId,userName,userMobile,userEmail,fullAddress,addressId,productIdArray,productNameArray,productPriceArray,productQtyArray,productDeliveryChargesArray,progressDialog);
//
//                    Toast.makeText(v.getContext(), "Selected Address Id : "+ addressId, Toast.LENGTH_SHORT).show();
//
//                }
//            }
        }
    }

    private void getLayoutDetails(String addressLine1, String addressLine2, String city, String state, String pincode, String country) {

        txtAddressLine1 = (EditText) dialogView.findViewById(R.id.txtAddressLine1);
        txtAddressLine2 = (EditText) dialogView.findViewById(R.id.txtAddressLine2);
        txtCity = (EditText) dialogView.findViewById(R.id.txtCity);
        txtState = (EditText) dialogView.findViewById(R.id.txtState);
        txtCountry = (EditText) dialogView.findViewById(R.id.txtCountry);
        txtPincode = (EditText) dialogView.findViewById(R.id.txtPincode);

        btnUpdateAddress = (Button) dialogView.findViewById(R.id.btnUpdateAddress);
        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAddress();
            }
        });

        Toast.makeText(v.getContext(), "id" + addressId, Toast.LENGTH_SHORT).show();
        txtAddressLine1.setText(addressLine1);
        txtAddressLine2.setText(addressLine2);
        txtCity.setText(city);
        txtState.setText(state);
        txtCountry.setText(country);
        txtPincode.setText(pincode);

    }

    private void UpdateAddress() {
        sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        addressLine1 = txtAddressLine1.getText().toString();
        addressLine2 = txtAddressLine1.getText().toString();
        city = txtCity.getText().toString();
        state = txtState.getText().toString();
        pincode = txtPincode.getText().toString();
        country = txtCountry.getText().toString();

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Req_Address_Details req_AddressDetails = new Req_Address_Details(v.getContext());
        req_AddressDetails.Update_Address(addressId, userId, addressLine1, addressLine2, city, state, pincode, country, progressDialog);
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Address is not available please add address.");
        }
    }
}