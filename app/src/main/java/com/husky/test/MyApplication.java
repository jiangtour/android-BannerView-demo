package com.husky.test;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author husky
 */
public class MyApplication extends Application {
    public static Context applicationContext;
    private static MyApplication instance;

    private static final String APK_ID = "apk_id";
    private String apk_id = null;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
    }

    public static MyApplication getInstance() {

        return instance;
    }

    /**
     * 设置下载APK ID
     *
     * @param id
     * @return
     */
    public void setApkId(String id) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(APK_ID, id).commit()) {
            apk_id = id;
        }
    }

    /**
     * 获取下载APK ID
     *
     * @return
     */
    public String getApkId() {
        apk_id = null;
        if (apk_id == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            apk_id = preferences.getString(APK_ID, null);
        }
        return apk_id;
    }
}
