package com.example.thanhthi.checkserver;

import android.support.v7.util.DiffUtil;

import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

import java.util.List;

public class ItemDiffCallback extends DiffUtil.Callback
{
    private List<ItemCheckServer> oldDataList, newDataList;

    public ItemDiffCallback(List<ItemCheckServer> oldDataList, List<ItemCheckServer> newDataList)
    {
        this.oldDataList = oldDataList;
        this.newDataList = newDataList;
    }

    @Override
    public int getOldListSize() {
        return oldDataList.size();
    }

    @Override
    public int getNewListSize() {
        return newDataList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return oldDataList.get(i).getId() == newDataList.get(i1).getId();
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return oldDataList.get(i).equals(newDataList.get(i1));
    }
}
