package com.plugins.database.tables;

import android.content.Context;
import android.provider.BaseColumns;

import com.plugins.database.DatabaseWrapper;

public class Courses extends DatabaseWrapper {

    public static final String COL_INSTITUTE = "institute";
    public static final String COL_FIELD_OF_STUDY = "field_of_study";
    public static final String COL_DEGREE = "degree";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_END_DATE = "end_date";
    public static final String COL_ON_GOING = "on_going";
    public static final String COL_GRADE = "grade";
    public static final String COL_ACTIVITIES_AND_ASSOCIATED = "activities_associated";
    public static final String COL_SUMMARY = "summary";
    public static final String COL_USER_ID = "user_id";

    public Courses(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName() + "(";
        sql += BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT, ";
        sql += COL_INSTITUTE + " varchar(50), ";
        sql += COL_FIELD_OF_STUDY + " varchar(40), ";
        sql += COL_DEGREE + " varchar(50), ";
        sql += COL_START_DATE + " varchar(20), ";
        sql += COL_END_DATE + " varchar(20), ";
        sql += COL_GRADE+ " varchar(20), ";
        sql += COL_ON_GOING + " varchar(5), ";
        sql += COL_ACTIVITIES_AND_ASSOCIATED + " text, ";
        sql += COL_SUMMARY + " text, ";
        sql += COL_USER_ID + " integer ";
        sql += ")";
        return sql;
    }

    @Override
    public String tableName() {
        return "user_courses";
    }
}
