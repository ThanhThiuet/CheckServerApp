package com.example.thanhthi.checkserver.data.local;

import android.provider.BaseColumns;

public class ItemContract
{
    public ItemContract() {
    }

    public static class ItemEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "tbl_item_check_server";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_KEYWORD = "key_word";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_FREQUENCY = "frequency";
        public static final String COLUMN_IS_CHECKING = "is_checking";
    }
}
