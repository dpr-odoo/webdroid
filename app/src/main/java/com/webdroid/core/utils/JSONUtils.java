package com.webdroid.core.utils;

import org.json.JSONArray;

import java.util.List;

public class JSONUtils {

    public static <T> JSONArray listToArray(List<T> list) {
        JSONArray array = new JSONArray();
        try {
            for (T t : list) {
                array.put(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }
}
