package cn.ouju.htt.v2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gyf.barlibrary.ImmersionBar;

import cn.ouju.htt.R;
import zuo.biao.library.manager.ThreadManager;


public class StartActivity extends XszWebViewActivity {

    @Override
    protected String getUrl() {
        final String url="https://www.lksbb.com/";
        return url;
    }
}

//public class StartActivity extends WebViewActivity {
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        //setContentView(R.layout.activity_start_v2);
//////        ImmersionBar.with(this)
//////                .statusBarColor(R.color.transparent)     //状态栏颜色，不写默认透明色
//////                .init();
////    }
//
//    @Override
//    public void initData() {
//        final String url="http://www.lksbb.com/";
//        String url1="http://www.baidu.com";
//        //preAgentWeb.go(url);
////        ThreadManager.getInstance().runThread("StartActivity", new Runnable() {
////            @Override
////            public void run() {
////
////            }
////        });
//        mWebView.loadUrl(url1);
//
//        //agentWeb.getUrlLoader().loadUrl(url);//.loadDataWithBaseURL(null, getHtml(content), "text/html", "UTF-8", null);
//    }
//}
