package com.example.thanhthi.checkserver.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.thanhthi.checkserver.MainActivity;
import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckServerService extends Service {
    public static final String INFOR_MODEL = "info_model";

    private ItemCheckServer model;
    private boolean isContained;

//    private NotificationManager notificationManager = null;
//    private int NOTIFICATION = 1; // Unique identifier for our notification

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isContained = false;
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Bundle bundle = intent.getExtras();
        String modelString = bundle.getString(INFOR_MODEL, "");
        model = ItemCheckServer.initialize(modelString);

        final long timeSleep = (long) (1000 * 60 * model.getFrequency());

        GetContentAsyntask asyntask = new GetContentAsyntask();
        asyntask.execute();

        Toast.makeText(this, "Starting service " + model.getId(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Nội dung: " + content, Toast.LENGTH_SHORT).show();

        //notify
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher) // the status icon
//                .setTicker("Service running...") // the status text
//                .setWhen(System.currentTimeMillis()) // the time stamp
//                .setContentTitle("My App") // the label of the entry
//                .setContentText("Service running...") // the content of the entry
//                .setContentIntent(contentIntent) // the intent to send when the entry is clicked
//                .setOngoing(true) // make persistent (disable swipe-away)
//                .build();
//        // Start service in foreground mode
//        startForeground(NOTIFICATION, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(getApplicationContext(), "Stop service " + model.getId() + " success.", Toast.LENGTH_SHORT).show();
//        notificationManager.cancel(NOTIFICATION); // Remove notification
        super.onDestroy();
    }

    private final class GetContentAsyntask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            // get content from url
            String content = "";
            try {
                content = getContentHtml();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("SHOW-CONTENT", content);
            if (content.isEmpty()) return null;

            List<String> keyWordList = new ArrayList<>(Arrays.asList(model.getKeyWord().split(";")));

            for (String keyWord : keyWordList)
            {
                Log.e("KEY-WORD-ILIMENT", keyWord);
                if (content.contains(keyWord))
                {
                    isContained = true;
                    Log.e("CHECK_CONTAIN_CONTENT", isContained + "");
                    break;
                }
            }

            return null;
        }

        private String getContentHtml() throws IOException
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("https://" + model.getUrl());
            HttpResponse response = client.execute(request);

            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                str.append(line);
            }
            in.close();
            Log.e("HTTP-GET", str.toString());
            return str.toString();
        }
    }
}
