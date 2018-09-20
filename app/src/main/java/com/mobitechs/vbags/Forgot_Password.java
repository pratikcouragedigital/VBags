package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobitechs.vbags.connectivity.Req_Registration;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.HashMap;

public class Forgot_Password extends AppCompatActivity implements View.OnClickListener {
    EditText txtEmail;
    Button btnSubmit;
    String email,userId;
    SessionManager sessionManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        sessionManager  = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(sessionManager.KEY_USEREMAIL);
        userId = user.get(sessionManager.KEY_USERID);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            email = txtEmail.getText().toString();

            if (txtEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email Id.", Toast.LENGTH_LONG).show();
            } else {

                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                Req_Registration checkEmail = new Req_Registration(this);
                checkEmail.CheckEmail(email,userId, progressDialog);

            }
        }
    }
}
