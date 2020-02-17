package cn.ouju.htt.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.constant.AppConstant;
import cn.ouju.htt.utils.LanguageUtils;
import cn.ouju.htt.utils.UserUtils;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.me_about_wv)
    WebView meAboutWv;
    @BindView(R.id.me_about_ll_failure)
    LinearLayout meAboutLlFailure;
    private StringBuffer sb = new StringBuffer();

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_about);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.about_us), android.R.color.black);

    }

    @Override
    protected void initData() {
        WebSettings setting = meAboutWv.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setDomStorageEnabled(true);
        setting.setLoadWithOverviewMode(true);
        sb.append(AppConstant.ABOUT_URL);
        sb.append("H5/about");
        SharedPreferences sp = getSharedPreferences(AppConstant.LANGUAGE_LOCALE, Context.MODE_PRIVATE);
        String language = sp.getString("language", null);
        sb.append("?language=" + language);
        meAboutWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    hideProgress();
                } else {
                    showProgress();
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                        view.loadUrl("about:blank");
                    }
                }
            }

        });

        Log.d("------", "------" + sb.toString());
        meAboutWv.loadUrl(sb.toString());

    }

    @Override
    @OnClick({R.id.me_about_ll_failure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_about_ll_failure:
                meAboutWv.loadUrl(sb.toString());
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {

    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }
}
