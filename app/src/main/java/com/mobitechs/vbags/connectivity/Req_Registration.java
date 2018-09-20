package com.mobitechs.vbags.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobitechs.vbags.Account;
import com.mobitechs.vbags.Forgot_Password;
import com.mobitechs.vbags.MainActivity;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.Register;
import com.mobitechs.vbags.Set_Password;
import com.mobitechs.vbags.sessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Req_Registration {

    Context context;
    private ProgressDialog progressDialog;
    public static  String userId,firstName,lastName,email,mobileNo,userType;
    SessionManager sessionManager;

    public Req_Registration(Register register) {

        context = register;
    }

    public Req_Registration(Forgot_Password forgot_password) {
        context =forgot_password;
    }

    public Req_Registration(Set_Password set_password) {
        context = set_password;
    }

    public Req_Registration(Context profile) {
        context = profile;
    }

    public void Register(String userFirstName,String userLastName, String userMobileNo, String userEmail, String userPassword, ProgressDialog pd) {
        progressDialog = pd ;
        String method ="userRegistration";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("firstName", userFirstName);
            jsonObject.put("lastName", userLastName);
            jsonObject.put("mobileNo", userMobileNo);
            jsonObject.put("email", userEmail);
            jsonObject.put("confirmPassword", userPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("register")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();

                            String result = response.getString("userRegistrationResponse");
                            if (result.equals("USERS_DETAILS_SAVED")) {
                                Toast.makeText(context, "You are Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent gotoLogin = new Intent(context, Account.class);
                                gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoLogin);

                            }else{
                                Toast.makeText(context, "Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e) {
                            e.getMessage();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }

    public void Login(String userLoginId, String userPassword, ProgressDialog pd) {

        sessionManager = new SessionManager(context);
        progressDialog = pd;
        String method = "userLogin";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userLoginId", userLoginId);
            jsonObject.put("confirmPassword", userPassword);
            jsonObject.put("method", method);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = context.getString(R.string.url);
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("login")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            if (response.getString("userLoginResponse").equals("LOGIN_FAILED")) {
                                Toast.makeText(context, "Please Enter Valid Credentials.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JSONArray arr = response.getJSONArray("userLoginResponse");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject jsonObject = arr.getJSONObject(i);

                                    firstName = jsonObject.getString("userFirstname");
                                    lastName = jsonObject.getString("userLastName");
                                    userId = jsonObject.getString("userId");
                                    email = jsonObject.getString("userEmail");
                                    mobileNo = jsonObject.getString("userMobileNo");
                                    userType = jsonObject.getString("userType");
                                    Toast.makeText(context, "Welcome: "+firstName + " " + lastName, Toast.LENGTH_LONG).show();
                                }
                                AddDeviceDetails();
                                sessionManager.createUserLoginSession(userId, firstName, lastName, email, mobileNo,userType);
                                Intent gotoHome = new Intent(context, MainActivity.class);
                                gotoHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHome);
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
    private void AddDeviceDetails() {

        sessionManager = new SessionManager(context);
        HashMap<String, String> userToken = sessionManager.getUserFirebaseNotificationToken();
        String token = userToken.get(SessionManager.KEY_TOKEN);
        String deviceId = userToken.get(SessionManager.KEY_DEVICEID);
        String manufacturer = userToken.get(SessionManager.KEY_MANUFACTURER);
        String model = userToken.get(SessionManager.KEY_MODEL);

        String deviceName = manufacturer +" "+ model;

        String method ="Add_Device_Details";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userId);
            jsonObject.put("tokenNo", token);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("deviceName", deviceName);
            jsonObject.put("method", method);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(method)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //do nothing
                        Toast.makeText(context, "Device details are saved.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        AddDeviceDetails();
                    }
                });
    }

    public void CheckEmail(String email,String userId, ProgressDialog pd) {

        progressDialog = pd ;
        String method ="checkEmail";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("email", email);
            jsonObject.put("userId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("checkEmail")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            String result = response.getString("checkEmailResponse");
                            if (result.equals("VALID_EMAIL")) {
                                Toast.makeText(context, "Please check your email for verification code", Toast.LENGTH_SHORT).show();
                                Intent setPassword = new Intent(context,Set_Password.class);
                                setPassword.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(setPassword);
                            }else{
                                Toast.makeText(context, "Please enter registered email", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e) {
                            e.getMessage();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });

    }

    public void SetNewPassword(String verificationCode, String password, String userId, ProgressDialog pd) {
        progressDialog = pd ;
        String method ="setNewPassword";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("OTP", verificationCode);
            jsonObject.put("password", password);
            jsonObject.put("userId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("setNewPassword")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            String result = response.getString("setNewPasswordResponse");
                            if (result.equals("NEW_PASSWORD_SUCCESSFULLY_SET")) {
                                Toast.makeText(context, "Password successfully reset", Toast.LENGTH_SHORT).show();
                                Intent gotoHome = new Intent(context,MainActivity.class);
                                gotoHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHome);
                            }else{
                                Toast.makeText(context, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e) {
                            e.getMessage();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }

    public void ChangeName(String uFirstName, String uLastName, String uId, ProgressDialog pd) {
        progressDialog = pd ;
        String method ="changeUserName";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("firstName", uFirstName);
            jsonObject.put("lastName", uLastName);
            jsonObject.put("userId", uId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("changeUserName")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray arr = response.getJSONArray("changeUserNameResponse");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject jsonObject = arr.getJSONObject(i);

                                firstName = jsonObject.getString("userFirstname");
                                lastName =jsonObject.getString("userLastName");
                                userId = jsonObject.getString("userId");
                                email = jsonObject.getString("userEmail");
                                mobileNo = jsonObject.getString("userMobileNo");
                                userType = jsonObject.getString("userType");
                            }
                            sessionManager.createUserLoginSession(userId,firstName,lastName,email,mobileNo,userType);
                            Toast.makeText(context, "Name successfully changed", Toast.LENGTH_SHORT).show();
                            Intent gotoHome = new Intent(context,MainActivity.class);
                            gotoHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(gotoHome);
                        }
                        catch(Exception e) {
                            e.getMessage();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }

    public void getVerificationCode(String email, String userId, ProgressDialog pd) {

        progressDialog = pd ;
        String method ="getVerificationCode";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("method", method);
            jsonObject.put("email", email);
            jsonObject.put("userId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = context.getString(R.string.url);

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag("getVerificationCode")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            String result = response.getString("getVerificationCodeResponse");
                            if (result.equals("VALID_EMAIL")) {
                                Toast.makeText(context, "Check your mail to get verification code.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e) {
                            e.getMessage();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getErrorDetail();
                    }
                });
    }
}
