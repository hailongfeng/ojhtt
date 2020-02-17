package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.ouju.htt.adapter.TokenAdapter;
import cn.ouju.htt.bean.TokenBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeTokenActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    TextView meFccTvPrice;
    TextView meFccTvChange;
    EditText meFccEtPrice;
    TextView meFccTvYue;
    TextView meFccTvSubmit;
    @BindView(R.id.me_fcc_lv)
    ListView meFccLv;
    @BindView(R.id.fcc_srf)
    SmartRefreshLayout fccSrf;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    private List<TokenBean> lists;
    private TokenAdapter adapter;
    private View headView;
    private int page = 1;
    private int isEnd;
    private LinearLayout mell;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_token);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.home_me)+" TOKEN", android.R.color.black);
    }

    @Override
    protected void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Token/getTokenAccount", params, null, this, handler);
        headView = LayoutInflater.from(this).inflate(R.layout.head_me_token, null);
        meFccTvPrice = headView.findViewById(R.id.me_fcc_tv_price);
        meFccTvChange = headView.findViewById(R.id.me_fcc_tv_change);
        meFccEtPrice = headView.findViewById(R.id.me_fcc_et_price);
        meFccTvYue = headView.findViewById(R.id.me_fcc_tv_yue);
        meFccTvSubmit = headView.findViewById(R.id.me_fcc_tv_submit);
        mell = headView.findViewById(R.id.me_fcc_ll);
        lists = new ArrayList<>();
        adapter = new TokenAdapter(lists, this);
        meFccLv.addHeaderView(headView);
        meFccLv.setAdapter(adapter);
        fccSrf.setOnRefreshLoadMoreListener(this);
        meFccTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String result = meFccEtPrice.getText().toString().trim();
                if (!TextUtils.isEmpty(result)) {
                    final EditText et = new EditText(MeTokenActivity.this);
                    et.setFocusable(true);
                    et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MeTokenActivity.this);
                    builder.setTitle(getString(R.string.input_safe_psw)).setView(et).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String psw = et.getText().toString().trim();
                            if (!TextUtils.isEmpty(psw)) {
                                Map<String, Object> params = new TreeMap<>();
                                params.put("dti", result);
                                params.put("user_no", UserUtils.getString("user_no", null));
                                params.put("password", SHAUtils.getSHA(psw));
                                CommonService.query("Token/dtiConvertToken", params, null, MeTokenActivity.this, handler);
                            } else
                                showShortToast(getString(R.string.input_safe_psw_hint));
                        }
                    }).show();
                } else {
                    showShortToast(getString(R.string.please_input_dti_num));
                }
            }
        });
        meFccEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    meFccTvSubmit.setAlpha(1f);
                    meFccTvSubmit.setEnabled(true);
                } else {
                    meFccTvSubmit.setAlpha(0.5f);
                    meFccTvSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        params.put("page", page);
        CommonService.query("Token/tokenRecordList", params, null, this, handler);
    }

    @Override
    @OnClick({R.id.common_ll_failure})
    public void onClick(View v) {
        if (v.getId() == R.id.common_ll_failure) {
            lists.clear();
            page = 1;
            visitNet();
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Token/tokenRecordList")) {
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<TokenBean> beans = jsonUtils.getEntityList("data", "data_list", new TokenBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                        }
                        if (lists == null || lists.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                            commonLlFailure.setVisibility(View.GONE);
                            mell.setVisibility(View.GONE);
                        } else {
                            mell.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        if (fccSrf.getState() == RefreshState.Loading) {
                            fccSrf.finishLoadMore();
                        }
                        if (fccSrf.getState() == RefreshState.Refreshing) {
                            fccSrf.finishRefresh();
                        }
                    } else if (t[0].equals("Token/getTokenAccount")) {
                        meFccTvPrice.setText(jsonUtils.getString("token_account", "data"));
                        meFccTvChange.setText(getString(R.string.single) + jsonUtils.getString("dti_convert", "data") + "DTI  "+getString(R.string.yes_change) + jsonUtils.getString("token_convert", "data") + "TOKEN");
                        meFccTvYue.setText(jsonUtils.getString("dti_account", "data"));
                        visitNet();
                    } else if (t[0].equals("Token/dtiConvertToken")) {
                        Intent broad = new Intent();
                        broad.setAction("android.intent.action.me");
                        sendBroadcast(broad);
                        showShortToast(getString(R.string.change_success));
                        meFccEtPrice.setText("");
                        Map<String, Object> params = new HashMap<>();
                        params.put("user_no", UserUtils.getString("user_no", null));
                        CommonService.query("Token/getTokenAccount", params, null, this, handler);
                        page = 1;
                        lists.clear();
                    }
                } else if (jsonUtils.getCode().equals("405")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
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

    @Override
    public void onNetWorkedFail(Object... strings) {
        super.onNetWorkedFail(strings);
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (isEnd != 1) {
            page++;
            visitNet();
        } else {
            fccSrf.finishLoadMoreWithNoMoreData();
        }

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        lists.clear();
        Map<String, Object> params = new HashMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Token/getTokenAccount", params, null, this, handler);
    }
}
