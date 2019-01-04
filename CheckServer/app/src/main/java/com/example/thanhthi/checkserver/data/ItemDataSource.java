package com.example.thanhthi.checkserver.data;

import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

import java.util.List;

public interface ItemDataSource
{
    boolean insertItem(ItemCheckServer item);
    boolean editItem(ItemCheckServer item);
    boolean deleteItem(ItemCheckServer item);

    List<ItemCheckServer> getAllItems();
    ItemCheckServer getItemById(int id);
}
