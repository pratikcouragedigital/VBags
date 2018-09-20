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

public class Set_Password extends AppCompatActivity implements View.OnClickListener {

    EditText txtVerificationCode,txtPassword,txtConfirmPassword;
    Button btnSubmit;
    String verificationCode,userId,password,confirmPassword;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_pasword);

        sessionManager  = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);

        txtVerificationCode = (EditText) findViewById(R.id.txtVerificationCode);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            verificationCode = txtVerificationCode.getText().toString();
            password = txtPassword.getText().toString();
            confirmPassword = txtConfirmPassword.getText().toString();

            if (txtVerificationCode.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter OTP.", Toast.LENGTH_LONG).show();
            } else {

                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                Req_Registration checkverificationCode = new Req_Registration(this);
                checkverificationCode.SetNewPassword(verificationCode,confirmPassword,userId, progressDialog);

            }
        }
    }
}
