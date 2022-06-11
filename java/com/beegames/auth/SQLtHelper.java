package com.beegames.auth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLtHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "loginDB";
    public static final String TABLE_USER = "user";

    public static final String KEY_ID = "_id";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_SCORE_2048 = "score_2048";
    public static final String KEY_SCORE_MINESW = "score_minesw";
    public static final String KEY_SCORE_3 = "score_3";
    public static final String KEY_SCORE_4 = "score_4";

    public static final String KEY_GRID_2048 = "grid_2048";

    public SQLtHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "create table " +
                TABLE_USER + "(" +
                KEY_ID + " iteger primary key," +
                KEY_NAME + " text," +
                KEY_EMAIL + " text," +
                KEY_PASSWORD + " text," +
                KEY_SCORE_2048 + " integer," +
                KEY_SCORE_MINESW + " integer," +
                KEY_SCORE_3 + " integer," +
                KEY_SCORE_4 + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_USER);
        onCreate(db);
    }
}
