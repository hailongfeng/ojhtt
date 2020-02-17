package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.MeSendEmailAdapter;
import cn.ouju.htt.bean.EmailBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeSendEmailActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnRefreshLoadMoreListener {
    @BindView(R.id.me_send_lv)
    ListView meSendLv;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.main_srf)
    SmartRefreshLayout MainSrf;
    private List<EmailBean> lists;
    private MeSendEmailAdapter adapter;
    private int page = 1;
    private int isEnd;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_send_email);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        int type = getIntent().getIntExtra("type", 0);
        if (type != 0) {
            if (type == 1) {
                setTitle(getString(R.string.inbox), android.R.color.black);
            } else if (type == 2) {
                setTitle(getString(R.string.outbox), android.R.color.black);
            }
        }
    }

    @Override
    protected void initData() {
        visitNet();
        lists = new ArrayList<>();
        adapter = new MeSendEmailAdapter(lists, this);
        TextView textView = new TextView(this);
        textView.setVisibility(View.GONE);
        meSendLv.addFooterView(textView);
        meSendLv.setAdapter(adapter);
        meSendLv.setOnItemClickListener(this);
        MainSrf.setOnRefreshLoadMoreListener(this);
    }

    private void visitNet() {
        Map<String, Object> params = new TreeMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        params.put("page", page);
        int type = getIntent().getIntExtra("type", 1);
        params.put("type", type);
        CommonService.query("Home/mailList", params, null, this, handler);
    }

    @Override
    @OnClick({R.id.common_ll_failure})
    public void onClick(View v) {
        if (v.getId() == R.id.common_ll_failure) {
            page = 1;
            lists.clear();
            visitNet();
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Home/mailList")) {
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<EmailBean> beans = jsonUtils.getEntityList("data", "data_list", new EmailBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                        }
                        adapter.notifyDataSetChanged();
                        if (lists != null && lists.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                            commonLlFailure.setVisibility(View.GONE);
                        }
                        if (MainSrf.getState() == RefreshState.Loading) {
                            MainSrf.finishLoadMore();
                        }
                        if (MainSrf.getState() == RefreshState.Refreshing) {
                            MainSrf.finishRefresh();
                        }
                        if (isEnd == 1) {
                            MainSrf.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else
                    AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        super.onNetWorkedFail(strings);
        if (MainSrf.getState() == RefreshState.Loading) {
            MainSrf.finishLoadMore();
        }
        if (MainSrf.getState() == RefreshState.Refreshing) {
            MainSrf.finishRefresh();
        }
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < lists.size()) {
            Intent intent = new Intent(this, MeSendEmailDetailsActivity.class);
            intent.putExtra("item", lists.get(position));
            intent.putExtra("type", getIntent().getIntExtra("type", 1));
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        lists.clear();
        page = 1;
        visitNet();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (isEnd != 1) {
            page++;
            visitNet();
        }else {
            MainSrf.finishLoadMoreWithNoMoreData();
        }
    }
}