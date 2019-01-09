package com.example.thanhthi.checkserver.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.thanhthi.checkserver.data.local.ItemContract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemCheckServer
{
    private int id;
    private String url;
    private String keyWord;
    private String message;
    private double frequency;
    private boolean isChecking = false;
    private boolean isSelected = false;

    public ItemCheckServer(String url, String keyWord, String message, double frequency)
    {
        this.url = url;
        this.keyWord = keyWord;
        this.message = message;
        this.frequency = frequency;
        isChecking = true;
    }

    public ItemCheckServer(String url, String keyWord, String message, double frequency, boolean isChecking)
    {
        this.url = url;
        this.keyWord = keyWord;
        this.message = message;
        this.frequency = frequency;
        this.isChecking = isChecking;
    }

    public ItemCheckServer(Cursor cursor)
    {
        id = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry._ID));
        url = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_URL));
        keyWord = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_KEYWORD));
        message = cursor.getString(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_MESSAGE));
        frequency = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_FREQUENCY));
        isChecking = cursor.getInt(cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_IS_CHECKING)) == 1;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public boolean isChecking() {
        return isChecking;
    }

    public void setChecking(boolean checking) {
        isChecking = checking;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();

        if (url != null) {
            contentValues.put(ItemContract.ItemEntry.COLUMN_URL, url);
        }

        if (keyWord != null) {
            contentValues.put(ItemContract.ItemEntry.COLUMN_KEYWORD, keyWord);
        }

        if (message != null) {
            contentValues.put(ItemContract.ItemEntry.COLUMN_MESSAGE, message);
        }

        if (frequency != 0.0d) {
            contentValues.put(ItemContract.ItemEntry.COLUMN_FREQUENCY, frequency);
        }

        if (isChecking) {
            contentValues.put(ItemContract.ItemEntry.COLUMN_IS_CHECKING, 1);
        } else {
            contentValues.put(ItemContract.ItemEntry.COLUMN_IS_CHECKING, 0);
        }

        return contentValues;
    }
}
