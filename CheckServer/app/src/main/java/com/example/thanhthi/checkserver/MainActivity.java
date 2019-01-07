package com.example.thanhthi.checkserver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.thanhthi.checkserver.data.ItemDataSource;
import com.example.thanhthi.checkserver.data.ItemRepository;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.services.CheckServerService;
import com.example.thanhthi.checkserver.services.NotificationHelper;
import com.example.thanhthi.checkserver.updateItem.UpdateItemFragment;

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
    }

    @Override
    protected void onResume()
    {
        for (ItemCheckServer item : dataList) {
            checkToStartService(item);
        }
        super.onResume();
    }

    private void initView()
    {
        setTitle("Check Server");
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new MainAdapter(getApplicationContext(), dataList, getSupportFragmentManager(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getData()
    {
        repository = ItemRepository.getInstance(this);
        dataList = repository.getAllItems();
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
            case R.id.delete:
            {
                // xóa nhiều
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendNewItem(ItemCheckServer item)
    {
        if (repository.insertItem(item))
        {
            adapter.insertItem(item);
            dataList = adapter.getDataList();
            recyclerView.scrollToPosition(0);
            Toast.makeText(this, "Thêm item thành công!", Toast.LENGTH_SHORT).show();
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
            adapter.editItem(item, position);
            dataList = adapter.getDataList();
            Toast.makeText(this, "Sửa item thành công!", Toast.LENGTH_SHORT).show();
            checkToStartService(item);
        }
        else
        {
            Toast.makeText(this, "Sửa item không thành công!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendDeleteItem(ItemCheckServer item, int position)
    {
        if (repository.deleteItem(item))
        {
            adapter.deleteItem(item);
            dataList = adapter.getDataList();
            Toast.makeText(this, "Xóa item thành công!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Xóa item không thành công!", Toast.LENGTH_SHORT).show();
        }
        recyclerView.scrollToPosition(position);
    }

    private void checkToStartService(ItemCheckServer model)
    {
        if (model.isChecking())
            startCheckServer(model);
        else
            stopCheckServer(model);

//        startCheckServer(model);
    }

    private void startCheckServer(ItemCheckServer model)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        long timeRepeat = (long) (1000 * 60 * model.getFrequency());

        // start service
        Intent startIntent = new Intent(getApplicationContext(), CheckServerService.class);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), NotificationHelper.requestCode, startIntent, model.getId());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), timeRepeat, pendingIntent);

        startIntent.putExtra(CheckServerService.INFOR_MODEL, model.toJson());
        startService(startIntent);
    }

    private void stopCheckServer(ItemCheckServer model)
    {
        // stop service
        Intent stopIntent = new Intent(getApplicationContext(), CheckServerService.class);
        stopService(stopIntent);
    }
}
