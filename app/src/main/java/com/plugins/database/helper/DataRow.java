package com.plugins.database.helper;

import org.json.JSONObject;

import java.util.HashMap;

public class DataRow extends HashMap<String, Object> {

    public String getString(String key) {
        return get(key) + "";
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key).toString());
    }

    public JSONObject toJSONObject() {
        JSONObject row = new JSONObject();
        try {
            for (String key : keySet()) {
                row.put(key, get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

}
