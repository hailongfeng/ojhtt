package cn.ouju.htt.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class VersionCodeUtils {
    private static Context contexts;
    private static VersionCodeUtils versionCodeUtils;

    public static void init(Context context) {
        contexts = context;
    }

    public static VersionCodeUtils getInstance() {
        if (versionCodeUtils == null) {
            versionCodeUtils = new VersionCodeUtils();
        }
        return versionCodeUtils;
    }

    //返回版本号,对应androidmanifest.xml下的android:versioncode
    public  int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = contexts.getPackageManager().getPackageInfo(contexts.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public  String getVersionName() {
        String VersionName = "";
        try {
            VersionName = contexts.getPackageManager().getPackageInfo(contexts.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return VersionName;
    }

}