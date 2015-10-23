package com.plugins.database.helper;

import android.content.Context;

import com.plugins.database.DatabaseWrapper;
import com.plugins.database.Tables;

import java.lang.reflect.Constructor;

public class TableFinder extends Tables {

    private Context context;

    public TableFinder(Context context) {
        this.context = context;
    }

    public DatabaseWrapper findModel(String table_name) {
        for (Class<? extends DatabaseWrapper> tbl : tables()) {
            try {
                Constructor<?> constructor = tbl.getConstructor(Context.class);
                DatabaseWrapper wrapper = (DatabaseWrapper) constructor.newInstance(context);
                if (wrapper.tableName().equals(table_name)) {
                    return wrapper;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
