package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.HomeNewsAdapter;
import cn.ouju.htt.bean.NewsBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;

public class MeNewsActivity extends BaseActivity implements AdapterView.OnItemClickListener, OnRefreshLoadMoreListener {
    @BindView(R.id.news_lv)
    ListView newsLv;
    @BindView(R.id.main_news_srf)
    SmartRefreshLayout mainNewsSrf;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    private List<NewsBean> lists;
    private HomeNewsAdapter adapter;
    private int page = 1;
    private int isEnd;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_main_news);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.news_notice), android.R.color.black);
    }

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        adapter = new HomeNewsAdapter(lists, this);
        newsLv.setAdapter(adapter);
        newsLv.setOnItemClickListener(this);
        visitNet();
        mainNewsSrf.setOnRefreshLoadMoreListener(this);
    }

    @Override
    @OnClick({R.id.common_ll_failure})
    public void onClick(View view) {
        if (view.getId() == R.id.common_ll_failure) {
            page = 1;
            lists.clear();
            visitNet();
        }
    }

    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        CommonService.query("Home/newsList", params, null, this, handler);
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils.getCode().equals("200")) {
                if (t[0].equals("Home/newsList")) {
                    commonLlDataNull.setVisibility(View.GONE);
                    commonLlFailure.setVisibility(View.GONE);
                    isEnd = jsonUtils.getInt("data", "is_end");
                    List<NewsBean> beans = jsonUtils.getEntityList("data", "data_list", new NewsBean());
                    if (beans != null && beans.size() > 0) {
                        lists.addAll(beans);
                    }
                    if (lists != null && lists.size() <= 0) {
                        commonLlDataNull.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    if (mainNewsSrf.getState() == RefreshState.Refreshing) {
                        mainNewsSrf.finishRefresh();
                    }
                    if (mainNewsSrf.getState() == RefreshState.Loading) {
                        mainNewsSrf.finishLoadMore();
                    }
                    if (isEnd == 1) {
                        mainNewsSrf.finishLoadMoreWithNoMoreData();
                    }
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

    @Override
    public void onNetWorkedFail(Object... strings) {
        super.onNetWorkedFail(strings);
        if (mainNewsSrf.getState() == RefreshState.Refreshing) {
            mainNewsSrf.finishRefresh();
        }
        if (mainNewsSrf.getState() == RefreshState.Loading) {
            mainNewsSrf.finishLoadMore();
        }
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra("id", lists.get(i).getId());
        intent.setClass(this, MeNewsDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        lists.clear();
        visitNet();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        if (isEnd != 1) {
            visitNet();
        } else {
            mainNewsSrf.finishLoadMoreWithNoMoreData();
        }
    }
}
