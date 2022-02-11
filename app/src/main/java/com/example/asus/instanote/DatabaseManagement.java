package com.example.asus.instanote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseManagement extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ColorsDatabase";
    private static final String TABLE_NAME = "ColorsDatabase";
    private static final String COLUMN_1 = "fileName";
    private static final String COLUMN_2 = "color";

    public DatabaseManagement(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" + COLUMN_1 + " primary key, " + COLUMN_2 + " int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Calendar.getInstance().getTime();
    }

    boolean addEntry(String fileName, int color) {
        ContentValues cv = new ContentValues();
        cv.put("fileName", fileName);
        cv.put("color", color);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    ArrayList<EntryItem> getList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<EntryItem> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            items.add(new EntryItem(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
        }
        return items;
    }

    boolean updateEntry(String fileName, int color) {
        ContentValues cv = new ContentValues();
        cv.put("fileName", fileName);
        cv.put("color", color);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, cv, COLUMN_1 + " = '" + fileName+"'", null) > 0;
    }

    boolean deleteEntry(String fileName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_1 + " = '" + fileName + "'", null) > 0;
    }

    int getColor(String fileName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + COLUMN_2 + " from "+TABLE_NAME+" where " + COLUMN_1 + " = '"+ fileName + "'", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }
}
