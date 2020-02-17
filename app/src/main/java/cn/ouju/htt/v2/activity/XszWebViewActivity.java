package cn.ouju.htt.v2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import cn.ouju.htt.R;

public abstract class XszWebViewActivity extends XszBaseActivity {
    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    protected AgentWeb agentWeb;
    protected AgentWeb.PreAgentWeb preAgentWeb;

    public static final String INTENT_RETURN = "INTENT_RETURN";
    public static final String INTENT_CONTENT = "INTENT_CONTENT";
    private String content;
    public static Intent createIntent(Context context, String title, String content) {
        return new Intent(context, XszWebViewActivity.class).
                putExtra(XszWebViewActivity.INTENT_TITLE, title).
                putExtra(XszWebViewActivity.INTENT_CONTENT, content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content=getIntent().getStringExtra(INTENT_CONTENT);
        setContentView(R.layout.activity_xsz_web_view);
    }

    protected abstract String getUrl();
    @Override
    public void initView() {
        autoSetTitle();
        //agentWeb= getAgentWebField(preAgentWeb);
    }
    private com.just.agentweb.WebViewClient mWebViewClient=new com.just.agentweb.WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view,url,favicon);
            //do you  work
            print("开始加载："+url);
            //LogUtils.d("开始加载："+url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            print("结束加载："+url);
        }
    };
    com.just.agentweb.WebChromeClient mWebChromeClient =new com.just.agentweb.WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
            print("onProgressChanged");
        }
    };
//    private WebChromeClient mWebChromeClient=new WebChromeClient(){
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            //do you work
//            print("onProgressChanged");
//        }
//    };

    @Override
    public void initData() {
        agentWeb= AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                //.setWebChromeClient(mWebChromeClient)
                //.setWebViewClient(mWebViewClient)
                //.setSecurityType(AgentWeb.SecurityType.DEFAULT_CHECK)
                //.setSecutityType(AgentWeb.SecurityType.DEFAULT_CHECK)
                .createAgentWeb()
                .ready()
                .go(getUrl());
    }

    @Override
    public void initEvent() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
//    @Override
//    public void onDestroyView() {
//        mAgentWeb.getWebLifeCycle().onDestroy();
//        super.onDestroyView();
//    }

}
