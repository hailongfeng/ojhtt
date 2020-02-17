package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.StoreOrderAdapter;
import cn.ouju.htt.bean.AddressBean;
import cn.ouju.htt.bean.StoreBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2018/2/1.
 */

public class StoresOrderActivity extends BaseActivity {

    @BindView(R.id.shopping_order_lv)
    ListView shoppingOrderLv;
    @BindView(R.id.me_shopping_cart_tv_total)
    TextView meShoppingCartTvTotal;
    @BindView(R.id.shopping_order_tv_pay)
    TextView shoppingOrderTvPay;
    private StoreOrderAdapter adapter;
    private List<StoreBean> beanList;
    private View head;
    private View foot;
    //---------head---------
    private LinearLayout orderTvAddressNone;
    private LinearLayout orderTvLlYes;
    private TextView orderTvName;
    private TextView orderTvTel;
    private TextView orderTvAddress;
    //-------foot----------
    private TextView orderTvWay;
    private EditText orderEtRemark;
    private TextView orderNum;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_store_order);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.confirm_order), android.R.color.black);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String, Object> params = new TreeMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        params.put("cart_ids", getIntent().getStringExtra("ids"));
        CommonService.query("Mall/orderConfirm", params, null, this, handler);
    }

    @Override
    protected void initData() {
        beanList = new ArrayList<>();
        adapter = new StoreOrderAdapter(beanList, this);
      /*  Map<String, Object> params = new TreeMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        params.put("cart_ids", getIntent().getStringExtra("ids"));
        CommonService.query("Mall/orderConfirm", params, null, this, handler);*/
        head = LayoutInflater.from(this).inflate(R.layout.store_order_head, null);
        orderTvAddressNone = head.findViewById(R.id.order_tv_address_none);
        orderTvLlYes = head.findViewById(R.id.order_tv_ll_yes);
        orderTvName = head.findViewById(R.id.order_tv_name);
        orderTvTel = head.findViewById(R.id.order_tv_tel);
        orderTvAddress = head.findViewById(R.id.order_tv_address);
        foot = LayoutInflater.from(this).inflate(R.layout.store_order_foot, null);
        orderTvWay = foot.findViewById(R.id.order_tv_way);
        orderEtRemark = foot.findViewById(R.id.order_et_remark);
        orderNum = foot.findViewById(R.id.order_tv_num);
        orderTvAddressNone.setOnClickListener(this);
        orderTvLlYes.setOnClickListener(this);
        shoppingOrderLv.addHeaderView(head, null, false);
        shoppingOrderLv.addFooterView(foot, null, false);
        shoppingOrderLv.setAdapter(adapter);
    }

    @Override
    @OnClick({R.id.shopping_order_tv_pay})
    public void onClick(View v) {
        if (v.getId() == R.id.shopping_order_tv_pay) {
            if (isAddress) {
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
                            Map<String, Object> params = new HashMap<>();
                            params.put("user_no", UserUtils.getString("user_no", null));
                            params.put("address_id", addressBean.getId());
                            params.put("password", SHAUtils.getSHA(et.getText().toString().trim()));
                            params.put("cart_ids", getIntent().getStringExtra("ids"));
                            String remark = orderEtRemark.getText().toString().trim();
                            if (!TextUtils.isEmpty(remark)) {
                                params.put("remark", remark);
                            }
                            CommonService.query("Mall/checkout", params, null, StoresOrderActivity.this, handler);
                        } else
                            showShortToast(getString(R.string.input_safe_psw_hint));
                    }
                }).show();
            } else {
                showShortToast(getString(R.string.add_address));
            }
        }
        if (v.getId() == R.id.order_tv_address_none) {
            Intent intent = new Intent(this, MeAddressActivity.class);
            intent.putExtra("isStart", "yes");
            startActivityForResult(intent, 10);
        }
        if (v.getId() == R.id.order_tv_ll_yes) {
            Intent intent = new Intent(this, MeAddressActivity.class);
            intent.putExtra("isStart", "yes");
            startActivityForResult(intent, 10);
        }
    }

    private String total;
    private boolean isAddress;

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils != null) {
                if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                    if (t[0].equals("Mall/orderConfirm")) {
                        List<StoreBean> beans = jsonUtils.getEntityList("data", "cart_list", new StoreBean());
                        List<AddressBean> addressBeans = jsonUtils.getEntityList("data", "address_list", new AddressBean());
                        String price = jsonUtils.getString("shipping", "data");
                        if (!TextUtils.isEmpty(price)) {
                            if (Integer.parseInt(price) > 0) {
                                orderTvWay.setText(jsonUtils.getString("shipping_mode", "data") + price + "DTI");
                            } else {
                                orderTvWay.setText(jsonUtils.getString("shipping_mode", "data"));
                            }
                        } else {
                            orderTvWay.setText(jsonUtils.getString("shipping_mode", "data"));
                        }
                        orderNum.setText(UserUtils.getString("user_no", null));
                        beanList.clear();
                        if (beans != null && beans.size() > 0) {
                            beanList.addAll(beans);
                        }
                        adapter.notifyDataSetChanged();
                        if (addressBeans != null && addressBeans.size() > 0) {
                            isAddress = true;
                            addressBean = addressBeans.get(0);
                            orderTvAddressNone.setVisibility(View.GONE);
                            orderTvLlYes.setVisibility(View.VISIBLE);
                            orderTvName.setText(addressBean.getReceiver());
                            orderTvTel.setText(addressBean.getPhone());
                            orderTvAddress.setText(addressBean.getAddress());
                        } else {
                            isAddress = false;
                            orderTvAddressNone.setVisibility(View.VISIBLE);
                            orderTvLlYes.setVisibility(View.GONE);
                        }
                        total = jsonUtils.getString("total", "data");
                        meShoppingCartTvTotal.setText(df.format(Double.parseDouble(total)));
                    } else if (t[0].equals("Mall/checkout")) {
                        Intent intent = new Intent();
                        intent.setClass(this, PaySuccessActivity.class);
                        String total = jsonUtils.getString("total", "data");
                        intent.putExtra("amount", total);
                        startActivity(intent);
                    }
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

    private DecimalFormat df = new DecimalFormat("#.00");
    private AddressBean addressBean;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == 20) {
              /*  Map<String, Object> params = new TreeMap<>();
                params.put("user_no", UserUtils.getString("user_no", null));
                params.put("cart_ids", getIntent().getStringExtra("ids"));
                CommonService.query("Mall/orderConfirm", params, null, this, handler);*/
            }
            /*    if (data != null) {
                    addressBean = (AddressBean) data.getSerializableExtra("address");
                    if (addressBean != null) {
                        isAddress = true;
                        orderTvAddressNone.setVisibility(View.GONE);
                        orderTvLlYes.setVisibility(View.VISIBLE);
                        orderTvName.setText(addressBean.getReceiver());
                        orderTvTel.setText(addressBean.getPhone());
                        orderTvAddress.setText(addressBean.getAddress());
                    }
                }else {
                    isAddress = false;
                    orderTvAddressNone.setVisibility(View.VISIBLE);
                    orderTvLlYes.setVisibility(View.GONE);
                }
            }*/
        }
    }

}
