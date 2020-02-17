package cn.ouju.htt.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.TradingAdapter;
import cn.ouju.htt.bean.BuySellBean;
import cn.ouju.htt.bean.RangeBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.ui.activity.BaseActivity;
import cn.ouju.htt.ui.activity.LoginActivity;
import cn.ouju.htt.ui.activity.SearchActivity;
import cn.ouju.htt.ui.activity.TradingToastActivity;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

public class BuyFragment extends BaseFragment implements OnRefreshLoadMoreListener {
    private static HttpListener listeners;
    private static BaseActivity.MyHandler handlers;
    @BindView(R.id.trading_lv)
    ListView tradingLv;
    TextView tradingTvFix;
    LinearLayout tradingLlFix;
    @BindView(R.id.main_srf)
    SmartRefreshLayout MainSrf;
    @BindView(R.id.trading_title)
    TextView tradingTitle;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll)
    LinearLayout commonLl;
    @BindView(R.id.common_ll_failure_public)
    LinearLayout commonLlFailurePublic;
    @BindView(R.id.common_ll_failure_scroll)
    ScrollView commonLlFailureScroll;
    private List<BuySellBean> lists;
    private TradingAdapter adapter;
    private View headView;
    private EditText price, num;
    private TextView btn, search;
    private int page = 1;
    private int isEnd;
    private TextView current;
    private LineChart lineChart;
    private CountDownTimer timer;
    private TextView get;

    public static BuyFragment getInstance(HttpListener listener, BaseActivity.MyHandler handler) {
        BuyFragment buyFragment = new BuyFragment();
        listeners = listener;
        handlers = handler;
        return buyFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            page = 1;
            lists.clear();
            visitNet();
            CommonService.query("Dti/price", null, null, listeners, handlers);
        }
    }


    @Override
    protected void initView() {
        visitNet();
        setContentView(R.layout.fragment_trading);
        tradingTitle.setText(getResources().getString(R.string.home_buy));
        CommonService.query("Dti/price", null, null, listeners, handlers);
        MainSrf.setOnRefreshLoadMoreListener(this);
        tradingLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 1);
        params.put("page", page);
        CommonService.query("Dti/trade", params, null, listeners, handlers);
    }

    List<String> date = new ArrayList<>();
    List<Float> score = new ArrayList<>();//图表的数据点

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        headView = LayoutInflater.from(getContext()).inflate(R.layout.head_trading, null);
        tradingTvFix = headView.findViewById(R.id.trading_tv_fix);
        tradingLlFix = headView.findViewById(R.id.trading_ll_fix);
        current = headView.findViewById(R.id.trading_head_tv_current);
        search = headView.findViewById(R.id.trading_et_head_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("type", "1");
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        price = headView.findViewById(R.id.trading_et_head_price);
        num = headView.findViewById(R.id.trading_et_head_num);
        btn = headView.findViewById(R.id.trading_btn);
        lineChart = headView.findViewById(R.id.trading_chart);
        btn.setOnClickListener(this);
        adapter = new TradingAdapter(lists, getContext());
        adapter.setBack(new TradingAdapter.CallBack() {
            @Override
            public void sellAndBuy(final int position) {
                if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("trade_id", lists.get(position).getTrade_id());
                    CommonService.query("Dti/buyDetail", params, null, listeners, handlers);

                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        tradingLv.addHeaderView(headView, null, false);
        tradingLv.setAdapter(adapter);
        lineChart.getDescription().setEnabled(false);
        lineChart.setNoDataText(getString(R.string.data_null));
        lineChart.setDrawGridBackground(false);
        lineChart.setScaleEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateXY(1000, 100);
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    @OnClick({R.id.common_ll_failure_public})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_ll_failure_public:
                page = 1;
                lists.clear();
                visitNet();
                CommonService.query("Dti/price", null, null, listeners, handlers);
                break;
            case R.id.trading_btn:
                if (TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                final String pr = price.getText().toString().trim();
                final String n = num.getText().toString().trim();
                if (!TextUtils.isEmpty(n)) {
                    if (!TextUtils.isEmpty(pr)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.alert_custom_content, null);
                        get = v.findViewById(R.id.mining_toast_tv_sell_get);
                        final EditText captcha = v.findViewById(R.id.mining_toast_tv_sell_captcha);
                        final EditText psw = v.findViewById(R.id.mining_toast_tv_sell_security);
                        Button btn = v.findViewById(R.id.mining_toast_tv_sell_btn);
                        final Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(v);
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                timer.cancel();
                            }
                        });
                        timer = new CountDownTimer(60 * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                get.setText(millisUntilFinished / 1000 + "s");
                            }

                            @Override
                            public void onFinish() {
                                GradientDrawable drawable = (GradientDrawable) get.getBackground();
                                drawable.setStroke(1, ContextCompat.getColor(getActivity(), R.color.colorBtn));
                                get.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBtn));
                                get.setText(getString(R.string.register_get_captcha));
                                get.setEnabled(true);
                            }
                        };
                        get.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Object> params = new TreeMap<>();
                                params.put("phone", UserUtils.getString("phone", null));
                                params.put("type", 2);
                                String sms = UserUtils.getString("sms_code", null);
                                if (!TextUtils.isEmpty(sms)) {
                                    params.put("sms", sms);
                                } else
                                    params.put("sms", "86");

                                CommonService.query("Home/getCode", params, null, BuyFragment.this, handlers);
                            }
                        });
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String c = captcha.getText().toString().trim();
                                String p = psw.getText().toString().trim();
                                if (!TextUtils.isEmpty(c)) {
                                    if (!TextUtils.isEmpty(p)) {
                                        dialog.dismiss();
                                        Map<String, Object> params = new HashMap<>();
                                        params.put("user_no", UserUtils.getString("user_no", null));
                                        params.put("number", n);
                                        params.put("price", pr);
                                        params.put("code", c);
                                        params.put("password", SHAUtils.getSHA(p));
                                        CommonService.query("Dti/buy", params, null, listeners, handlers);
                                    } else
                                        showShortToast(getString(R.string.safe_psw_exist));
                                } else {
                                    showShortToast(getString(R.string.captcha_no));
                                }

                            }
                        });

                    } else {
                        showShortToast(getString(R.string.price_exist));

                    }
                } else {
                    showShortToast(getString(R.string.apply_num_no));
                }
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Dti/price")) {
                        commonLlFailureScroll.setVisibility(View.VISIBLE);
                        commonLlFailurePublic.setVisibility(View.GONE);
                        RangeBean bean = jsonUtils.getEntity("data", new RangeBean());
                        List<RangeBean.PriceListBean> priceListBeans = bean.getPrice_list();
                        if (priceListBeans != null && priceListBeans.size() > 0) {
                            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date.clear();
                            score.clear();
                            for (int i = 0; i < priceListBeans.size(); i++) {
                                try {
                                    Date d = format.parse(priceListBeans.get(i).getAdd_time());
                                    date.add(new SimpleDateFormat("MM-dd").format(d));
                                    score.add(Float.parseFloat(priceListBeans.get(i).getPrice()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setTextColor(Color.BLACK);
                            xAxis.setTextSize(12);
                            xAxis.enableGridDashedLine(10f, 10f, 0f);
                            xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
                            xAxis.setLabelCount(score.size(), true);
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    return date.get((int) value % date.size());
                                }
                            });
                            YAxis yAxis = lineChart.getAxisLeft();
                            yAxis.setDrawAxisLine(true);
                            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                            yAxis.setDrawGridLines(true);
                            yAxis.setTextColor(Color.BLACK);
                            yAxis.setTextSize(12);
                            yAxis.setAxisMinimum(0.0f);
                            yAxis.setAxisMaximum(Float.parseFloat(bean.getMax_price())+5);
                            final DecimalFormat format1 = new DecimalFormat("######0.00");
                            yAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    return format1.format(value);
                                }
                            });
                            List<Entry> entries = new ArrayList<>();
                            for (int i = 0; i < score.size(); i++) {
                                entries.add(new Entry(i, score.get(i)));
                            }
                            LineDataSet lineDataSet = new LineDataSet(entries, null);
                            lineDataSet.setValueFormatter(new IValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                    return format1.format(value);
                                }
                            });
                            lineDataSet.setColor(Color.parseColor("#00aae1"));
                            lineDataSet.setCircleColor(getResources().getColor(R.color.colorBtn));
                            //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                            lineDataSet.setValueTextSize(10f);
                            LineData data = new LineData(lineDataSet);
                            lineChart.setData(data);
                            lineChart.invalidate();
                            price.setHint(getString(R.string.min) + bean.getMin_price() + "," + getString(R.string.max) + bean.getMax_price());
                            current.setText(bean.getCurr_price() + "$");
                        }
                    } else if (t[0].equals("Dti/trade")) {
                        commonLl.setVisibility(View.GONE);
                        commonLlDataNull.setVisibility(View.GONE);
                        tradingLlFix.setVisibility(View.VISIBLE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<BuySellBean> beans = jsonUtils.getEntityList("data", "data_list", new BuySellBean());
                        int title = jsonUtils.getInt("data", "dont_trade");
                        if (title == 1) {
                            tradingTitle.setText(getString(R.string.buy_closed));
                        } else {
                            tradingTitle.setText(getString(R.string.home_buy));
                        }
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                        }
                        if (lists == null || lists.size() <= 0) {
                            tradingLlFix.setVisibility(View.GONE);
                            commonLl.setVisibility(View.VISIBLE);
                            commonLlDataNull.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < lists.size(); i++) {
                            lists.get(i).setType("1");
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
                        intent.setClass(getContext(), TradingToastActivity.class);
                        intent.putExtra("buy_uno", buy_uno);
                        intent.putExtra("buy_phone", buy_phone);
                        intent.putExtra("sale_uno", sale_uno);
                        intent.putExtra("sale_phone", sale_phone);
                        intent.putExtra("sale_gfc", sale_gfc);
                        intent.putExtra("trade_id", trade_id);
                        intent.putExtra("number", number);
                        intent.putExtra("amount", amount);
                        intent.putExtra("price", price);
                        intent.putExtra("type", "buy");
                        startActivityForResult(intent, 10);
                    } else if (t[0].equals("Dti/buy")) {
                        num.setText("");
                        price.setText("");
                        showShortToast(getString(R.string.issue_success));
                        lists.clear();
                        page = 1;
                        visitNet();
                    } else if (t[0].equals("Home/getCode")) {
                        showShortToast(getString(R.string.captcha_send_success));
                        timer.start();
                        get.setEnabled(false);
                        GradientDrawable drawable = (GradientDrawable) get.getBackground();
                        drawable.setStroke(1, ContextCompat.getColor(getContext(), R.color.colorGrayText));
                        get.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGrayText));
                    }
                } else if (jsonUtils.getCode().equals("405")) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else
                    AlertDialogUtils.showMsg(getContext(), jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        if (MainSrf.getState() == RefreshState.Loading) {
            MainSrf.finishLoadMore();
        }
        if (MainSrf.getState() == RefreshState.Refreshing) {
            MainSrf.finishRefresh();
        }
        if (isEnd == 1) {
            MainSrf.finishLoadMoreWithNoMoreData();
        }
        commonLlFailureScroll.setVisibility(View.GONE);
        commonLlFailurePublic.setVisibility(View.VISIBLE);
        showShortToast(getResources().getString(R.string.network_fail));
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        lists.clear();
        visitNet();
        CommonService.query("Dti/price", null, null, listeners, handlers);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == 20) {
                page = 1;
                lists.clear();
                visitNet();
            }
        }
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
