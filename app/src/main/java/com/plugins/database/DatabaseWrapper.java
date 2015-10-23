package com.plugins.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.plugins.database.helper.CursorUtils;
import com.plugins.database.helper.DataRow;
import com.plugins.database.helper.TableHelper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseWrapper extends SQLiteOpenHelper implements TableHelper {

    public static final String DB_NAME = "ResumeBuilder.db";
    public static final int DB_VERSION = 1;
    private Context mContext;

    public DatabaseWrapper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating all databases
        Tables tables = new Tables();
        for (Class<? extends DatabaseWrapper> cls : tables.tables()) {
            try {
                Constructor<?> constructor = cls.getConstructor(Context.class);
                TableHelper helper = (TableHelper) constructor.newInstance(mContext);
                String statement = helper.createStatement();
                if (statement != null) {
                    db.execSQL(statement);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        Long id = db.insert(tableName(), null, values);
        db.close();
        return id.intValue();
    }

    public int update(ContentValues values, String where, String[] args) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.update(tableName(), values, where, args);
        db.close();
        return count;
    }

    public int delete(String where, String[] args) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete(tableName(), where, args);
        db.close();
        return count;
    }

    public DataRow browse(int id) {
        DataRow row = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.query(tableName(), null, BaseColumns._ID + " = ?",
                new String[]{id + ""}, null, null, null);
        if (cr.moveToFirst()) {
            row = CursorUtils.toDatarow(cr);
        }
        cr.close();
        db.close();
        return row;
    }

    public DataRow browse(String selection, String[] args) {
        DataRow row = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.query(tableName(), null, selection, args, null, null, null);
        if (cr.moveToFirst()) {
            row = CursorUtils.toDatarow(cr);
        }
        cr.close();
        db.close();
        return row;
    }

    public int count(String selection, String[] args) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.query(tableName(), null, selection, args, null, null, null);
        count = cr.getCount();
        cr.close();
        db.close();
        return count;
    }


    public List<DataRow> select() {
        return select(null, null);
    }

    public List<DataRow> select(String selection, String[] args) {
        List<DataRow> records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.query(tableName(), null, selection, args, null, null, null);
        if (cr.moveToFirst()) {
            do {
                records.add(CursorUtils.toDatarow(cr));
            } while (cr.moveToNext());
        }
        cr.close();
        db.close();
        return records;
    }

}
