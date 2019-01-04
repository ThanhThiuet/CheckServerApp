package com.example.thanhthi.checkserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.thanhthi.checkserver.data.ItemDataSource;
import com.example.thanhthi.checkserver.data.ItemRepository;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.updateItem.UpdateItemActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity
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

    private void initView()
    {
        setTitle("Check Server");
        recyclerView = findViewById(R.id.recyclerView);

        if (adapter == null)
        {
            adapter = new MainAdapter(dataList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
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
                Intent intent = new Intent(MainActivity.this, UpdateItemActivity.class);
                intent.putExtra(UpdateItemActivity.FLAG, UpdateItemActivity.IS_CREATE);
                startActivity(intent);
                break;
            }
            case R.id.delete:
            {

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
