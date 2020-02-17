package cn.ouju.htt.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.MiningAdapter;
import cn.ouju.htt.bean.MiningBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeMiningActivity extends BaseActivity {
    @BindView(R.id.mining_tv_price)
    TextView miningTvPrice;
    @BindView(R.id.mining_tv_description)
    TextView miningTvDescription;
    @BindView(R.id.mining_ll_log)
    LinearLayout miningLlLog;
    @BindView(R.id.shopping_lv)
    ListView shoppingLv;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.home_srl)
    SmartRefreshLayout homeSrl;
    private List<MiningBean> lists;
    private MiningAdapter adapter;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_mining);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.me_mining), android.R.color.black);
    }

    @Override
    protected void initData() {
        homeSrl.setEnableLoadMore(false);
        homeSrl.setEnableRefresh(false);
        Map<String, Object> params = new HashMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Home/sneak", params, null, this, handler);
        lists = new ArrayList<>();
        adapter = new MiningAdapter(lists, this);
        shoppingLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils.getCode().equals("200")) {
                if (t[0].equals("Home/sneak")) {
                    commonLlDataNull.setVisibility(View.GONE);
                    commonLlFailure.setVisibility(View.GONE);
                    String type = jsonUtils.getString("type", "data");
                    miningTvPrice.setText(jsonUtils.getString("amount","data"));
                    if (type.equals("no")) {
                        miningTvDescription.setText(getString(R.string.mining_register_out));
                        miningLlLog.setVisibility(View.GONE);
                    } else if (type.equals("had")) {
                        miningLlLog.setVisibility(View.VISIBLE);
                        miningTvDescription.setText(getString(R.string.mining_success));
                        List<MiningBean> beans = jsonUtils.getEntityList("data", "data_list", new MiningBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                        }
                        adapter.notifyDataSetChanged();
                        if (lists == null || lists.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                            commonLlFailure.setVisibility(View.GONE);
                        }
                    } else if (type.equals("has")) {
                        miningTvDescription.setText(getString(R.string.mining_register_in));
                        miningLlLog.setVisibility(View.GONE);
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

}
