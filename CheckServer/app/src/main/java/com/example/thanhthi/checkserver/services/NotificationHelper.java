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
import android.util.Log;

import com.example.thanhthi.checkserver.MainActivity;
import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

public class NotificationHelper
{
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
        Log.e("createNotification", "START_NOTIFY");
        Log.e("createNotification", "item: " + item);

        if (item == null) return;
        Log.e("createNotification", "item id: " + item.getId());
        Log.e("createNotification", "item is checking: " + item.isChecking());

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, item.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
            NotificationChannel notificationChannel = new NotificationChannel(Context.NOTIFICATION_SERVICE, "NOTIFICATION_CHANNEL_NAME_" + item.getId(), importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            assert notificationManager != null;
            builder.setChannelId(item.getId() + "");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        assert notificationManager != null;
        notificationManager.notify(item.getId(), builder.build());
    }
}
