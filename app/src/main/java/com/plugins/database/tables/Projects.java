package com.plugins.database.tables;

import android.content.Context;
import android.provider.BaseColumns;

import com.plugins.database.DatabaseWrapper;

public class Projects extends DatabaseWrapper {

    public static final String COL_PROJECT_TITLE = "project_title";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_END_DATE = "end_date";
    public static final String COL_SUMMARY = "summary";
    public static final String COL_ON_GOING = "on_going";
    public static final String COL_MEMBERS = "members";
    public static final String COL_USER_ID = "user_id";

    public Projects(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName() + "(";
        sql += BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT, ";
        sql += COL_PROJECT_TITLE + " varchar(50), ";
        sql += COL_START_DATE + " varchar(20), ";
        sql += COL_END_DATE + " varchar(20), ";
        sql += COL_SUMMARY + " text, ";
        sql += COL_ON_GOING + " varchar(7), ";
        sql += COL_MEMBERS + " text, ";
        sql += COL_USER_ID + " integer ";
        sql += ")";
        return sql;
    }

    @Override
    public String tableName() {
        return "user_projects";
    }
}
