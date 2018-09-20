package com.mobitechs.vbags;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.adapter.Product_List_adapter;
import com.mobitechs.vbags.connectivity.Req_Product_List;
import com.mobitechs.vbags.model.Product_ListItems;
import com.mobitechs.vbags.scrollListeners.ProductListScrollListener;
import com.mobitechs.vbags.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SessionManager sessionManager;

    private ViewPager mPager;
    private int currentPage = 1;

    private ArrayList<Integer> sliderImagesArray = new ArrayList<Integer>();

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    public List<Product_ListItems> listItems = new ArrayList<Product_ListItems>();
    private ProgressDialog progressDialog;
    private Product_List_adapter listAdapter;
    String categoryId = "";
    String requestFor = "allProduct";
    String userId = "", userType;
    int requestState;
    FloatingActionButton btnFab;

    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    private boolean ascending;
    String isLowToHigh;

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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);
        userType = user.get(sessionManager.KEY_USERTYPE);

        main_frame = (FrameLayout) findViewById(R.id.main_frame);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        filterPanel = (ViewGroup) findViewById(R.id.filterBottomLayout);
        btnPriceSubmit = (Button) findViewById(R.id.btnPriceSubmit);
        btnPriceSubmit.setOnClickListener(this);
        btnPriceSubmit.setVisibility(View.GONE);
        sortPanel = (ViewGroup) findViewById(R.id.sortBottomLayout);
        bottomUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation);
        bottomDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        Hash_file_maps = new HashMap<String, String>();
        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        rdGroupSort = (RadioGroup) findViewById(R.id.rdGroupSort);

        rdGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                String radioName = String.valueOf(checkedRadioButton.getText());

                if (radioName.equals("Newest First")) {
                    Collections.shuffle(listItems);
                    listAdapter = new Product_List_adapter(MainActivity.this, listItems, gridCount);
                    recyclerView.setAdapter(listAdapter);
                    sortVisibility = 1;
                    SortLayoutManage();
                } else if (radioName.equals("Price Low to High")) {

                    sortData(ascending);
                } else if (radioName.equals("Price High to Low")) {

                    sortDataDescending();

                }
            }
        });

        gridToListImg = (ImageView) findViewById(R.id.gridToListImg);
        sortLayout = (LinearLayout) findViewById(R.id.sortLayout);
        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);

        recyclerView = (RecyclerView) findViewById(R.id.newProductLayout);
        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        btnFab.setOnClickListener(this);

        sortLayout.setOnClickListener(this);
        filterLayout.setOnClickListener(this);
        gridToListImg.setOnClickListener(this);
        main_frame.setOnTouchListener(this);

        recyclerView.setHasFixedSize(true);

        setUpAdapter();

        requestState = 0;
        CreateSlider();
        getProductList(currentPage);

        recyclerView.addOnScrollListener(new ProductListScrollListener(gridLayoutManager, currentPage) {

            @Override
            public void onLoadMore(int currentPage) {
                getProductList(currentPage);
            }
        });
        recyclerView.smoothScrollToPosition(0);

