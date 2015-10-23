package com.plugins.database.helper;

import android.content.Context;

import com.plugins.database.DatabaseWrapper;

public class DatabaseCreator extends DatabaseWrapper {

    public DatabaseCreator(Context context) {
        super(context);
    }

    @Override
    public String createStatement() {
        return null;
    }

    @Override
    public String tableName() {
        return null;
    }
}
