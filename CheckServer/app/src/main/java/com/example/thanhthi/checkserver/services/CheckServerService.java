package com.example.thanhthi.checkserver.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.thanhthi.checkserver.data.ItemDataSource;
import com.example.thanhthi.checkserver.data.ItemRepository;
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
    private ItemCheckServer model;
    private GetContentAsyntask asyntask;

    public static ItemDataSource repository;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
//        repository = ItemRepository.getInstance(getApplicationContext());
        List<ItemCheckServer> dataList = repository.getAllItems();

        int idSelected = intent.getFlags();

        for (ItemCheckServer item : dataList)
        {
            if (idSelected == item.getId())
            {
                model = item;
                break;
            }
        }

        if (model != null && model.isChecking())
        {
            asyntask = new GetContentAsyntask();
            asyntask.execute();
        }
        else
        {
            stopSelf();
            asyntask.cancel(false);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        asyntask.cancel(false);
        asyntask = null;
        super.onDestroy();
    }

    private final class GetContentAsyntask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... voids)
        {
            // get content from url
            String content = "";
            try
            {
                content = getContentHtml();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("CONTENT", content);
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

            HttpResponse response = null;
            try {
                response = client.execute(request);
            }
            catch (Exception e) {
                Log.e("HTTP-GET", e.getMessage());
            }
            if (response == null) return "";


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
        }
    }
}
