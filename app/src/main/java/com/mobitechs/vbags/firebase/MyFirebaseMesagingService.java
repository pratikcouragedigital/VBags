package com.mobitechs.vbags.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobitechs.vbags.Notification;
import com.mobitechs.vbags.Product_List;
import com.mobitechs.vbags.Product_List_Details;
import com.mobitechs.vbags.R;
import com.mobitechs.vbags.sessionManager.SessionManager;

public class MyFirebaseMesagingService extends FirebaseMessagingService {

    Bitmap bitmap;
    String token,deviceId,model,manufacturer,fingerPrint;
    SessionManager sessionManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        ShowNotification(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        token = s;
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        fingerPrint = Build.FINGERPRINT;
        deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        createDeviceId();
    }

    public void createDeviceId() {
        sessionManager = new SessionManager(this);
        sessionManager.createUserFirebaseNotificationToken(token, deviceId,manufacturer,model,fingerPrint);
    }

    private void ShowNotification(RemoteMessage remoteMessage) {
        String message = remoteMessage.getData().get("NOTIFICATION_TYPE");

        if(message.trim().equals("OPEN_ACTIVITY_PRODUCT_DETAILS")){
            int notificationId = Integer.parseInt(remoteMessage.getData().get("NOTIFICATION_PRODUCT_ID"));
            String ImFrom = "Notification";
            Intent intent = new Intent(this, Product_List_Details.class);
            intent.putExtra("ImFrom",ImFrom );
            intent.putExtra("ProductId", remoteMessage.getData().get("NOTIFICATION_PRODUCT_ID"));
            intent.putExtra("ProductName", remoteMessage.getData().get("PRODUCT_NAME"));
            intent.putExtra("ProductPrice", remoteMessage.getData().get("PRODUCT_PRICE"));
            intent.putExtra("Image", remoteMessage.getData().get("PRODUCT_FIRST_IMAGE"));
            intent.putExtra("ProductImage", remoteMessage.getData().get("PRODUCT_FIRST_IMAGE"));
            intent.putExtra("DeliveryCharges", "0");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{
                bitmap = Glide.with(this)
                        .asBitmap()
                        .load(remoteMessage.getData().get("PRODUCT_FIRST_IMAGE"))
                        .into(100,1000).get();
            }catch (Exception e){
                e.printStackTrace();
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
            //set ic_launcer_short
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            } else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getData().get("PRODUCT_NAME"));
            notificationBuilder.setContentText(remoteMessage.getData().get("PRODUCT_PRICE"));
            notificationBuilder.setPriority(2);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN,1000,1000);
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("NOTIFICATION_DESCRIPTION")));
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(notificationId,notificationBuilder.build());

        }
        else {
            String imFrom = "Notification";
            Intent intent = new Intent(this, Notification.class);
            intent.putExtra("Title", remoteMessage.getNotification().getTitle());
            intent.putExtra("Message", remoteMessage.getNotification().getBody());
            intent.putExtra("Image", remoteMessage.getNotification().getBody());
            intent.putExtra("imFrom",imFrom );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
            //set ic_launcer_short
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            else {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher));
            }
            notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
            notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
            notificationBuilder.setPriority(2);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setLights(Color.GREEN, 1000, 1000);
            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, notificationBuilder.build());
        }
    }
}
