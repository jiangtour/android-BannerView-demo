package com.husky.library.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author husky
 */
public class PreferenceUtils {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static PreferenceUtils instance;

    private static final String APK_ID = "apk_id";
    private String apk_id = null;

    private PreferenceUtils(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public static PreferenceUtils getInstance(Context context){
        if (instance  == null){
            synchronized (PreferenceUtils.class){
                if (instance == null){
                    instance = new PreferenceUtils(context);
                }
            }
        }
        return instance;
    }

    /**
     * 设置下载APK ID
     *
     * @param id
     * @return
     */
    public void setApkId(String id) {
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
            apk_id = preferences.getString(APK_ID, null);
        }
        return apk_id;
    }
}
