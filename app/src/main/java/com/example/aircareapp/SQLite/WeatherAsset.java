package com.example.aircareapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherAsset extends SQLiteOpenHelper {
    public static final String DBName = "Weather Asset";
    public static final int DBVersion = 1;

    public WeatherAsset(Context context) {
        super(context, DBName, null, DBVersion);
    }

    public void QueryData (String sqlData) {
        SQLiteDatabase getData = getWritableDatabase();
        getData.execSQL(sqlData);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE WeatherAsset(temperature char, humidity integer, wind char, time char )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS WeatherAsset";
        db.execSQL(sql);
        onCreate(db);
    }
}
