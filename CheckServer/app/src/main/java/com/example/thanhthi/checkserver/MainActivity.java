package com.example.thanhthi.checkserver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.thanhthi.checkserver.data.ItemDataSource;
import com.example.thanhthi.checkserver.data.ItemRepository;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.services.CheckServerService;
import com.example.thanhthi.checkserver.services.NotificationHelper;
import com.example.thanhthi.checkserver.updateItem.UpdateItemFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SendDataToMainActivity
{
    private RecyclerView recyclerView;
    private MainAdapter adapter;

    private List<ItemCheckServer> dataList;
    private ItemDataSource repository;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();
        initView();
        startServiceFirst();
    }

    private void initView()
    {
        setTitle("Check Server");
        recyclerView = findViewById(R.id.recyclerView);

        if (adapter == null)
        {
            adapter = new MainAdapter(getApplicationContext(), dataList, getSupportFragmentManager(), this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            adapter.updateDataList(dataList);
        }
    }

    private void getData()
    {
        repository = ItemRepository.getInstance(this);
        CheckServerService.repository = repository;

        dataList = repository.getAllItems();
        if (dataList == null) dataList = new ArrayList<>();
    }

    private void startServiceFirst()
    {
        if (dataList.isEmpty()) return;

        for (int i = 0; i < dataList.size(); i++)
        {
            ItemCheckServer item = dataList.get(i);

            Log.e("MainActivity_startServiceFirst_" + i, "model id: " + item.getId());
            Log.e("MainActivity_startServiceFirst_" + i, "model is checking: " + item.isChecking());

            if (item.isChecking()) {
                startCheckServer(item);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add:
            {
                UpdateItemFragment updateItemFragment = new UpdateItemFragment();
                updateItemFragment.setFlag(UpdateItemFragment.IS_CREATE);
                updateItemFragment.setSendDataToMainActivity(this);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(android.R.id.content, updateItemFragment, "tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
//            case R.id.delete:
//            {
//                // xóa nhiều
//                break;
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendNewItem(ItemCheckServer item)
    {
        if (repository.insertItem(item))
        {
            int position = repository.getAllItems().size() - 1;
            item = repository.getAllItems().get(position);
            Log.e("ADD_ITEM_SUCCESS", item.getId() + "");

            adapter.insertItem(item);
            dataList = adapter.getDataList();
            recyclerView.scrollToPosition(position + 1);

            Toast.makeText(this, "Thêm item " + item.getId() + " thành công!", Toast.LENGTH_SHORT).show();
            checkToStartService(item);
        }
        else
        {
            Toast.makeText(this, "Thêm item không thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendEditItem(ItemCheckServer item, int position)
    {
        if (repository.editItem(item))
        {
            Log.e("EDIT_ITEM_SUCCESS", item.getId() + "");
            adapter.editItem(item, position);
            dataList = adapter.getDataList();
            Toast.makeText(this, "Sửa item " + item.getId() + " thành công!", Toast.LENGTH_SHORT).show();
            checkToStartService(item);
        }
        else
        {
            Toast.makeText(this, "Sửa item " + item.getId() + " không thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendDeleteItem(ItemCheckServer item, int position)
    {
        if (repository.deleteItem(item))
        {
            Log.e("DELETE_ITEM_SUCCESS", item.getId() + "");
            adapter.deleteItem(item);
            dataList = adapter.getDataList();
            Toast.makeText(this, "Xóa item " + item.getId() + " thành công!", Toast.LENGTH_SHORT).show();
            stopCheckServer(item);
        }
        else
        {
            Toast.makeText(this, "Xóa item " + item.getId() + " không thành công!", Toast.LENGTH_SHORT).show();
        }
        recyclerView.scrollToPosition(position);
    }

    private void checkToStartService(ItemCheckServer model)
    {
        CheckServerService.repository = repository;

        if (model.isChecking()) {
            startCheckServer(model);
        }
    }

    @SuppressLint("LongLogTag")
    private void startCheckServer(ItemCheckServer model)
    {
        Log.e("MainActivity_startCheckServer", "model: " + model);
        Log.e("MainActivity_startCheckServer", "model id: " + model.getId());
        Log.e("MainActivity_startCheckServer", "model is checking: " + model.isChecking());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long timeRepeat = (long) (1000 * 60 * model.getFrequency());

        // start service
        Intent startIntent = new Intent(getBaseContext(), CheckServerService.class);
        startIntent.putExtra(CheckServerService.ID_SEND, model.getId());
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), model.getId(), startIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), timeRepeat, pendingIntent);

        startService(startIntent);
    }

    @SuppressLint("LongLogTag")
    private void stopCheckServer(ItemCheckServer model)
    {
        Log.e("MainActivity_stopCheckServer_1", "model: " + model);
        Log.e("MainActivity_stopCheckServer_1", "model id: " + model.getId());
        Log.e("MainActivity_stopCheckServer_1", "model is checking: " + model.isChecking());

        // stop service
        Intent stopIntent = new Intent(getApplicationContext(), CheckServerService.class);
        stopIntent.putExtra(CheckServerService.ID_SEND, model.getId());
        stopService(stopIntent);

        Log.e("MainActivity_stopCheckServer_2", "model: " + model);
        Log.e("MainActivity_stopCheckServer_2", "model id: " + model.getId());
        Log.e("MainActivity_stopCheckServer_2", "model is checking: " + model.isChecking());
    }
}
