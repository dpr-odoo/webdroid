package com.plugins.database.tables;

import android.content.Context;
import android.provider.BaseColumns;

import com.plugins.database.DatabaseWrapper;

public class SystemUsers extends DatabaseWrapper {

    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    public SystemUsers(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName() + "(";
        sql += BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT, ";
        sql += COL_FULL_NAME + " varchar(50), ";
        sql += COL_EMAIL + " varchar(40), ";
        sql += COL_PASSWORD + " varchar(30)";
        sql += ")";
        return sql;
    }

    @Override
    public String tableName() {
        return "system_users";
    }
}
