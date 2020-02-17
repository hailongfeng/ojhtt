package cn.ouju.htt.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.ouju.htt.adapter.StoreAdapter;
import cn.ouju.htt.bean.StoreBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;


public class SearchStoreActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.search_et_content)
    EditText searchEtContent;
    @BindView(R.id.search_tv_cancel)
    TextView searchTvCancel;
    @BindView(R.id.search_lv)
    ListView searchLv;
    @BindView(R.id.main_srf)
    SmartRefreshLayout MainSrf;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.common_tv_null)
    TextView commonTvNull;
    private int page = 1;
    private int isEnd;
    private List<StoreBean> lists;
    private StoreAdapter adapter;
    private boolean b;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_store_search);
        ButterKnife.bind(this);
        commonTvNull.setText(getString(R.string.search_empty));
        searchEtContent.setHint(getString(R.string.search_store));
    }

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        adapter = new StoreAdapter(lists, this);
        adapter.setCall(new StoreAdapter.CallBack() {
            @Override
            public void setInt(int position, int num) {
                lists.get(position).setNum(num);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void join(int position) {
                if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                    Map<String, Object> params = new TreeMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("goods_id", lists.get(position).getId());
                    if (lists.get(position).getNum() <= 1) {
                        params.put("quantity", 1);
                    } else {
                        params.put("quantity", lists.get(position).getNum());
                    }
                    CommonService.query("Mall/addCart", params, null, SearchStoreActivity.this, handler);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(SearchStoreActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        searchLv.setAdapter(adapter);
        MainSrf.setOnRefreshLoadMoreListener(this);

        searchEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = v.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        commonLlDataNull.setVisibility(View.GONE);
                        lists.clear();
                        visitNet();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } else {
                        showShortToast(getString(R.string.input_search_content));
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    @OnClick({R.id.search_tv_cancel, R.id.common_ll_failure})
    public void onClick(View v) {
        if (v.getId() == R.id.search_tv_cancel) {
            finish();
        }
        if (v.getId() == R.id.common_ll_failure) {
            page = 1;
            lists.clear();
            visitNet();
        }
    }


    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(searchEtContent.getText().toString().trim())) {
            params.put("keyword", searchEtContent.getText().toString());
        }
        params.put("page", page);
        CommonService.query("Mall/goods", params, null, this, handler);

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    commonLlDataNull.setVisibility(View.GONE);
                    commonLlFailure.setVisibility(View.GONE);
                    if (t[0].equals("Mall/goods")) {
                        lists.clear();
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<StoreBean> beans = jsonUtils.getEntityList("data", "data_list", new StoreBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                        }
                        if (lists == null || lists.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        if (MainSrf.getState() == RefreshState.Refreshing) {
                            MainSrf.finishRefresh();
                        }
                        if (MainSrf.getState() == RefreshState.Loading) {
                            MainSrf.finishLoadMore();
                        }
                        if (isEnd == 1) {
                            MainSrf.finishLoadMoreWithNoMoreData();
                        }
                    }else if (t[0].equals("Mall/addCart")) {
                        Toast.makeText(this, getString(R.string.store_add_success), Toast.LENGTH_SHORT).show();
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
    public void onNetWorkedFail(Object... strings) {
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
        if (MainSrf.getState() == RefreshState.Loading) {
            MainSrf.finishLoadMore();
        }
        if (MainSrf.getState() == RefreshState.Refreshing) {
            MainSrf.finishRefresh();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        lists.clear();
        visitNet();
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == 20) {
                b = true;
                page = 1;
                lists.clear();
                visitNet();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (b) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                setResult(20);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        if (isEnd != 1) {
            visitNet();
        }else{
            MainSrf.finishLoadMoreWithNoMoreData();
        }
    }
}
