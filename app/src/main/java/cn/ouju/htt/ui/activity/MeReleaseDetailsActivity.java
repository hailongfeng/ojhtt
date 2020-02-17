package cn.ouju.htt.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
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
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.ReleaseDetailsAdapter;
import cn.ouju.htt.bean.ReleaseDetailsBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeReleaseDetailsActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.me_send_lv)
    ListView meSendLv;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.main_srf)
    SmartRefreshLayout mainSrf;
    private int page = 1;
    private int isEnd;
    private List<ReleaseDetailsBean> list;
    private ReleaseDetailsAdapter adapter;

    @Override
    protected void initView() {
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.release_details), android.R.color.black);
        setChildContentView(R.layout.activity_me_send_email);
    }

    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("market_id", getIntent().getStringExtra("id"));
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Dti/returnDetail", params, null, this, handler);
    }

    @Override
    protected void initData() {
        visitNet();
        list = new ArrayList<>();
        adapter = new ReleaseDetailsAdapter(list, this);
        meSendLv.setAdapter(adapter);
        mainSrf.setOnRefreshLoadMoreListener(this);
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
                    if (t[0].equals("Dti/returnDetail")) {
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<ReleaseDetailsBean> beans = jsonUtils.getEntityList("data", "data_list", new ReleaseDetailsBean());
                        if (beans != null && beans.size() > 0) {
                            list.addAll(beans);
                        }
                        adapter.notifyDataSetChanged();
                        if (list != null && list.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                            commonLlFailure.setVisibility(View.GONE);
                        }
                        if (mainSrf.getState() == RefreshState.Loading) {
                            mainSrf.finishLoadMore();
                        }
                        if (mainSrf.getState() == RefreshState.Refreshing) {
                            mainSrf.finishRefresh();
                        }
                        if (isEnd == 1) {
                            mainSrf.finishLoadMoreWithNoMoreData();
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
        if (mainSrf.getState() == RefreshState.Loading) {
            mainSrf.finishLoadMore();
        }
        if (mainSrf.getState() == RefreshState.Refreshing) {
            mainSrf.finishRefresh();
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        list.clear();
        page = 1;
        visitNet();
    }
}