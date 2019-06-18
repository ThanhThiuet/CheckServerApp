package com.example.thanhthi.checkserver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.thanhthi.checkserver.MainActivity;
import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

public class NotificationHelper
{
    private Context context;
    private NotificationManager notificationManager;
    private ItemCheckServer item;

    private String CHANNEL_ID = "CheckServerApp";
    private String CHANNEL_NAME = "Check Server App";
    private int NOTIFICATION_ID = 0;

    public NotificationHelper(Context context, ItemCheckServer item)
    {
        this.context = context;
        this.item = item;
        NOTIFICATION_ID = item.getId();
    }

    public void createNotification()
    {
        Log.e("createNotification", "START_NOTIFY");
        Log.e("createNotification", "item: " + item);

        if (item == null) return;
        Log.e("createNotification", "item id: " + item.getId());
        Log.e("createNotification", "item is checking: " + item.isChecking());

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, item.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // notification channel
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(Context.NOTIFICATION_SERVICE, CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Message of item " + item.getId())
                .setContentTitle("Check server")
                .setContentText(item.getMessage())
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();

//        assert notificationManager != null;
//        notificationManager.notify(item.getId(), builder.build());
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }
}
