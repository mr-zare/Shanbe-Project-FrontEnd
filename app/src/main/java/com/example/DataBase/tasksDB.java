package com.example.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;
import java.text.SimpleDateFormat;

public class tasksDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "TASKS";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "myTasks";
    private static final String ID_COL = "token";
    private static final String NAME_COL = "title";
    private static final String CATEGORY = "category";

    public tasksDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
