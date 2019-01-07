package com.example.thanhthi.checkserver.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.example.thanhthi.checkserver.MainActivity;
import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

public class NotificationHelper
{
    public static final String NOTIFICATION_CHANNEL_ID = "1000";
    public static final int requestCode = 0;

    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private ItemCheckServer item;

    public NotificationHelper(Context context, ItemCheckServer item)
    {
        this.context = context;
        this.item = item;
    }

    public void createNotification()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Message of item " + item.getId())
                .setContentText(item.getMessage())
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(Context.NOTIFICATION_SERVICE, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            assert notificationManager != null;
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        assert notificationManager != null;
        notificationManager.notify(requestCode, builder.build());
    }
}
