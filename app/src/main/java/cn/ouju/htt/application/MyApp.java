package cn.ouju.htt.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.Locale;

import cn.ouju.htt.R;
import cn.ouju.htt.utils.LanguageUtils;
import cn.ouju.htt.utils.UserUtils;
import cn.ouju.htt.utils.VersionCodeUtils;

/**
 * Created by Administrator on 2017/8/4.
 */

public class MyApp extends Application {
    public static MyApp application;

    public static MyApp getInstances() {
        return application;
    }


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setEnableAutoLoadMore(true);
                layout.setEnableOverScrollDrag(false);
                layout.setEnableOverScrollBounce(true);
                layout.setEnableLoadMoreWhenContentNotFull(true);
                layout.setEnableScrollContentWhenRefreshed(true);
            }
        });

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.white, R.color.colorGrayText);
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        // ClassicsHeader.REFRESH_HEADER_PULLING=getResources().getString(R.string.header_pulldown);
    }

    @Override
    public void onCreate() {
        super.onCreate();
       /* String language = UserUtils.getString("language", null);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (!TextUtils.isEmpty(language)) {
            if (language.equals("zh-cn")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else if (language.equals("en-us")) {
                config.locale = Locale.ENGLISH;
            } else if (language.equals("th-th")) {
                config.locale = new Locale("th", "TH");
            }
            resources.updateConfiguration(config, metrics);
        }
        Log.d("---111111----", "" + language);*/
       UserUtils.init(this);
        application = this;
        VersionCodeUtils.init(this);
        LanguageUtils.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtils.setLocal(base));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LanguageUtils.setLocal(this);
    }
}