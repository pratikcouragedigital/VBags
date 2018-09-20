package com.mobitechs.vbags;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.ExplodeAnimation;
import com.mobitechs.vbags.Drawer.BaseActivity;

public class Admin_Rights_Add extends BaseActivity implements View.OnClickListener {


    private AnimationSet mSet;
    private int mScreenWidth;
    String imFor="";
    RelativeLayout addCategoryCV,addProductCV,orderListCV,addPatternCV,addMaterialCV,addHandleCV,addCustomBagCV;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rights_add);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        addCategoryCV = (RelativeLayout) findViewById(R.id.addCategoryCV);
        addProductCV = (RelativeLayout) findViewById(R.id.addProductCV);
        orderListCV = (RelativeLayout) findViewById(R.id.orderListCV);
        addPatternCV = (RelativeLayout) findViewById(R.id.addPatternCV);
        addMaterialCV = (RelativeLayout) findViewById(R.id.addMaterialCV);
        addHandleCV = (RelativeLayout) findViewById(R.id.addHandleCV);
        addCustomBagCV = (RelativeLayout) findViewById(R.id.addCustomBagCV);

        addCategoryCV.setOnClickListener(this);
        addProductCV.setOnClickListener(this);
        orderListCV.setOnClickListener(this);
        addPatternCV.setOnClickListener(this);
        addMaterialCV.setOnClickListener(this);
        addHandleCV.setOnClickListener(this);
        addCustomBagCV.setOnClickListener(this);

        //get screen height
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        mScreenWidth = size.x;

        //animation set on list view
        mSet = new AnimationSet(false);
        mSet.addAnimation(new AlphaAnimation(0f,1f));
        mSet.addAnimation(new TranslateAnimation(-mScreenWidth, 0f, 0f, 0f));
        mSet.setDuration(1500);

        constraintLayout.startAnimation(mSet);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addCategoryCV) {
            imFor = "addCategory";
            doAnimation(v,imFor);
        }
        else if (v.getId() == R.id.addProductCV) {
            imFor = "addProduct";
            doAnimation(v,imFor);
        }
        else if (v.getId() == R.id.orderListCV) {
            imFor = "orderList";
            doAnimation(v,imFor);
        }
        else if (v.getId() == R.id.addPatternCV) {
            imFor = "pattern";
            doAnimation(v,imFor);
        }
        else if (v.getId() == R.id.addMaterialCV) {
            imFor = "material";
            doAnimation(v,imFor);
        }
        else if (v.getId() == R.id.addHandleCV) {
            imFor = "handle";
            doAnimation(v,imFor);
        }
        else if (v.getId() == R.id.addCustomBagCV) {
            imFor = "customBag";
            doAnimation(v,imFor);
        }
    }

    private void doAnimation(final View v, final String imFor) {
        new ExplodeAnimation(v).setExplodeMatrix(ExplodeAnimation.MATRIX_2X2)
                .setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
                        v.setVisibility(View.VISIBLE);

                        if(imFor.equals("addCategory")){
                            Intent gotoAddProduct = new Intent(Admin_Rights_Add.this, Add_Category.class);
                            startActivity(gotoAddProduct);
                        }
                        else if(imFor.equals("addProduct")){
                            Intent gotoAddProduct = new Intent(Admin_Rights_Add.this, Add_Product.class);
                            startActivity(gotoAddProduct);
                        }
                        else if(imFor.equals("orderList")){
                            Intent gotoAddProduct = new Intent(Admin_Rights_Add.this, Generated_Order_List.class);
                            startActivity(gotoAddProduct);
                        }
                        else if(imFor.equals("pattern")){
                            Intent gotoAddPattern = new Intent(Admin_Rights_Add.this, Add_Pattern_Material_Handle.class);
                            gotoAddPattern.putExtra("imFor",imFor);
                            startActivity(gotoAddPattern);
                        }
                        else if(imFor.equals("material")){
                            Intent gotoAddMaterial = new Intent(Admin_Rights_Add.this, Add_Pattern_Material_Handle.class);
                            gotoAddMaterial.putExtra("imFor",imFor);
                            startActivity(gotoAddMaterial);
                        }
                        else if(imFor.equals("handle")){
                            Intent gotoAddHandle = new Intent(Admin_Rights_Add.this, Add_Pattern_Material_Handle.class);
                            gotoAddHandle.putExtra("imFor",imFor);
                            startActivity(gotoAddHandle);
                        }
                        else if(imFor.equals("customBag")){
                            Intent gotoAddProduct = new Intent(Admin_Rights_Add.this, My_Custom_Bag.class);
                            startActivity(gotoAddProduct);
                        }
                    }
                })
                .animate();
    }
}
