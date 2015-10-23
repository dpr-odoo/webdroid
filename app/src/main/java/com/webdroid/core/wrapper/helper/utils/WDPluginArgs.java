package com.webdroid.core.wrapper.helper.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WDPluginArgs {
    public static final String TAG = WDPluginArgs.class.getSimpleName();

    HashMap<String, Object> _data = new HashMap<>();

    public void put(String key, Object value) {
        _data.put(key, value);
    }

    public Object get(String key) {
        return _data.get(key);
    }

    public Integer getInt(String key) {
        if (_data.get(key).toString().equals("false"))
            return 0;
        else
            return Integer.parseInt(_data.get(key).toString());
    }

    public Float getFloat(String key) {
        return Float.parseFloat(_data.get(key).toString());
    }

    public String getString(String key) {
        if (_data.containsKey(key) && _data.get(key) != null)
            return _data.get(key).toString();
        else
            return "false";
    }

    public WDPluginArgs getMap(String key) {
        return (WDPluginArgs) _data.get(key);
    }

    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(_data.get(key).toString());
    }


    public List<Object> values() {
        List<Object> values = new ArrayList<>();
        values.addAll(_data.values());
        return values;
    }

    public List<String> keys() {
        List<String> list = new ArrayList<>();
        list.addAll(_data.keySet());
        return list;
    }

    public boolean contains(String key) {
        return _data.containsKey(key);
    }

    public int size() {
        return _data.size();
    }

    @Override
    public String toString() {
        return _data.toString();
    }


    public void addAll(HashMap<String, Object> data) {
        _data.putAll(data);
    }

    public void addAll(WDPluginArgs row) {
        _data.putAll(row.getAll());
    }

    public HashMap<String, Object> getAll() {
        return _data;
    }


    public String getString(String key, String defValue) {
        if (contains(key)) {
            return getString(key);
        }
        return defValue;
    }

    public void remove(String key) {
        _data.remove(key);
    }
}
