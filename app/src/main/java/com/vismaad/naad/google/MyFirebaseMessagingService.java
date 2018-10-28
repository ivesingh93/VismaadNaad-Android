package com.vismaad.naad.google;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vismaad.naad.R;
import com.vismaad.naad.navigation.NavigationActivity;
import android.media.RingtoneManager;

import android.app.PendingIntent;
import android.content.Context;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = getClass().getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNotification(String title, String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String id = "id_product";
            String name = "Product";
            String description = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);

            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);

            Intent resultIntent = new Intent(getApplicationContext(), NavigationActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")
                    .setSmallIcon(R.drawable.status_icon)
                    .setBadgeIconType(R.drawable.status_icon)
                    .setChannelId(id)
                    .setContentTitle(title)
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setNumber(1)
                    .setColor(255)
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis());
            notificationManager.notify(1000 /* ID of notification */, notificationBuilder.build());
        } else {
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentText(message)
                    .setTicker(title)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.status_icon)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1000, notificationBuilder.build());
        }
    }

}
