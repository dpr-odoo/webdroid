package com.plugins.database.tables;

import android.content.Context;
import android.provider.BaseColumns;

import com.plugins.database.DatabaseWrapper;

public class Company extends DatabaseWrapper {

    public static final String COL_NAME = "company_name";
    public static final String COL_DESIGNATION = "designation";
    public static final String COL_JOIN_DATE = "join_date";
    public static final String COL_END_DATE = "end_date";
    public static final String COL_ON_GOING = "on_going";
    public static final String COL_SUMMARY = "summary";
    public static final String COL_USER_ID = "user_id";

    public Company(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName() + "(";
        sql += BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT, ";
        sql += COL_NAME + " varchar(50), ";
        sql += COL_DESIGNATION + " varchar(40), ";
        sql += COL_JOIN_DATE + " varchar(20), ";
        sql += COL_END_DATE + " varchar(20), ";
        sql += COL_ON_GOING + " varchar(5), ";
        sql += COL_SUMMARY + " text, ";
        sql += COL_USER_ID + " integer ";
        sql += ")";
        return sql;
    }

    @Override
    public String tableName() {
        return "user_company";
    }
}
