package com.plugins.database.tables;

import android.content.Context;
import android.provider.BaseColumns;

import com.plugins.database.DatabaseWrapper;

public class Skills extends DatabaseWrapper {
    public static final String COL_NAME = "skill_name";
    public static final String COL_USER_ID = "user_id";

    public Skills(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName() + "(";
        sql += BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT, ";
        sql += COL_NAME + " varchar(50), ";
        sql += COL_USER_ID + " integer ";
        sql += ")";
        return sql;
    }

    @Override
    public String tableName() {
        return "user_skills";
    }
}
