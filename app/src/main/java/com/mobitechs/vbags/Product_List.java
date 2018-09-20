package com.mobitechs.vbags;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Product_List_adapter;
import com.mobitechs.vbags.connectivity.Req_Product_List;
import com.mobitechs.vbags.model.Product_ListItems;
import com.mobitechs.vbags.scrollListeners.ProductListScrollListener;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Product_List extends BaseActivity implements View.OnClickListener{

    Product_List mActivity;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    public List<Product_ListItems> listItems = new ArrayList<Product_ListItems>();
    private ProgressDialog progressDialog;
    private Product_List_adapter listAdapter;
    String categoryId;
    int currentPage = 1;
    String requestFor = "categoryWiseProduct";
    String userId = "";
    int requestState;

    private boolean ascending;
    String text = "";
    int gridCount = 2;

    LinearLayout sortLayout, filterLayout;
    ImageView gridToListImg;

    private View filterPanel, sortPanel;
    Animation bottomUp, bottomDown;

    FrameLayout main_frame;
    int filterVisibility = 0, sortVisibility = 0;

    RadioButton rdNewest, rdHighLow, rdLowHigh;
    RadioGroup rdGroupSort;

    Spinner spinnerCategory;

    int minimumVal, maximumVal;

    final String[] a = new String[1];
    final String[] b = new String[1];
    Button btnPriceSubmit;
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERID);

        if (userId == null) {
            userId = "";
        }
        setContentView(R.layout.product_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        filterPanel = (ViewGroup) findViewById(R.id.filterBottomLayout);
        btnPriceSubmit = (Button) findViewById(R.id.btnPriceSubmit);
        btnPriceSubmit.setOnClickListener(this);
        btnPriceSubmit.setVisibility(View.GONE);
        sortPanel = (ViewGroup) findViewById(R.id.sortBottomLayout);
        bottomUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation);
        bottomDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        rdGroupSort = (RadioGroup) findViewById(R.id.rdGroupSort);



        rdGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                String radioName = String.valueOf(checkedRadioButton.getText());

                if (radioName.equals("Newest First")) {
                    Collections.shuffle(listItems);
                    listAdapter = new Product_List_adapter(Product_List.this, listItems,gridCount);
                    recyclerView.setAdapter(listAdapter);
                    sortVisibility = 1;
                    SortLayoutManage();
                }
                else if (radioName.equals("Price Low to High")) {
                    sortData(ascending);
                }
                else if (radioName.equals("Price High to Low")) {
                    sortDataDescending();
                }
            }
        });

        gridToListImg = (ImageView) findViewById(R.id.gridToListImg);
        sortLayout = (LinearLayout) findViewById(R.id.sortLayout);
        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);

        sortLayout.setOnClickListener(this);
        filterLayout.setOnClickListener(this);
        gridToListImg.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.productListRecyclerView);
        recyclerView.setHasFixedSize(true);

        setUpAdapter();
        setPriceRangeSeekbar();

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        requestState = 0;
        getProductList(currentPage);

        recyclerView.addOnScrollListener(new ProductListScrollListener(gridLayoutManager, currentPage) {

            @Override
            public void onLoadMore(int currentPage) {
                getProductList(currentPage);
            }
        });
        recyclerView.smoothScrollToPosition(0);

        getCategoryDetails();
    }

    private void getCategoryDetails() {

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNameList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoryName = parent.getItemAtPosition(position).toString();
                    categoryId = categorydList.get(position);

                    filterVisibility = 1;
                    FilterLayoutManage();

//                    Intent gotoProductList = new Intent(Product_List.this,Product_List.class);
//                    gotoProductList.putExtra("categoryId",categoryId);
//                    startActivity(gotoProductList);
                    currentPage = 1;
                    getProductList(currentPage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setUpAdapter() {
        gridLayoutManager = new GridLayoutManager(this, gridCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.smoothScrollToPosition(0);
        listAdapter = new Product_List_adapter(this,listItems,gridCount);
        recyclerView.setAdapter(listAdapter);
    }

    private void getProductList(int currentPage) {
        try {
            Req_Product_List get_ProductList = new Req_Product_List(this);
            get_ProductList.getProductList(progressBar,listItems, recyclerView, listAdapter, currentPage, categoryId, requestFor, userId, requestState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.sortLayout) {
            SortLayoutManage();
        } else if (v.getId() == R.id.filterLayout) {
            FilterLayoutManage();

        }else if (v.getId() == R.id.btnPriceSubmit) {
            getPriceFilterList();

        } else if (v.getId() == R.id.gridToListImg) {
            if (gridCount == 2) {
                gridToListImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_black_24dp));
                setUpAdapter();
                gridCount = 1;
            } else {
                gridToListImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid));
                setUpAdapter();
                gridCount = 2;
            }
        }
    }

    private void FilterLayoutManage() {
        if (filterVisibility == 0) {
            sortPanel.setVisibility(View.INVISIBLE);
            sortPanel.setVisibility(View.GONE);
            filterPanel.startAnimation(bottomUp);
            filterPanel.setVisibility(View.VISIBLE);
            filterVisibility = 1;
            sortVisibility = 0;
        } else {
            filterPanel.startAnimation(bottomDown);
            filterPanel.setVisibility(View.INVISIBLE);
            filterVisibility = 0;
        }
    }

    private void SortLayoutManage() {
        if (sortVisibility == 0) {
            filterPanel.setVisibility(View.INVISIBLE);
            filterPanel.setVisibility(View.GONE);
            sortPanel.startAnimation(bottomUp);
            sortPanel.setVisibility(View.VISIBLE);
            sortVisibility = 1;
            filterVisibility = 0;
        } else {
            sortPanel.startAnimation(bottomDown);
            sortPanel.setVisibility(View.INVISIBLE);
            sortVisibility = 0;
        }
    }

    private void setPriceRangeSeekbar() {

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar5);

        // get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.textMin5);
        final TextView tvMax = (TextView) findViewById(R.id.textMax5);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText("Rs." + String.valueOf(minValue));
                tvMax.setText("Rs." + String.valueOf(maxValue));

                btnPriceSubmit.setVisibility(View.VISIBLE);
                a[0] = String.valueOf(minValue);
                b[0] = String.valueOf(maxValue);

                minimumVal = Integer.parseInt(a[0]);
                maximumVal = Integer.parseInt(b[0]);
            }
        });
        // call wb service to get data from min and max value
    }

    private void getPriceFilterList() {
        try {
            requestState = 0;
            Req_Product_List req_ProductList = new Req_Product_List(this);
            req_ProductList.getPriceFilterProductList(listItems, recyclerView, listAdapter,requestState, currentPage, minimumVal, maximumVal, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortDataDescending() {

        sortVisibility = 1;
        SortLayoutManage();
        Collections.sort(listItems, new Comparator<Product_ListItems>() {
            @Override
            public int compare(Product_ListItems o1, Product_ListItems o2) {
                return o1.productPrice.compareTo(o2.productPrice);
            }
        });
        Collections.reverse(listItems);
        listAdapter = new Product_List_adapter(this, listItems,gridCount);
        recyclerView.setAdapter(listAdapter);
    }

    private void sortData(boolean asc)
    {
        sortVisibility = 1;
        SortLayoutManage();
        Collections.sort(listItems, new Comparator<Product_ListItems>() {
            @Override
            public int compare(Product_ListItems o1, Product_ListItems o2) {
                return o1.productPrice.compareTo(o2.productPrice);
            }
        });

        //ADAPTER
        listAdapter = new Product_List_adapter(this, listItems,gridCount);
        recyclerView.setAdapter(listAdapter);

    }
}
