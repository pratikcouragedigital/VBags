package com.mobitechs.vbags.Drawer;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.BadgeDrawable;
import com.mobitechs.vbags.Cart_List;
import com.mobitechs.vbags.Drawer.DrawerAdapter;
import com.mobitechs.vbags.Drawer.DrawerItems;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.Notification;
import com.mobitechs.vbags.Product_List;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    SessionManager sessionManager;
    TextView lblName;
    //TextView lbluserId;
    RecyclerView listItems;
    DrawerLayout drawer;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    DrawerAdapter drawerAdapter;
    String userId,userName,userEmail,userMobile;

    public ArrayAdapter<String> adapter;
    ListView lv;
    ArrayList<String> bagTypeList = new ArrayList<>();
    public List<String> categorydList = new ArrayList<String>();
    public List<String> categoryNameList = new ArrayList<String>();
    public String categoryName;
    public String categoryId="";
    String selectedItem ="";

    public ArrayList<DrawerItems> itemArrayList;
    public ArrayList<DrawerItems> itemSelectedArrayListForNgo;

    public ArrayList<DrawerItems> itemArrayListForNgo;
    public ArrayList<DrawerItems> itemSelectedArrayList;
    private String cartBadge;

    @Override
    public void setContentView(int layoutResID) {
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        frameLayout = (FrameLayout) drawer.findViewById(R.id.contentFrame);
        linearLayout = (LinearLayout) drawer.findViewById(R.id.drawerlinearlayout);
        listItems = (RecyclerView) drawer.findViewById(R.id.drawerListItem);

        lv = (ListView) drawer.findViewById(R.id.categoryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listItems.setLayoutManager(linearLayoutManager);

        SessionManager sessionManagerNgo = new SessionManager(BaseActivity.this);
        HashMap<String, String> typeOfUser = sessionManagerNgo.getUserDetails();
        userId = typeOfUser.get(SessionManager.KEY_USERID);
        userEmail = typeOfUser.get(SessionManager.KEY_USEREMAIL);
        userMobile = typeOfUser.get(SessionManager.KEY_CONTACTNO);
        userName = typeOfUser.get(SessionManager.KEY_FIRST_NAME)+" "+ typeOfUser.get(SessionManager.KEY_LAST_NAME);

        final String[] tittle = new String[]{"Home", "Wishlist", "Cart List", "My Orders", "Account", "Logout"};
        final int[] icons = new int[]{R.drawable.home, R.drawable.ic_favorite_grey_500_24dp, R.drawable.ic_shopping_cart_grey_500_24dp, R.drawable.order_completed, R.drawable.ic_account_circle_grey_500_24dp, R.drawable.logout};
        itemArrayList = new ArrayList<DrawerItems>();
        for (int i = 0; i < tittle.length; i++) {
            DrawerItems drawerItems = new DrawerItems();
            drawerItems.setTittle(tittle[i]);
            drawerItems.setIcons(icons[i]);
            itemArrayList.add(drawerItems);
        }
        final int[] selectedicons = new int[]{R.drawable.ic_home_black_24dp, R.drawable.ic_favorite_cyan_24dp, R.drawable.ic_shopping_cart_cyan_24dp, R.drawable.order_completed_cyan,  R.drawable.ic_account_circle_cyan_24dp, R.drawable.logout};
        itemSelectedArrayList = new ArrayList<DrawerItems>();
        for (int i = 0; i < tittle.length; i++) {
            DrawerItems drawerItems = new DrawerItems();
            drawerItems.setTittle(tittle[i]);
            drawerItems.setIcons(selectedicons[i]);
            itemSelectedArrayList.add(drawerItems);
        }
        drawerAdapter = new DrawerAdapter(itemArrayList, itemSelectedArrayList, drawer);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        getLayoutInflater().inflate(layoutResID, linearLayout, true);
        drawer.setClickable(true);
        drawerAdapter.notifyDataSetChanged();
        listItems.setAdapter(drawerAdapter);

        toolbar = (Toolbar) drawer.findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);

        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        super.setContentView(drawer);
        sessionManager = new SessionManager(getApplicationContext());
        lblName = (TextView) findViewById(R.id.lblName);
        lblName.setText(userName);
        //changeAppFont();

        getCategoryList();
        setupSearchList();
    }

    public void getCategoryList() {
        String webMethod = "showCategoryList";
        String params =  "?method=" + webMethod;
        String url = getString(R.string.url) + params;
        AndroidNetworking.get(url)
                .setTag("showCategoryList")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("showCategoriesResponse");

                            if (arr.length() == 0) {
                               // Toast.makeText(this, "List Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    categorydList.add(obj.getString("categoryId"));
                                    categoryNameList.add(obj.getString("categoryName"));
                                    adapter.notifyDataSetChanged();
                                }
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

    private void setupSearchList() {

        // bagTypeList.addAll(Arrays.asList(getResources().getStringArray(R.array.array_bagsType)));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNameList);
        lv.setAdapter(adapter);
        lv.setVisibility(View.GONE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                if (position > 0) {
                    categoryName = parent.getItemAtPosition(position).toString();
                    categoryId = categorydList.get(position);
//                }
                //selectedItem = String.valueOf(((TextView) view).getText());

                Intent gotoProductList = new Intent(BaseActivity.this,Product_List.class);
                gotoProductList.putExtra("categoryId",categoryId);
                startActivity(gotoProductList);
            }
        });
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem itemCart = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();

        cartBadge = sessionManager.getCartBadge();
        setBadgeCount(this, icon, cartBadge);

        SearchManager searchManager = (SearchManager) getSystemService(BaseActivity.this.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        ImageView searchMagIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchMagIcon.setImageResource(R.drawable.ic_search_cyan_24dp);

        ImageView searchClose = (ImageView) searchView.findViewById (android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_close_cyan_24dp);

        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorAccent));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setVisibility(View.VISIBLE);
//                tabLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lv.setVisibility(View.GONE);
//                tabLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.search:

                return true;
            case R.id.cart:
                Intent gotoCart = new Intent(this, Cart_List.class);
                startActivity(gotoCart);
                return true;
            case R.id.notification:
                Intent gotoNotification = new Intent(this, Notification.class);
                startActivity(gotoNotification);

                return true;
//            case R.id.restore:

//            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}