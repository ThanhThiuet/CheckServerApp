package com.example.thanhthi.checkserver.data;

import android.content.Context;

import com.example.thanhthi.checkserver.data.local.ItemLocalDataSource;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

import java.util.List;

public class ItemRepository implements ItemDataSource
{
    private static ItemRepository repository;
    private ItemDataSource localDataSource;

    public ItemRepository(ItemDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ItemRepository getInstance(Context context)
    {
        if (repository == null)
            repository = new ItemRepository(ItemLocalDataSource.getInstance(context));
        return repository;
    }

    @Override
    public boolean insertItem(ItemCheckServer item) {
        return localDataSource.insertItem(item);
    }

    @Override
    public boolean editItem(ItemCheckServer item) {
        return localDataSource.editItem(item);
    }

    @Override
    public boolean deleteItem(ItemCheckServer item) {
        return localDataSource.deleteItem(item);
    }

    @Override
    public List<ItemCheckServer> getAllItems() {
        return localDataSource.getAllItems();
    }

    @Override
    public ItemCheckServer getItemById(int id) {
        return localDataSource.getItemById(id);
    }
}
