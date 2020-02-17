package cn.ouju.htt.ui.activity;

import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.NewsBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;

public class MeNewsDetailsActivity extends BaseActivity {


    @BindView(R.id.news_details_title)
    TextView newsDetailsTitle;
    @BindView(R.id.news_details_time)
    TextView newsDetailsTime;
    @BindView(R.id.news_details_wb)
    WebView newsDetailsWb;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_main_news_details);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.news_details), android.R.color.black);

    }

    @Override
    protected void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", getIntent().getStringExtra("id"));
        CommonService.query("Home/newsDetail", params, null, this, handler);
        WebSettings settings = newsDetailsWb.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        settings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        settings.setAllowFileAccess(true); // 允许访问文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils.getCode().equals("200")) {
                if (t[0].equals("Home/newsDetail")) {
                    NewsBean bean = jsonUtils.getEntity("data", new NewsBean());
                    newsDetailsTitle.setText(bean.getTitle());
                    newsDetailsTime.setText(bean.getAdd_time());
                    newsDetailsWb.loadData(getNewContent(bean.getContent()), "text/html; charset=UTF-8", null);
                }
            } else
                AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    private String getNewContent(String content) {
        Document doc = Jsoup.parse(content);
        Elements element = doc.getElementsByTag("img");
        for (Element elements : element) {
            elements.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }
}
