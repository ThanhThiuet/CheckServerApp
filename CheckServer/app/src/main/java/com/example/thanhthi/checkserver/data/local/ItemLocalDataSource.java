package com.example.thanhthi.checkserver.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thanhthi.checkserver.data.ItemDataSource;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

import java.util.ArrayList;
import java.util.List;

public class ItemLocalDataSource extends DatabaseHelper implements ItemDataSource
{
    private static ItemDataSource source;

    public static ItemDataSource getInstance(Context context)
    {
        if (source == null)
            source = new ItemLocalDataSource(context);
        return source;
    }

    public ItemLocalDataSource(Context context) {
        super(context);
    }

    @Override
    public boolean insertItem(ItemCheckServer item)
    {
        if (item == null) return false;
        SQLiteDatabase db = getWritableDatabase();

        long result = db.insert(
                ItemContract.ItemEntry.TABLE_NAME,
                null,
                item.getContentValues()
        );

        return result != -1;
    }

    @Override
    public boolean editItem(ItemCheckServer item)
    {
        if (item == null) return false;
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ItemContract.ItemEntry._ID + " =?";
        String[] whereArgs = {String.valueOf(item.getId())};

        long result = db.update(
                ItemContract.ItemEntry.TABLE_NAME,
                item.getContentValues(),
                whereClause,
                whereArgs
        );

        return result != -1;
    }

    @Override
    public boolean deleteItem(ItemCheckServer item)
    {
        if (item == null) return false;
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ItemContract.ItemEntry._ID + " =?";
        String[] whereArgs = {String.valueOf(item.getId())};

        long result = db.delete(
                ItemContract.ItemEntry.TABLE_NAME,
                whereClause,
                whereArgs
        );

        return result != -1;
    }

    @Override
    public List<ItemCheckServer> getAllItems()
    {
        List<ItemCheckServer> itemList = null;
        SQLiteDatabase db = getReadableDatabase();

        // Định nghĩa một phép chiếu (projection) xác định các cột từ csdl
        // bạn sẽ thực sự sử dụng projection này trong truy vấn sau đó
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_URL,
                ItemContract.ItemEntry.COLUMN_KEYWORD,
                ItemContract.ItemEntry.COLUMN_MESSAGE,
                ItemContract.ItemEntry.COLUMN_FREQUENCY,
                ItemContract.ItemEntry.COLUMN_IS_CHECKING
        };

        Cursor cursor = db.query(
                ItemContract.ItemEntry.TABLE_NAME, // bảng để truy vấn
                projection, // các cột để trả về
                null, // các cột cho mệnh đề WHERE
                null, // các giá trị cho mệnh đề WHERE
                null, // không group các hàng
                null, // không lọc bởi group hàng
                null // không sắp thứ tự theo order
        );

        if (cursor != null && cursor.moveToFirst())
        {
            itemList = new ArrayList<>();
            do {
                itemList.add(new ItemCheckServer(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null) cursor.close();
        db.close();

        return itemList;
    }

    @Override
    public ItemCheckServer getItemById(int id)
    {
        ItemCheckServer item = null;
        SQLiteDatabase db = getReadableDatabase();

        // Định nghĩa một phép chiếu (projection) xác định các cột từ csdl
        // bạn sẽ thực sự sử dụng projection này trong truy vấn sau đó
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_URL,
                ItemContract.ItemEntry.COLUMN_KEYWORD,
                ItemContract.ItemEntry.COLUMN_MESSAGE,
                ItemContract.ItemEntry.COLUMN_FREQUENCY,
                ItemContract.ItemEntry.COLUMN_IS_CHECKING
        };

        String selection = ItemContract.ItemEntry._ID + " =?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                ItemContract.ItemEntry.TABLE_NAME, // bảng để truy vấn
                projection, // các cột để trả về
                selection, // các cột cho mệnh đề WHERE
                selectionArgs, // các giá trị cho mệnh đề WHERE
                null, // không group các hàng
                null, // không lọc bởi group hàng
                null // không sắp thứ tự theo order
        );

        if (cursor != null && cursor.moveToFirst()) {
            item = new ItemCheckServer(cursor);
        }

        if (cursor != null) cursor.close();
        db.close();

        return item;
    }
}
