package cn.ouju.htt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.CommonOrderAdapter;
import cn.ouju.htt.bean.OrderBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.ui.activity.OrderItemDetailsActivity;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2017/10/25.
 */

public class OrderRejectFragment extends MyLazyFragment {
    private static HttpListener httpListener;
    private static Handler handlers;
    @BindView(R.id.shopping_lv)
    ListView shoppingLv;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.home_srl)
    SmartRefreshLayout homeSrl;
    private boolean isFirstLoad = false;
    private boolean isPrepared;
    private int page = 1;
    private int isEnd;
    private List<OrderBean> orderBeans;
    private CommonOrderAdapter adapter;

    public static OrderRejectFragment newInstance(HttpListener listener, Handler handler) {
        OrderRejectFragment allEarningsFragment = new OrderRejectFragment();
        httpListener = listener;
        handlers = handler;
        return allEarningsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_tab_list, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        initView();
        return view;
    }


    private void firstLoad() {
        Map<String, Object> params = new TreeMap<>();
        params.put("status", "2");
        params.put("user_no", UserUtils.getString("user_no", null));
        params.put("page", page);
        CommonService.query("Mall/order", params, null, httpListener, handlers);
    }

    private void initView() {
        orderBeans = new ArrayList<>();
        adapter = new CommonOrderAdapter(orderBeans, getContext());
        shoppingLv.setAdapter(adapter);
        homeSrl.setOnRefreshLoadMoreListener(this);
        shoppingLv.setOnItemClickListener(this);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void onVisible() {
        if (!isPrepared || !isVisible) {
            return;
        }
        if (!isFirstLoad) {
            isFirstLoad = true;
            firstLoad();
        }
    }


    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                    if (t[0].equals("Mall/order")) {
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<OrderBean> beans = jsonUtils.getEntityList("data", "data_list", new OrderBean());
                        if (beans != null && beans.size() > 0) {
                            orderBeans.addAll(beans);
                            adapter.notifyDataSetChanged();
                        }
                        if (orderBeans == null || orderBeans.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                        }
                        if (homeSrl.getState() == RefreshState.Refreshing) {
                            homeSrl.finishRefresh();
                        }
                        if (homeSrl.getState() == RefreshState.Loading) {
                            homeSrl.finishLoadMore();
                        }
                        if (isEnd == 1) {
                            homeSrl.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else if (Integer.parseInt(jsonUtils.getCode()) >= 600) {
                    AlertDialogUtils.showMsg(getContext(), jsonUtils.getMsg());
                } else {
                    Toast.makeText(getContext(), jsonUtils.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        if (homeSrl.getState() == RefreshState.Refreshing) {
            homeSrl.finishRefresh();
        }
        if (homeSrl.getState() == RefreshState.Loading) {
            homeSrl.finishLoadMore();
        }
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

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
        orderBeans.clear();
        firstLoad();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), OrderItemDetailsActivity.class);
        intent.putExtra("id", orderBeans.get(position).getOrder_id());
        startActivity(intent);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (isEnd != 1) {
            page++;
            firstLoad();
        }else {
            homeSrl.finishLoadMoreWithNoMoreData();
        }
    }
}
