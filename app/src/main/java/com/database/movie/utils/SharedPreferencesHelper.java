/*
 * Copyright (c) 2018 Ilanthirayan Paramanathan Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.database.movie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.database.movie.BuildConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public class SharedPreferencesHelper {

    private static final String TAG = SharedPreferencesHelper.class.getSimpleName();

    private static SharedPreferences cache;
    private static boolean dirty = true;
    private static SharedPreferences.Editor currentEditor;
    private static int held = 0;
    private static final WeakHashMap<SharedPreferences.OnSharedPreferenceChangeListener, Void> observers = new WeakHashMap<>();

    private Context mContext;

    public SharedPreferencesHelper(Context context) {
        this.mContext = context;
    }

    public void invalidate() {
        dirty = true;
    }

    private SharedPreferences.Editor getEditor(SharedPreferences pref) {
        if (currentEditor == null) {
            currentEditor = pref.edit();
        }
        return currentEditor;
    }

    public int getInt(Enum<?> key, int def) {
        return getInt(key.toString(), def);
    }

    public int getInt(String key, int def) {
        SharedPreferences pref = read();
        return pref.getInt(key, def);
    }

    public float getFloat(Enum<?> key, float def) {
        SharedPreferences pref = read();
        return pref.getFloat(key.toString(), def);
    }

    public float getFloat(String key, float def) {
        SharedPreferences pref = read();
        return pref.getFloat(key, def);
    }

    public long getLong(Enum<?> key, long def) {
        return getLong(key.toString(), def);
    }

    public long getLong(String key, long def) {
        SharedPreferences pref = read();
        return pref.getLong(key, def);
    }

    public String getString(Enum<?> key, String def) {
        SharedPreferences pref = read();
        return pref.getString(key.toString(), def);
    }

    public String getString(String key, String def) {
        SharedPreferences pref = read();
        return pref.getString(key, def);
    }

    @Nullable
    public String getString(Enum<?> key) {
        SharedPreferences pref = read();
        return pref.getString(key.toString(), null);
    }

    @Nullable
    public String getString(String key) {
        SharedPreferences pref = read();
        return pref.getString(key, null);
    }

    public boolean getBoolean(Enum<?> key, boolean def) {
        return getBoolean(key.toString(), def);
    }

    public boolean getBoolean(String key, boolean def) {
        SharedPreferences pref = read();
        return pref.getBoolean(key, def);
    }

    public Object get(String key) {
        SharedPreferences pref = read();
        return pref.getAll().get(key);
    }

    public Map<String, ?> getAll() {
        SharedPreferences pref = read();
        return pref.getAll();
    }

    public Set<String> getAllKeys() {
        return new HashSet<>(getAll().keySet());
    }

    public void setInt(Enum<?> key, int val) {
        setInt(key.toString(), val);
    }

    public void setInt(String key, int val) {
        SharedPreferences pref = read();
        getEditor(pref).putInt(key, val);
        commitIfNotHeld();
        if (BuildConfig.DEBUG) Log.d(TAG, key + " = (int) " + val);
    }

    public void setFloat(Enum<?> key, float val) {
        setFloat(key.toString(), val);
    }

    public void setFloat(String key, float val) {
        SharedPreferences pref = read();
        getEditor(pref).putFloat(key, val);
        commitIfNotHeld();
        if (BuildConfig.DEBUG) Log.d(TAG, key + " = (float) " + val);
    }

    public void setLong(Enum<?> key, long val) {
        setLong(key.toString(), val);
    }

    public void setLong(String key, long val) {
        SharedPreferences pref = read();
        getEditor(pref).putLong(key, val);
        commitIfNotHeld();
        if (BuildConfig.DEBUG) Log.d(TAG, key + " = (long) " + val);
    }

    public void setString(Enum<?> key, String val) {
        setString(key.toString(), val);
    }

    public void setString(String key, String val) {
        SharedPreferences pref = read();
        getEditor(pref).putString(key, val);
        commitIfNotHeld();
        if (BuildConfig.DEBUG) Log.d(TAG, key + " = (string) " + val);
    }

    public void setBoolean(Enum<?> key, boolean val) {
        setBoolean(key.toString(), val);
    }

    public void setBoolean(String key, boolean val) {
        SharedPreferences pref = read();
        getEditor(pref).putBoolean(key, val);
        commitIfNotHeld();
        if (BuildConfig.DEBUG) Log.d(TAG, key + " = (bool) " + val);
    }

    public boolean contains(final Enum<?> key) {
        SharedPreferences pref = read();
        return pref.contains(key.toString());
    }

    public boolean contains(final String key) {
        SharedPreferences pref = read();
        return pref.contains(key);
    }

    public void remove(Enum<?> key) {
        remove(key.toString());
    }

    public void remove(String key) {
        SharedPreferences pref = read();
        getEditor(pref).remove(key);
        commitIfNotHeld();
        if (BuildConfig.DEBUG) Log.d(TAG, key + " removed");
    }

    private synchronized void commitIfNotHeld() {
        if (held > 0) {
            // don't do anything now
        } else {
            if (currentEditor != null) {
                if (Build.VERSION.SDK_INT >= 9) {
                    currentEditor.apply();
                } else {
                    currentEditor.commit();
                }
                currentEditor = null;
            }
        }
    }

    private synchronized SharedPreferences read() {
        SharedPreferences res;
        if (dirty || cache == null) {
            long start = 0;
            if (BuildConfig.DEBUG) start = SystemClock.uptimeMillis();
            res = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (BuildConfig.DEBUG) Log.d(TAG, "SharedPreferencesHelper was read from disk in " + (SystemClock.uptimeMillis() - start) + " ms");
            dirty = false;

            // re-register observers if the SharedPreferences object changes
            if (cache != null && res != cache && observers.size() > 0) {
                for (final SharedPreferences.OnSharedPreferenceChangeListener observer : observers.keySet()) {
                    cache.unregisterOnSharedPreferenceChangeListener(observer);
                    res.registerOnSharedPreferenceChangeListener(observer);
                }
            }

            cache = res;
        } else {
            res = cache;
        }

        return res;
    }
}
