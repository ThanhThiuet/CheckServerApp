package com.example.thanhthi.checkserver.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.thanhthi.checkserver.data.ItemDataSource;
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
    public static final String ID_SEND = "id";

    private ItemCheckServer model;
    public static ItemDataSource repository;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("LongLogTag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("CheckServerService_onStartCommand", "START_SERVICE");
        Log.e("CheckServerService_onStartCommand", "repository : " + repository);

        if (intent == null)
        {
            stopSelf();
            return START_STICKY;
        }

        int idSelected = intent.getIntExtra(ID_SEND, -1);
        Log.e("CheckServerService_onStartCommand", "idSelected : " + idSelected);

        if (repository == null || repository.getAllItems().size() == 0 || idSelected == -1)
        {
            stopSelf(idSelected);
            return START_STICKY;
        }

        // find item
        List<ItemCheckServer> dataList = repository.getAllItems();
        for (ItemCheckServer item : dataList)
        {
            if (idSelected == item.getId())
            {
                model = item;
                break;
            }
        }

        Log.e("CheckServerService_onStartCommand", "model: " + model);
        Log.e("CheckServerService_onStartCommand", "model id: " + model.getId());
        Log.e("CheckServerService_onStartCommand", "model is checking: " + model.isChecking());

        if (model != null && model.isChecking()) {
            new GetContentAsyncTask().execute();
        }
        else {
            stopSelf();
        }

        return START_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    private final class GetContentAsyncTask extends AsyncTask<Void, Void, Boolean>
    {
        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(Void... voids)
        {
            Log.e("AsyncTask_doInBackground", "START_ASYNC_TASK");
            Log.e("AsyncTask_doInBackground", "model: " + model);
            Log.e("AsyncTask_doInBackground", "model id: " + model.getId());
            Log.e("AsyncTask_doInBackground", "model is checking: " + model.isChecking());

            if (model == null || !model.isChecking()) return false;

            // get content from url
            String content = "";
            try
            {
                content = getContentHtml();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("CONTENT", content);
            if (content.isEmpty()) return false;

            // check keyword in content url
            List<String> keyWordList = new ArrayList<>(Arrays.asList(model.getKeyWord().split(";"))); // split a string to a List<String> by ";" character

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
            Log.e("AsyncTask_onPostExecute", "model: " + model);
            Log.e("AsyncTask_onPostExecute", "model id : " + model.getId());
            Log.e("AsyncTask_onPostExecute", "can get content html: " + aBoolean);

            // show notify
            if (aBoolean)
            {
                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext(), model);
                notificationHelper.createNotification();
            }
        }
    }
}
