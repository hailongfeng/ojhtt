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

import butterknife.BindView;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.ReleaseAdapter;
import cn.ouju.htt.bean.ReleaseBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeReleaseActivity extends BaseActivity implements OnRefreshLoadMoreListener, AdapterView.OnItemClickListener {
    @BindView(R.id.me_send_lv)
    ListView meSendLv;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.main_srf)
    SmartRefreshLayout mainSrf;
    private ReleaseAdapter adapter;
    private List<ReleaseBean> lists;
    private int page = 1;
    private int isEnd;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_send_email);
        setTitle(getString(R.string.release_list), android.R.color.black);
    }

    @Override
    protected void initData() {
        visitNet();
        lists = new ArrayList<>();
        adapter = new ReleaseAdapter(lists, this);
        meSendLv.setAdapter(adapter);
        mainSrf.setOnRefreshLoadMoreListener(this);
        meSendLv.setOnItemClickListener(this);
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
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils.getCode().equals("200")) {
                if (t[0].equals("Dti/returnList")) {
                    commonLlDataNull.setVisibility(View.GONE);
                    commonLlFailure.setVisibility(View.GONE);
                    isEnd = jsonUtils.getInt("data", "is_end");
                    List<ReleaseBean> beans = jsonUtils.getEntityList("data", "data_list", new ReleaseBean());
                    if (beans != null && beans.size() > 0) {
                        lists.addAll(beans);
                    }
                    if (lists == null || lists.size() <= 0) {
                        commonLlDataNull.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    if (mainSrf.getState() == RefreshState.Refreshing) {
                        mainSrf.finishRefresh();
                    }
                    if (mainSrf.getState() == RefreshState.Loading) {
                        mainSrf.finishLoadMore();
                    }
                    if (isEnd == 1) {
                        mainSrf.finishLoadMoreWithNoMoreData();
                    }
                }
            } else
                AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        super.onNetWorkedFail(strings);
        if (mainSrf.getState() == RefreshState.Refreshing) {
            mainSrf.finishRefresh();
        }
        if (mainSrf.getState() == RefreshState.Loading) {
            mainSrf.finishLoadMore();
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (isEnd != 1) {
            page++;
            visitNet();
        } else {
            mainSrf.finishLoadMoreWithNoMoreData();
        }

    }

    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Dti/returnList", params, null, this, handler);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        lists.clear();
        visitNet();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, MeReleaseDetailsActivity.class);
        intent.putExtra("id", lists.get(position).getId());
        startActivity(intent);
    }
}