//        sortText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // TODO Auto-generated method stub
//
//                text = String.valueOf(s);
//                Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
//                listAdapter.getFilter().filter(text);
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                // TODO Auto-generated method stub
//            }
//        });
        getCategoryDetails();
        setPriceRangeSeekbar();
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

                    Intent gotoProductList = new Intent(MainActivity.this, Product_List.class);
                    gotoProductList.putExtra("categoryId", categoryId);
                    startActivity(gotoProductList);

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
        listAdapter = new Product_List_adapter(this, listItems, gridCount);
        recyclerView.setAdapter(listAdapter);
    }


    private void getProductList(int currentPage) {
        try {
            Req_Product_List req_ProductList = new Req_Product_List(this);
            req_ProductList.getProductList(progressBar,listItems, recyclerView, listAdapter, currentPage, categoryId, requestFor, userId, requestState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CreateSlider() {
        getTopProductList();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFab) {

            if (userType.equals("Admin")) {
                Intent gotoAddProduct = new Intent(MainActivity.this, Admin_Rights_Add.class);
                startActivity(gotoAddProduct);
            } else {
                Intent gotoAddProduct = new Intent(MainActivity.this, My_Custom_Bag.class);
                startActivity(gotoAddProduct);
            }
        } else if (v.getId() == R.id.sortLayout) {

            SortLayoutManage();
        } else if (v.getId() == R.id.filterLayout) {

            FilterLayoutManage();

        }else if (v.getId() == R.id.btnPriceSubmit) {

            getPriceFilterList();
            filterVisibility = 1;
            FilterLayoutManage();

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


    public void getTopProductList() {

        String webMethod = "TopProductList";
        final String params = "?method=" + webMethod;
        String url = getString(R.string.url) + params;
        final String ResponseName = "productListResponse";

        AndroidNetworking.get(url)
                .setTag("productList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray(ResponseName);

                            if (arr.length() == 0) {
//                                Toast.makeText(context, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
//                                    Product_ListItems product_ListItems = new Product_ListItems();
//                                    product_ListItems.setProductId(obj.getString("productId"));
//                                    product_ListItems.setProductName(obj.getString("productName"));
//                                    product_ListItems.setProductPrice(obj.getString("productPrice"));
                                    if (!obj.getString("img1").isEmpty() && obj.getString("img1") != null) {
//                                        product_ListItems.setFirstImagePath(obj.getString("img1"));
                                        Hash_file_maps.put(obj.getString("productName") + ":" + obj.getString("productPrice") + ":" + obj.getString("productId") + ":" + obj.getString("deliveryCharges"), URL_WebService.getImageUrl() + obj.getString("img1"));
                                    }
                                }
                                SliderRun();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }

    private void SliderRun() {
        for (String name : Hash_file_maps.keySet()) {
            String details = "";
            String detailsArray[];
            final String pImage = Hash_file_maps.get(name);

            details = name;
            detailsArray = details.split(":");
            final String pName = detailsArray[0];
            final String pPrice = detailsArray[1];
            final String pId = detailsArray[2];
            final String pDeliveryCharge = detailsArray[3];

            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            textSliderView
                    .description(pName + " Rs." + pPrice)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                            Intent gotoProductDetails = new Intent(MainActivity.this, Product_List_Details.class);
                            gotoProductDetails.putExtra("ProductId", pId);
                            gotoProductDetails.putExtra("ProductName", pName);
                            gotoProductDetails.putExtra("ProductPrice", pPrice);
                            gotoProductDetails.putExtra("Image", pImage);
                            gotoProductDetails.putExtra("ProductImage", pImage);
                            gotoProductDetails.putExtra("DeliveryCharges", pDeliveryCharge);
                            gotoProductDetails.putExtra("ImFrom", "Slider");
                            //startActivity(gotoProductDetails);
                            startActivity(gotoProductDetails, options.toBundle());
                            // Toast.makeText(MainActivity.this, "Id: " + pId, Toast.LENGTH_SHORT).show();

                        }
                    });
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(MainActivity.this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

//                getCompareRangeData();
            }
        });
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

        listAdapter = new Product_List_adapter(this, listItems, gridCount);
        recyclerView.setAdapter(listAdapter);
    }

    private void sortData(boolean asc) {
        sortVisibility = 1;
        SortLayoutManage();
        Collections.sort(listItems, new Comparator<Product_ListItems>() {
            @Override
            public int compare(Product_ListItems o1, Product_ListItems o2) {
                return o1.productPrice.compareTo(o2.productPrice);
            }
        });

        //ADAPTER
        listAdapter = new Product_List_adapter(this, listItems, gridCount);
        recyclerView.setAdapter(listAdapter);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.main_frame) {

            if (filterVisibility == 1) {
                filterPanel.startAnimation(bottomDown);
                filterPanel.setVisibility(View.GONE);
            }
            if (sortVisibility == 1) {
                sortPanel.startAnimation(bottomDown);
                sortPanel.setVisibility(View.GONE);
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }
}