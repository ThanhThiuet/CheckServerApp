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
    public boolean areContentsTheSame(int i, int i1) 
    {
        return oldDataList.get(i).getUrl().equals(newDataList.get(i1).getUrl())
                && oldDataList.get(i).getKeyWord().equals(newDataList.get(i1).getKeyWord())
                && oldDataList.get(i).getMessage().equals(newDataList.get(i1).getMessage())
                && oldDataList.get(i).getFrequency() == newDataList.get(i1).getFrequency()
                && oldDataList.get(i).isChecking() == newDataList.get(i1).isChecking();
    }
}
