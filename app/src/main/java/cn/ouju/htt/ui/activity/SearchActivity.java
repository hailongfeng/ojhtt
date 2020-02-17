package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
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
import cn.ouju.htt.adapter.TradingAdapter;
import cn.ouju.htt.bean.BuySellBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;


public class SearchActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    @BindView(R.id.search_et_content)
    EditText searchEtContent;
    @BindView(R.id.search_tv_cancel)
    TextView searchTvCancel;
    @BindView(R.id.search_lv)
    ListView searchLv;
    @BindView(R.id.main_srf)
    SmartRefreshLayout MainSrf;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.common_ll_data_sorry)
    LinearLayout commonLlDataSorry;
    private int page = 1;
    private String type;
    private int isEnd;
    private List<BuySellBean> lists;
    private TradingAdapter adapter;
    private boolean b;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            searchEtContent.setHint(getString(R.string.search_seller_tel));
        } else if (type.equals("1")) {
            searchEtContent.setHint(getString(R.string.search_buyer_tel));
        }
    }

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        adapter = new TradingAdapter(lists, this);
        adapter.setBack(new TradingAdapter.CallBack() {
            @Override
            public void sellAndBuy(int position) {
                Map<String, Object> params = new HashMap<>();
                params.put("user_no", UserUtils.getString("user_no", null));
                params.put("trade_id", lists.get(position).getTrade_id());
                if (type.equals("0")) {
                    CommonService.query("Dti/saleDetail", params, null, SearchActivity.this, handler);
                } else if (type.equals("1")) {
                    CommonService.query("Dti/buyDetail", params, null, SearchActivity.this, handler);
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
                        commonLlDataSorry.setVisibility(View.GONE);
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
        params.put("type", type);
        params.put("page", page);
        params.put("phone", searchEtContent.getText().toString());
        CommonService.query("Dti/trade", params, null, SearchActivity.this, handler);

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    commonLlDataSorry.setVisibility(View.GONE);
                    commonLlFailure.setVisibility(View.GONE);
                    if (t[0].equals("Dti/trade")) {
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<BuySellBean> beans = jsonUtils.getEntityList("data", "data_list", new BuySellBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                            for (int i = 0; i < lists.size(); i++) {
                                lists.get(i).setType(type);
                            }
                        }
                        if (lists != null && lists.size() > 0) {
                            commonLlDataSorry.setVisibility(View.GONE);
                        } else {
                            commonLlDataSorry.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        if (MainSrf.getState() == RefreshState.Loading) {
                            MainSrf.finishLoadMore();
                        }
                        if (MainSrf.getState() == RefreshState.Refreshing) {
                            MainSrf.finishRefresh();
                        }
                        if (isEnd == 1) {
                            MainSrf.finishLoadMoreWithNoMoreData();
                        }
                    } else if (t[0].equals("Dti/saleDetail")) {
                        final String buy_uno = jsonUtils.getString("buy_uno", "data");
                        final String trade_id = jsonUtils.getString("trade_id", "data");
                        final EditText et = new EditText(this);
                        et.setFocusable(true);
                        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                    params.put("user_no", buy_uno);
                                    params.put("trade_id", trade_id);
                                    params.put("password", SHAUtils.getSHA(psw));
                                    CommonService.query("Dti/confirmBuy", params, null, SearchActivity.this, handler);
                                } else
                                    showShortToast(getString(R.string.input_safe_psw_hint));
                            }
                        }).show();
                    } else if (t[0].equals("Dti/buyDetail")) {
                        String buy_uno = jsonUtils.getString("buy_uno", "data");
                        String buy_phone = jsonUtils.getString("buy_phone", "data");
                        String sale_uno = jsonUtils.getString("sale_uno", "data");
                        String sale_phone = jsonUtils.getString("sale_phone", "data");
                        String sale_gfc = jsonUtils.getString("sale_gfc", "data");
                        String number = jsonUtils.getString("number", "data");
                        String amount = jsonUtils.getString("amount", "data");
                        String price = jsonUtils.getString("price", "data");
                        String trade_id = jsonUtils.getString("trade_id", "data");
                        Intent intent = new Intent();
                        intent.setClass(this, TradingToastActivity.class);
                        intent.putExtra("buy_uno", buy_uno);
                        intent.putExtra("buy_phone", buy_phone);
                        intent.putExtra("sale_uno", sale_uno);
                        intent.putExtra("sale_phone", sale_phone);
                        intent.putExtra("sale_gfc", sale_gfc);
                        intent.putExtra("trade_id", trade_id);
                        intent.putExtra("number", number);
                        intent.putExtra("amount", amount);
                        intent.putExtra("price", price);
                        startActivityForResult(intent, 10);
                    } else if (t[0].equals("Dti/confirmBuy")) {
                        b = true;
                        showShortToast(getString(R.string.buy_success));
                        lists.clear();
                        page = 1;
                        visitNet();
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
        super.onNetWorkedFail(strings);
        commonLlDataSorry.setVisibility(View.GONE);
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
        MainSrf.setEnableLoadMore(true);
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
        } else {
            MainSrf.finishLoadMoreWithNoMoreData();
        }
    }
}
