package com.mobitechs.vbags.internetConnectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkInfo.State mState;
    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected){
            Log.i("NET", "Connected" + isConnected);
            mState = NetworkInfo.State.CONNECTED;
        }

        else{
            Log.i("NET", "Not Connected" + isConnected);
            mState = NetworkInfo.State.DISCONNECTED;
            Intent networkReciever = new Intent(context, NotifyNetworkConnectionMessage.class);
            networkReciever.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            networkReciever.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(networkReciever);
        }

    }
}
