package com.mobitechs.vbags;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobitechs.vbags.connectivity.Req_Registration;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText txtFirstName,txtLastName,txtMobileNo,txtEmail,txtPassword,txtConfirmPassword,txtAddressLine1,txtAddressLine2,txtCity,txtState,txtCountry,txtPincode;
    Button  btnRegister;

    String userFirstName,userLastName,userMobileNo,userEmail,userPassword,userConfirmPassword,addressLine1,addressLine2,userCity,userState,userCountry,userPincode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtMobileNo = (EditText) findViewById(R.id.txtMobileNo);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

    }

    public static boolean isValidEmail(String emailForValidation) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailForValidation).matches();

    }
    private boolean isValidMobile(String mobilenoValidation)
    {
        return android.util.Patterns.PHONE.matcher(mobilenoValidation).matches();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnRegister){
            //userFirstName,userMobileNo,userEmail,userPassword,addressLine1,userCity,userState,userPincode

            userFirstName = txtFirstName.getText().toString();
            userLastName = txtLastName.getText().toString();
            userMobileNo = txtMobileNo.getText().toString();
            userEmail = txtEmail.getText().toString();
            userPassword = txtPassword.getText().toString();
            userConfirmPassword = txtConfirmPassword.getText().toString();


            if (txtFirstName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your First Name.", Toast.LENGTH_LONG).show();
            }
            else if (txtLastName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Last Name.", Toast.LENGTH_LONG).show();
            }
            else if (txtMobileNo.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Mobile No.", Toast.LENGTH_LONG).show();
            }
            else if (txtEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email Id.", Toast.LENGTH_LONG).show();
            }
            else if (txtPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_LONG).show();
            }
            else if (txtConfirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_LONG).show();
            }
            else {

                if( (userMobileNo.length() < 10 || userMobileNo.length() > 13) ) {
                    Toast.makeText(this, "Mobile No is not valid", Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmail(userEmail)) {
                    Toast.makeText(this, "Email is not valid", Toast.LENGTH_LONG).show();
                }
                else if(!userPassword.equals(userConfirmPassword)){
                    Toast.makeText(this, "Password and confirm password not matching.", Toast.LENGTH_LONG).show();
                }
                else{
                    ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    Req_Registration login = new Req_Registration(this);
                    login.Register(userFirstName,userLastName,userMobileNo,userEmail,userPassword,progressDialog);
                }
            }
        }
    }
}
