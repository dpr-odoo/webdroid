package com.plugins.database.helper;

import android.database.Cursor;


public class CursorUtils {

    public static DataRow toDatarow(Cursor cr) {
        DataRow row = new DataRow();
        for (String col : cr.getColumnNames()) {
            row.put(col, CursorUtils.cursorValue(col, cr));
        }
        return row;
    }

    public static Object cursorValue(String column, Cursor cr) {
        Object value = false;
        int index = cr.getColumnIndex(column);
        switch (cr.getType(index)) {
            case Cursor.FIELD_TYPE_NULL:
                value = false;
                break;
            case Cursor.FIELD_TYPE_STRING:
                value = cr.getString(index);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                value = cr.getInt(index);
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                value = cr.getFloat(index);
                break;
            case Cursor.FIELD_TYPE_BLOB:
                value = cr.getBlob(index);
                break;
        }
        return value;
    }
}
