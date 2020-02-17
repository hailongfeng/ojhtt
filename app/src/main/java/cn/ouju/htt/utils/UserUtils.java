package cn.ouju.htt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.ouju.htt.constant.AppConstant;


/**
 * Created by Administrator on 2017/9/11.
 */

public class UserUtils {
    private static Context contexts;

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        contexts = context;
    }

    public static void saveBoolean(String guide_db, String key, boolean value) {
        SharedPreferences sp = contexts.getSharedPreferences(guide_db, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String guide_db, String key, boolean DefaultValue) {
        SharedPreferences sp = contexts.getSharedPreferences(guide_db, Context.MODE_PRIVATE);
        return sp.getBoolean(key, DefaultValue);
    }

    public static void saveString(String key, String value) {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveInt(String key, int value) {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key, String DefaultValue) {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        return sp.getString(key, DefaultValue);
    }

    public static int getInt(String key, int DefaultValue) {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        return sp.getInt(key, DefaultValue);
    }

    public static boolean getBoolean(String key, boolean DefaultValue) {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        return sp.getBoolean(key, DefaultValue);
    }

    public static void clearAll() {
        SharedPreferences sp = contexts.getSharedPreferences(AppConstant.USER_INFO, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
