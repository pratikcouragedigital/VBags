package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.Drawer.BaseActivity;
import com.mobitechs.vbags.connectivity.Req_Add_Admin_Right_Details;
import com.mobitechs.vbags.connectivity.Req_Category_Details;

public class Add_Category extends BaseActivity implements View.OnClickListener {

    TextView txtCategory;
    Button btnSubmit;
    String categry;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        txtCategory =  (TextView) findViewById(R.id.txtCategory);
        btnSubmit =  (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){

            categry = txtCategory.getText().toString();

            if (categry == null || categry.equals("")) {
                Toast.makeText(this, "Please Enter Category Name.", Toast.LENGTH_SHORT).show();
            }

            else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait. Category Is Adding.");
                progressDialog.show();

                try {
                    Req_Add_Admin_Right_Details addCategory = new Req_Add_Admin_Right_Details(this);
                    addCategory.AddCategory(categry,progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
