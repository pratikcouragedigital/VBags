package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.connectivity.Req_Registration;
import com.mobitechs.vbags.sessionManager.SessionManager;

import java.util.HashMap;


public class Account extends AppCompatActivity implements View.OnClickListener {

    View v;
    EditText txtLoginId, txtPassword;
    Button btnLogin;
    TextView btnRegister, txtForgotPassword;
    String userLoginId, userPassword;
    private ProgressDialog progressDialog;

    RelativeLayout personalDetailsLayout;
    RelativeLayout changePasswordLayout;
    RelativeLayout personalDetailsContentLayout;
    RelativeLayout changePasswordContentLayout;

    CollapsingToolbarLayout detailsCollapsingToolbar;
    CoordinatorLayout detailsCoordinatorLayout;
    AppBarLayout detailsAppBar;
    Toolbar myProfileToolbar;
    NestedScrollView detailsNestedScrollView;

    FloatingActionButton fabPersonalDetails, fabChangePassword;
    EditText txtFirstName, txtLastName, txtMobileNo, txtEmail, txtVerificationCode, txtChangePassword, txtConfirmPassword;
    String firstName, lastName, mobileNo, email, verificationCode, changePassword, confirmPassword;
    TextView txtGetVerificationCode;
    int countForm1 = 0;
    int countForm2 = 0;

    String userId = "";
    SessionManager sessionManager;

    public Account() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get(sessionManager.KEY_USERID);
        firstName = user.get(sessionManager.KEY_FIRST_NAME);
        lastName = user.get(sessionManager.KEY_LAST_NAME);
        email = user.get(sessionManager.KEY_USEREMAIL);
        mobileNo = user.get(sessionManager.KEY_CONTACTNO);
        
        if (userId == null) {
            userId = "";
        }

