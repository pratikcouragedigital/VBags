package com.mobitechs.vbags.internetConnectivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class NotifyNetworkConnectionMessage extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayAlert();
    }

    private void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet connection!");
        builder.setMessage("Please check your internet connection.").setCancelable(
                false).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = NotifyNetworkConnectionMessage.this.getPackageManager();
        ComponentName component = new ComponentName(NotifyNetworkConnectionMessage.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Intent mainActivityIntent = new Intent(NotifyNetworkConnectionMessage.this, SplashScreen.class);
//        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(mainActivityIntent);
    }
}