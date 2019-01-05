package com.example.thanhthi.checkserver;

import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

import java.io.Serializable;

public interface SendDataToMainActivity extends Serializable
{
    void sendNewItem(ItemCheckServer item);

    void sendEditItem(ItemCheckServer item, int position);

    void sendDeleteItem(ItemCheckServer item, int position);
}
