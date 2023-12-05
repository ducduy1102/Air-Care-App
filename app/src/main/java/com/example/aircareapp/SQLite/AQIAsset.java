package com.example.aircareapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AQIAsset extends SQLiteOpenHelper {
    public static final String DBName = "AqiAsset.db";
    public static final int DBVersion = 1;

    public AQIAsset(Context context) {
        super(context, DBName, null, DBVersion);
    }

    public void QueryData (String sqlData) {
        SQLiteDatabase getData = getWritableDatabase();
        getData.execSQL(sqlData);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE AqiAsset(PM10 VARCHAR(10), PM25 VARCHAR(10), time VARCHAR(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS AqiAsset";
        db.execSQL(sql);
        onCreate(db);
    }
}
