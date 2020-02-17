package cn.ouju.htt.ui.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.OrderDetailsAdapter;
import cn.ouju.htt.bean.GoodsListBean;
import cn.ouju.htt.bean.OrderBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderItemDetailsActivity extends BaseActivity {


    @BindView(R.id.shopping_lv)
    ListView shoppingLv;
    @BindView(R.id.home_srl)
    SmartRefreshLayout homeSrl;

    private LinearLayout orderDetailsLl;
    private OrderDetailsAdapter adapter;
    private List<GoodsListBean> beanList;
    private View head;
    private LinearLayout orderTvLlYes;
    private TextView orderTvName;
    private TextView orderTvTel;
    private TextView orderTvAddress;
    private View foot;
    private TextView orderDetailsTvPrice;
    private TextView orderDetailsTvNum;
    private TextView orderDetailsTvXdbh;
    private TextView orderDetailsTvTime;
    private TextView orderDetailsTvWay;
    private TextView orderDetailsTvRemark;
    private TextView orderDetailsTvtotal;
    private LinearLayout orderDetailsLlReject;
    private TextView orderDetailsRejectTime;
    private TextView orderDetailsReject;
    private TextView orderDetailsRejectFix;

    @Override
    protected void initView() {
        setChildContentView(R.layout.common_fragment_tab_list);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.order_details), android.R.color.black);
    }

    @Override
    protected void initData() {
        homeSrl.setEnableRefresh(false);
        homeSrl.setEnableLoadMore(false);
        shoppingLv.setDividerHeight(2);
        beanList = new ArrayList<>();
        adapter = new OrderDetailsAdapter(beanList, this);
        Map<String, Object> params = new TreeMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        String id = getIntent().getStringExtra("id");
        params.put("order_id", getIntent().getStringExtra("id"));
        CommonService.query("Mall/orderDetail", params, null, this, handler);
        head = LayoutInflater.from(this).inflate(R.layout.store_order_head, null);
        orderTvLlYes = head.findViewById(R.id.order_tv_ll_yes);
        orderTvLlYes.setVisibility(View.VISIBLE);
        orderTvName = head.findViewById(R.id.order_tv_name);
        orderTvTel = head.findViewById(R.id.order_tv_tel);
        orderTvAddress = head.findViewById(R.id.order_tv_address);
        TextView arrow = head.findViewById(R.id.order_tv_arrow);
        arrow.setVisibility(View.GONE);
        foot = LayoutInflater.from(this).inflate(R.layout.shopping_order_details_foot, null);
        orderDetailsTvPrice = foot.findViewById(R.id.order_details_tv_price);
        orderDetailsTvNum = foot.findViewById(R.id.order_details_tv_order_id);
        orderDetailsLl = foot.findViewById(R.id.order_details_ll);
        orderDetailsTvXdbh = foot.findViewById(R.id.order_details_tv_xdbh);
        orderDetailsTvTime = foot.findViewById(R.id.order_details_tv_time);
        orderDetailsTvtotal = foot.findViewById(R.id.order_details_tv_num);
        orderDetailsTvWay = foot.findViewById(R.id.order_details_tv_way);
        orderDetailsTvRemark = foot.findViewById(R.id.order_details_tv_remark);
        orderDetailsLlReject = foot.findViewById(R.id.order_details__reject_ll);
        orderDetailsRejectTime = foot.findViewById(R.id.order_details_tv_reject_time);
        orderDetailsReject = foot.findViewById(R.id.order_details_tv_reject);
        orderDetailsRejectFix = foot.findViewById(R.id.order_details_tv_reject_fix);
        shoppingLv.addHeaderView(head, null, false);
        shoppingLv.addFooterView(foot, null, false);
        shoppingLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils != null) {
                if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                    if (t[0].equals("Mall/orderDetail")) {
                        OrderBean info = jsonUtils.getEntity("data", new OrderBean());
                        if (info != null) {
                            orderTvName.setText(info.getReceiver());
                            orderTvTel.setText(info.getReceiver_phone());
                            orderTvAddress.setText(info.getReceiver_address());
                            orderDetailsTvPrice.setText(info.getTotal());
                            orderDetailsTvNum.setText(info.getOrder_no());
                            orderDetailsTvtotal.setText("" + info.getGoods_list().size());
                            String str = info.getStatus();
                            if (str.equals("0")) {
                                orderDetailsLlReject.setVisibility(View.GONE);
                            } else if (str.equals("1")) {
                                orderDetailsLlReject.setVisibility(View.VISIBLE);
                                orderDetailsRejectTime.setText(info.getOperate_time());
                                orderDetailsReject.setText(info.getOperate_remark());
                                orderDetailsRejectFix.setText(getString(R.string.logistics_information));
                            } else if (str.equals("2")) {
                                orderDetailsLlReject.setVisibility(View.VISIBLE);
                                orderDetailsRejectTime.setText(info.getOperate_time());
                                orderDetailsReject.setText(info.getOperate_remark());
                                orderDetailsRejectFix.setText(getString(R.string.reject_dispose));
                            }
                            orderDetailsTvXdbh.setText(info.getUser_no());
                            orderDetailsTvTime.setText(info.getAdd_time());
                            String shipping = info.getShipping_set();
                            if (shipping.equals("1")) {
                                orderDetailsTvWay.setText(getString(R.string.freight_collect));
                            } else if (shipping.equals("0")) {
                                orderDetailsTvWay.setText(getString(R.string.pinkage));
                            } else if (shipping.equals("2")) {
                                orderDetailsTvWay.setText(getString(R.string.payroll) + df.format(Double.parseDouble(info.getShipping())) + "DTI");
                            }
                            if (!TextUtils.isEmpty(info.getRemark())) {
                                orderDetailsTvRemark.setText(info.getRemark());
                            } else {
                                orderDetailsTvRemark.setText(getString(R.string.data_empty));
                            }
                            List<GoodsListBean> beans = info.getGoods_list();
                            if (beans != null && beans.size() > 0) {
                                for (int i = 0; i < beans.size(); i++) {
                                    beans.get(i).setStatus(info.getStatus());
                                }
                                beanList.addAll(beans);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            } else if (Integer.parseInt(jsonUtils.getCode()) >= 600) {
                AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
            } else {
                showShortToast(jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {
    }

    private DecimalFormat df = new DecimalFormat("#.00");

}
