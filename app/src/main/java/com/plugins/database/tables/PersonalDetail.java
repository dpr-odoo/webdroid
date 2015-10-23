package com.plugins.database.tables;

import android.content.Context;
import android.provider.BaseColumns;

import com.plugins.database.DatabaseWrapper;

public class PersonalDetail extends DatabaseWrapper {

    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_ADDRESS = "address";
    public static final String COL_DATE_OF_BIRTH = "date_of_birth";
    public static final String COL_GENDER = "gender";
    public static final String COL_STATE = "state";
    public static final String COL_COUNTRY = "country";
    public static final String COL_CITY = "city";
    public static final String COL_MOBILE_NUMBER = "mobile_number";
    public static final String COL_SUMMARY = "summary";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_IMAGE = "image";

    public PersonalDetail(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName() + "(";
        sql += BaseColumns._ID + " integer PRIMARY KEY AUTOINCREMENT, ";
        sql += COL_FULL_NAME + " varchar(50), ";
        sql += COL_ADDRESS + " text, ";
        sql += COL_DATE_OF_BIRTH + " varchar(30), ";
        sql += COL_GENDER + " varchar(8), ";
        sql += COL_STATE + " varchar(30), ";
        sql += COL_COUNTRY + " varchar(30), ";
        sql += COL_CITY + " varchar(30), ";
        sql += COL_IMAGE + " blob, ";
        sql += COL_MOBILE_NUMBER + " varchar(30), ";
        sql += COL_SUMMARY + " text, ";
        sql += COL_USER_ID + " integer";
        sql += ")";
        return sql;
    }

    @Override
    public String tableName() {
        return "personal_details";
    }
}
