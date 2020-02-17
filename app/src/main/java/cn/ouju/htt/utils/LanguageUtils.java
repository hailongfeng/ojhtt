package cn.ouju.htt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

import cn.ouju.htt.constant.AppConstant;

public class LanguageUtils {


    private static Context contexts;

    public static void init(Context context) {
        contexts = context;
    }


    public static String getLanguage() {
        Locale locale = contexts.getResources().getConfiguration().locale;
        String result = "zh-cn";
        if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
            result = "zh-cn";
        } else if (locale.getLanguage().equals("en")) {
            result = "en-us";
        } else if (locale.getLanguage().equals("th")) {
            result = "th-th";
        }
        return result;
    }


    public static Context setLocal(Context context) {
        return updateResource(context, getSetLanguageLocale(context));
    }

    private static Context updateResource(Context context, Locale locale) {
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getSetLanguageLocale(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AppConstant.LANGUAGE_LOCALE, Context.MODE_PRIVATE);
        String language = sp.getString("language", null);
        if (!TextUtils.isEmpty(language)) {
            if (language.equals("zh-cn")) {
                return Locale.SIMPLIFIED_CHINESE;
            } else if (language.equals("en-us")) {
                return Locale.ENGLISH;
            } else if (language.equals("th-th")) {
                return new Locale("th", "TH");
            }
        }
        return Locale.SIMPLIFIED_CHINESE;
    }
}