package cn.ouju.htt.ui.activity;


import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.MeTeamAdapter;
import cn.ouju.htt.bean.TeamListBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeTeamActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    List<TeamListBean> lists;
    @BindView(R.id.me_send_lv)
    ListView meSendLv;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.main_srf)
    SmartRefreshLayout mainSrf;
    MeTeamAdapter adapter;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_send_email);
        setTitle(getString(R.string.me_team), android.R.color.black);
    }

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        adapter = new MeTeamAdapter(lists, this);
        meSendLv.setAdapter(adapter);
        Map<String, Object> params = new HashMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Me/recommendList", params, null, this, handler);
    }

    @Override
    @OnClick({R.id.common_ll_failure})
    public void onClick(View v) {
        if (v.getId() == R.id.common_ll_failure) {
            lists.clear();
            Map<String, Object> params = new HashMap<>();
            params.put("user_no", UserUtils.getString("user_no", null));
            CommonService.query("Me/recommendList", params, null, this, handler);
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    commonLlDataNull.setVisibility(View.GONE);
                    commonLlFailure.setVisibility(View.GONE);
                    List<TeamListBean> listBeans = jsonUtils.getEntityList("data", new TeamListBean());
                    if (listBeans != null && listBeans.size() > 0) {
                        lists.addAll(listBeans);
                    }
                    if (lists != null && lists.size() <= 0) {
                        commonLlDataNull.setVisibility(View.VISIBLE);
                        commonLlFailure.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else if (jsonUtils.getCode().equals("405")) {

                } else
                    AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        super.onNetWorkedFail(strings);
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
     /*   if (flag == 0) {
            if (lists.get(position).getChildren() != null && lists.get(position).getChildren().size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("item", lists.get(position));
                StartActivity(MeTeamDetailsActivity.class, params);
            }
        }*/
    }

}