        if (userId.equals("")) {
            setContentView(R.layout.account);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnRegister = (TextView) findViewById(R.id.btnRegister);

            txtLoginId = (EditText) findViewById(R.id.txtLoginId);
            txtPassword = (EditText) findViewById(R.id.txtPassword);
            txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

            btnLogin.setOnClickListener(this);
            btnRegister.setOnClickListener(this);
            txtForgotPassword.setOnClickListener(this);

        } else {
            setContentView(R.layout.profile);

            detailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.userDetailsCollapsingToolbar);
            detailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.detailsCoordinatorLayout);
            detailsAppBar = (AppBarLayout) findViewById(R.id.detailsAppBar);
            myProfileToolbar = (Toolbar) findViewById(R.id.myProfileToolbar);
            detailsNestedScrollView = (NestedScrollView) findViewById(R.id.detailsNestedScrollView);

            personalDetailsLayout = (RelativeLayout) findViewById(R.id.personalDetailsLayout);
            changePasswordLayout = (RelativeLayout) findViewById(R.id.changePasswordLayout);
            personalDetailsContentLayout = (RelativeLayout) findViewById(R.id.personalDetailsContentLayout);
            changePasswordContentLayout = (RelativeLayout) findViewById(R.id.changePasswordContentLayout);
            fabPersonalDetails = (FloatingActionButton) findViewById(R.id.fabPersonalDetails);
            fabChangePassword = (FloatingActionButton) findViewById(R.id.fabChangePassword);

            txtFirstName = (EditText) findViewById(R.id.txtFirstName);
            txtLastName = (EditText) findViewById(R.id.txtLastName);
            txtMobileNo = (EditText) findViewById(R.id.txtMobileNo);
            txtEmail = (EditText) findViewById(R.id.txtEmail);

            txtVerificationCode = (EditText) findViewById(R.id.txtVerificationCode);
            txtChangePassword = (EditText) findViewById(R.id.txtChangePassword);
            txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
            txtGetVerificationCode = (TextView) findViewById(R.id.txtGetVerificationCode);

            fabPersonalDetails.setVisibility(View.GONE);
            fabChangePassword.setVisibility(View.GONE);

            personalDetailsContentLayout.setVisibility(View.GONE);
            changePasswordContentLayout.setVisibility(View.GONE);

            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) detailsAppBar.getLayoutParams();
            layoutParams.height = getResources().getDisplayMetrics().widthPixels;

            detailsAppBar.post(new Runnable() {
                @Override
                public void run() {
                    int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                    setAppBarOffset(heightPx);
                }
            });
            detailsCollapsingToolbar.setTitle(firstName + " " + lastName);

            personalDetailsLayout.setOnClickListener(this);
            changePasswordLayout.setOnClickListener(this);
            fabPersonalDetails.setOnClickListener(this);
            fabChangePassword.setOnClickListener(this);
            txtGetVerificationCode.setOnClickListener(this);

            txtFirstName.setText(firstName);
            txtLastName.setText(lastName);
            txtMobileNo.setText(mobileNo);
            txtEmail.setText(email);

        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) detailsAppBar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(detailsCoordinatorLayout, detailsAppBar, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onClick(View v) {

        if (userId.equals("")) {
            if (v.getId() == R.id.btnLogin) {
                userLoginId = txtLoginId.getText().toString();
                userPassword = txtPassword.getText().toString();

                if (txtLoginId.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Your Email Id Or Phone No.", Toast.LENGTH_LONG).show();
                } else if (txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    Req_Registration login = new Req_Registration(this);
                    login.Login(userLoginId, userPassword, progressDialog);
                }
            }
            else if (v.getId() == R.id.btnRegister) {
                Intent gotoRegister = new Intent(this, Register.class);
                startActivity(gotoRegister);
            }
            else if (v.getId() == R.id.txtForgotPassword) {
                Intent gotoForgotPassword = new Intent(this, Forgot_Password.class);
                startActivity(gotoForgotPassword);
            }
        } else {
            //if logged in
            if (v.getId() == R.id.personalDetailsLayout) {

                if (countForm1 == 0) {
                    personalDetailsContentLayout.setVisibility(View.VISIBLE);
                    fabPersonalDetails.setVisibility(View.VISIBLE);

                    countForm1 = 1;
                } else {
                    personalDetailsContentLayout.setVisibility(View.GONE);
                    fabPersonalDetails.setVisibility(View.GONE);

                    countForm1 = 0;
                }
            } else if (v.getId() == R.id.changePasswordLayout) {

                if (countForm2 == 0) {
                    changePasswordContentLayout.setVisibility(View.VISIBLE);

                    fabChangePassword.setVisibility(View.VISIBLE);
                    countForm2 = 1;
                } else {
                    changePasswordContentLayout.setVisibility(View.GONE);
                    fabChangePassword.setVisibility(View.GONE);
                    countForm2 = 0;
                }
            } else if (v.getId() == R.id.fabPersonalDetails) {
                firstName = txtFirstName.getText().toString();
                lastName = txtLastName.getText().toString();

                if (txtFirstName.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter First Name.", Toast.LENGTH_LONG).show();
                }
                else if (txtLastName.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Last Name.", Toast.LENGTH_LONG).show();
                }
                else{
                    ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    Req_Registration changeName = new Req_Registration(this);
                    changeName.ChangeName(firstName, lastName, userId, progressDialog);
                }
            }
            else if (v.getId() == R.id.fabChangePassword) {
                verificationCode = txtVerificationCode.getText().toString();
                changePassword = txtChangePassword.getText().toString();
                confirmPassword = txtConfirmPassword.getText().toString();

                if (txtVerificationCode.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Verification Code.", Toast.LENGTH_LONG).show();
                } else if (txtChangePassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                } else if (txtConfirmPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_LONG).show();
                } else {
                    if (!changePassword.equals(confirmPassword)) {
                        Toast.makeText(this, "Password and confirm password not matching.", Toast.LENGTH_LONG).show();
                    } else {
                        ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Please Wait");
                        progressDialog.show();

                        Req_Registration changePassword = new Req_Registration(this);
                        changePassword.SetNewPassword(verificationCode, confirmPassword, userId, progressDialog);
                    }
                }
            }
            else if (v.getId() == R.id.txtGetVerificationCode) {
                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();

                Req_Registration getVerificationCode = new Req_Registration(this);
                getVerificationCode.getVerificationCode(email, userId, progressDialog);
            }
        }
    }
}