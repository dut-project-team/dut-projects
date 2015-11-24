package com.blogspot.sontx.whitelight.lib;

import android.util.Log;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Copyright by NE 2015.
 * Created by noem on 17/11/2015.
 */
public final class SharedObject {
    private static final String LOG_TAG = "APP_DEBUG";
    private static SharedObject instance = null;
    private Dictionary<String, Object> dictionary;

    private SharedObject() {
        dictionary = new Hashtable<>();
    }

    public static SharedObject getInstance() {
        if (instance == null)
            instance = new SharedObject();
        return instance;
    }

    public synchronized Object pop(String key) {
        Object obj = get(key);
        remove(key);
        Log.d(LOG_TAG, "pop '" + key + "'");
        return obj;
    }

    public synchronized Object get(String key) {
        Log.d(LOG_TAG, "get '" + key + "'");
        return dictionary.get(key);
    }

    public synchronized void set(String key, Object obj) {
        if (obj == null)
            return;
        dictionary.put(key, obj);
        Log.d(LOG_TAG, "set '" + key + "'");
    }

    public synchronized void remove(String key) {
        dictionary.remove(key);
        Log.d(LOG_TAG, "removed '" + key + "'");
    }
}
