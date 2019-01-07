package com.example.thanhthi.checkserver.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckServerService extends Service
{
    public static final String INFOR_MODEL = "info_model";

    private ItemCheckServer model;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Bundle bundle = intent.getExtras();
        String modelString = bundle.getString(INFOR_MODEL, "");
        model = ItemCheckServer.initialize(modelString);

        GetContentAsyntask asyntask = new GetContentAsyntask();

        while (model.isChecking())
        {
            asyntask.execute();

            long timeSleep = (long) (1000 * 60 * model.getFrequency());
            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Toast.makeText(this, "Starting service " + model.getId(), Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(getApplicationContext(), "Stop service " + model.getId() + " success.", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private final class GetContentAsyntask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... voids)
        {
            // get content from url
            String content = "";
            try {
                content = getContentHtml();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (content.isEmpty()) return null;

            // check keyword in content url
            List<String> keyWordList = new ArrayList<>(Arrays.asList(model.getKeyWord().split(";")));

            boolean isContained = false;
            for (String keyWord : keyWordList)
            {
                if (content.contains(keyWord))
                {
                    isContained = true;
                    Log.e("CHECK_CONTAIN_CONTENT", isContained + "");
                    break;
                }
            }
            return isContained;
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
            while((line = reader.readLine()) != null) {
                str.append(line);
            }
            in.close();

            return str.toString();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            // show notify
            NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext(), model);
            notificationHelper.createNotification();
            super.onPostExecute(aBoolean);
        }
    }
}
