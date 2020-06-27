package com.longrise.ticketunion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.longrise.ticketunion.model.domain.CacheWithDuration;
import com.longrise.ticketunion.ui.activity.MyApplication;

public class JsonCacheUtil {

    private final Gson mGson;
    private final SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "sp_name";

    private JsonCacheUtil() {
        mSharedPreferences = MyApplication.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key, Object value) {
        saveCache(key, value, -1L);
    }

    /**
     * 保存数据
     * @param key
     * @param value
     * @param duration：数据保存的有效时间，超过这个时间后，表示数据过期
     *                比如：duration=3天，表示这个数据的有效时间是3天，当3天之后这个数据就失效了
     */
    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key).apply();
        String valueStr = mGson.toJson(value);
        // 保存一个有数据有时间的内容
        if (duration != -1L) {
            duration = System.currentTimeMillis() + duration;
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        editor.putString(key, cacheWithTime);
        editor.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        // 对时间进行判断
        long duration = cacheWithDuration.getDuration();
        if (duration != -1) {
            // 判断数据是否过期
            long dTime = duration - System.currentTimeMillis();
            if (dTime < 0) {
                // 过期了
                return null;
            }
        }
        // 没有过期，或者未设置有效时间(duration=-1)
        String cache = cacheWithDuration.getCache();
        T result = mGson.fromJson(cache, clazz);
        return result;
    }

    private static JsonCacheUtil jsonCacheUtilInstance = null;

    public static JsonCacheUtil getInstance() {
        if (jsonCacheUtilInstance == null) {
            jsonCacheUtilInstance = new JsonCacheUtil();
            return jsonCacheUtilInstance;
        }
        return jsonCacheUtilInstance;
    }
}
