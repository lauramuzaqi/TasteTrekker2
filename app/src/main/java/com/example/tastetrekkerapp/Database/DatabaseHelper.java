package com.example.tastetrekkerapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tastetrekkerapp.Models.Recipe;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TasteTrekkerDb";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_FAVORITES = "Favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SERVINGS = "servings";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_IMAGE = "image";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_SERVINGS + " INTEGER, " +
                    COLUMN_TIME + " INTEGER, " +
                    COLUMN_IMAGE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}