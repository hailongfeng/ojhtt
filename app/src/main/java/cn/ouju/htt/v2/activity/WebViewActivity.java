package cn.ouju.htt.v2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import cn.ouju.htt.R;

public class WebViewActivity extends XszBaseActivity {
    @BindView(R.id.wv_webview)
    protected WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }

    @Override
    public void initView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
    }

    @Override
    public void initData() {
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onResume() {
        if (mWebView != null) {
            mWebView.resumeTimers();
            mWebView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.pauseTimers();
            mWebView.onPause();
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.stopLoading();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            unregisterForContextMenu(mWebView);
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
