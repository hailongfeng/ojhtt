package cn.ouju.htt.ui.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.EmailBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.ui.view.MyWebView;
import cn.ouju.htt.utils.AlertDialogUtils;

public class MeSendEmailDetailsActivity extends BaseActivity {
    @BindView(R.id.email_details_tv_title)
    TextView emailDetailsTvTitle;
    @BindView(R.id.email_details_web)
    MyWebView emailDetailsWeb;
    @BindView(R.id.email_details_tv_hint)
    TextView emailDetailsTvHint;
    @BindView(R.id.email_details_tv_time)
    TextView emailDetailsTvTime;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_send_email_details);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.email_content), android.R.color.black);
    }

    @Override
    protected void initData() {
        WebSettings settings = emailDetailsWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        EmailBean bean = (EmailBean) getIntent().getSerializableExtra("item");
        Map<String, Object> params = new TreeMap<>();
        params.put("id", bean.getId());
        CommonService.query("Home/mailDetail", params, null, this, handler);
        int type = getIntent().getIntExtra("type", 0);
        if (type != 0) {
            if (type == 1) {
                emailDetailsTvHint.setText(getString(R.string.inbox_time) + "：");
            } else if (type == 2) {
                emailDetailsTvHint.setText(getString(R.string.outbox_time)+"：");
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Home/mailDetail")) {
                        emailDetailsTvTitle.setText(jsonUtils.getString("title", "data"));
                        emailDetailsTvTime.setText(jsonUtils.getString("send_time", "data"));
                        emailDetailsWeb.loadData(jsonUtils.getString("content", "data"), "text/html; charset=UTF-8", null);
                    }
                } else
                    AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }
}
