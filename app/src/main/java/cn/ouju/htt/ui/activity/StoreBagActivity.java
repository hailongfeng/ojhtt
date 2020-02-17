package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.ShoppingCartAdapter;
import cn.ouju.htt.bean.StoreBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2018/1/30.
 */

public class StoreBagActivity extends BaseActivity {
    @BindView(R.id.me_shopping_cart_smlv)
    ListView meShoppingCartSmlv;
    @BindView(R.id.me_shopping_cart_iv_icon)
    ImageView meShoppingCartIvIcon;
    @BindView(R.id.me_shopping_cart_tv_text)
    TextView meShoppingCartTvText;
    @BindView(R.id.me_shopping_cart_select)
    LinearLayout meShoppingCartSelect;
    @BindView(R.id.me_shopping_cart_tv_total)
    TextView meShoppingCartTvTotal;
    @BindView(R.id.me_shopping_cart_tv_num)
    TextView meShoppingCartTvNum;
    @BindView(R.id.me_shopping_cart_ll)
    LinearLayout meShoppingCartLl;
    @BindView(R.id.me_shopping_cart_ll_total)
    LinearLayout meShoppingCartLlTotal;
    @BindView(R.id.me_shopping_cart_ll_num)
    LinearLayout meShoppingCartLlNum;
    @BindView(R.id.me_shopping_cart_tv_num_fix)
    TextView meShoppingCartTvNumFix;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    private List<StoreBean> beanList;
    private ShoppingCartAdapter adapter;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_store_bag);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.store_car), android.R.color.black);
        setRightMenu(getString(R.string.edit), android.R.color.black, 0, this);
        Map<String, Object> params = new TreeMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Mall/cart", params, null, this, handler);

    }

    private int count;
    private int position;

    @Override
    protected void initData() {
        beanList = new ArrayList<>();
        adapter = new ShoppingCartAdapter(beanList, this);
        meShoppingCartSmlv.setAdapter(adapter);
        adapter.setCall(new ShoppingCartAdapter.CallBack() {
            @Override
            public void setInt(int position, int num) {
                beanList.get(position).setQuantity("" + num);
                adapter.notifyDataSetChanged();
                Map<String, Object> params = new TreeMap<>();
                params.put("user_no", UserUtils.getString("user_no", null));
                params.put("goods_id", beanList.get(position).getGoods_id());
                params.put("quantity", num);
                CommonService.query("Mall/modifyCart", params, null, StoreBagActivity.this, handler);
                StoreBagActivity.this.position = position;
            }


            @Override
            public void setSelect(int position) {
                if (isEdit == 1) {
                    List<String> errors = beanList.get(position).getErrors();
                    if (errors == null || errors.size() <= 0) {
                        count = 0;
                        int i = beanList.get(position).getNum();
                        if (i == 10) {
                            beanList.get(position).setNum(20);
                        } else
                            beanList.get(position).setNum(10);
                        total = 0;
                        for (int j = 0; j < beanList.size(); j++) {
                            if (beanList.get(j).getNum() == 10) {
                                count++;
                                total += Double.parseDouble(beanList.get(j).getNew_price()) * Double.parseDouble(beanList.get(j).getQuantity());
                            }
                        }
                        if (count != beanList.size()) {
                            b = false;
                            meShoppingCartIvIcon.setImageResource(R.drawable.select_is_no);
                        } else {
                            b = true;
                            meShoppingCartIvIcon.setImageResource(R.drawable.select_is_yes);
                        }
                        if (total > 0) {
                            meShoppingCartTvTotal.setText(df.format(total));
                        } else {
                            meShoppingCartTvTotal.setText("0.00");
                        }
                        meShoppingCartTvNum.setText("（ " + count + " ）");
                        adapter.notifyDataSetChanged();
                    }
                } else if (isEdit == 2) {
                    int i = beanList.get(position).getNum();
                    if (i == 10) {
                        beanList.get(position).setNum(20);
                    } else
                        beanList.get(position).setNum(10);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    private boolean b;
    private double total;
    private DecimalFormat df = new DecimalFormat("#.00");
    private int isEdit = 1;

    @Override
    @OnClick({R.id.me_shopping_cart_select, R.id.me_shopping_cart_ll_num, R.id.toolbar_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_shopping_cart_select:
                if (isEdit == 1) {
                    if (!b) {
                        count = 0;
                        total = 0;
                        for (int i = 0; i < beanList.size(); i++) {
                            if (beanList.get(i).getErrors() == null || beanList.get(i).getErrors().size() <= 0) {
                                count++;
                                beanList.get(i).setNum(10);
                                total += Double.parseDouble(beanList.get(i).getNew_price()) * Double.parseDouble(beanList.get(i).getQuantity());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        meShoppingCartTvNum.setText("（ " + count + " ）");
                        if (total > 0) {
                            meShoppingCartTvTotal.setText(df.format(total));
                        } else {
                            meShoppingCartTvTotal.setText("0.00");
                        }
                        b = true;
                        meShoppingCartIvIcon.setImageResource(R.drawable.select_is_yes);
                    } else {
                        b = false;
                        for (int i = 0; i < beanList.size(); i++) {
                            beanList.get(i).setNum(20);
                        }
                        meShoppingCartTvNum.setText("（ 0 ）");
                        meShoppingCartTvTotal.setText("0.00");
                        meShoppingCartIvIcon.setImageResource(R.drawable.select_is_no);
                        adapter.notifyDataSetChanged();
                    }
                } else if (isEdit == 2) {
                    if (!b) {
                        count = 0;
                        for (int i = 0; i < beanList.size(); i++) {
                            if (beanList.get(i).getErrors() == null || beanList.get(i).getErrors().size() <= 0) {
                                count++;
                                beanList.get(i).setNum(10);
                            }
                        }
                        b = true;
                        meShoppingCartIvIcon.setImageResource(R.drawable.select_is_yes);
                    } else {
                        b = false;
                        for (int i = 0; i < beanList.size(); i++) {
                            beanList.get(i).setNum(20);
                        }
                        meShoppingCartIvIcon.setImageResource(R.drawable.select_is_no);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.me_shopping_cart_ll_num:
                StringBuffer sb = new StringBuffer();
                if (beanList != null && beanList.size() > 0) {
                    for (int i = 0; i < beanList.size(); i++) {
                        if (beanList.get(i).getNum() == 10) {
                            sb.append(beanList.get(i).getId() + ",");
                        }
                    }
                    if (sb.toString().length() > 0) {
                        sb.delete(sb.length() - 1, sb.length());
                    } else {
                        showShortToast(getString(R.string.please_select_product));
                        return;
                    }
                    if (isEdit == 1) {
                        Intent intent = new Intent(this, StoresOrderActivity.class);
                        intent.putExtra("ids", sb.toString());
                        startActivity(intent);

                    } else if (isEdit == 2) {
                        Map<String, Object> params = new TreeMap<>();
                        params.put("user_no", UserUtils.getString("user_no", null));
                        params.put("cart_ids", sb.toString());
                        CommonService.query("Mall/deleteCart", params, null, this, handler);
                    }
                } else {
                    showShortToast(getString(R.string.please_select_product));
                }

                break;
            case R.id.toolbar_menu:
                if (isEdit == 1) {
                    isEdit = 2;
                    setRightMenu(getString(R.string.done), android.R.color.black, 0, this);
                    meShoppingCartTvNum.setText(getString(R.string.delete));
                    meShoppingCartLlNum.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                    meShoppingCartTvNumFix.setVisibility(View.GONE);
                    meShoppingCartLlTotal.setVisibility(View.INVISIBLE);
                } else if (isEdit == 2) {
                    isEdit = 1;
                    setRightMenu(getString(R.string.edit), android.R.color.black, 0, this);
                    meShoppingCartLlNum.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBtn));
                    meShoppingCartLlTotal.setVisibility(View.VISIBLE);
                    meShoppingCartTvNumFix.setVisibility(View.VISIBLE);
                    count = 0;
                    total = 0;
                    for (int i = 0; i < beanList.size(); i++) {
                        if (beanList.get(i).getNum() == 10) {
                            count++;
                            total += Double.parseDouble(beanList.get(i).getNew_price()) * Double.parseDouble(beanList.get(i).getQuantity());
                        }
                    }
                    meShoppingCartTvNum.setText("（ " + count + " ）");
                    if (total > 0) {
                        meShoppingCartTvTotal.setText(df.format(total));
                    } else {
                        meShoppingCartTvTotal.setText("0.00");
                    }
                }


                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                    if (t[0].equals("Mall/cart")) {
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        meShoppingCartLl.setVisibility(View.GONE);
                        List<StoreBean> beans = jsonUtils.getEntityList("data", new StoreBean());
                        beanList.clear();
                        if (beans != null && beans.size() > 0) {
                            meShoppingCartLl.setVisibility(View.VISIBLE);
                            beanList.addAll(beans);
                        }
                        if (beanList == null || beanList.size() <= 0) {
                            meShoppingCartLl.setVisibility(View.GONE);
                            commonLlDataNull.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    } else if (t[0].equals("Mall/modifyCart")) {
                        List<String> lists = jsonUtils.getStringList("errors", "data");
                        if (lists != null && lists.size() > 0) {
                            beanList.get(position).setErrors(lists);
                            beanList.get(position).setNum(20);
                            adapter.notifyDataSetChanged();
                        } else {
                            if (beanList.get(position).getErrors() != null && beanList.get(position).getErrors().size() > 0) {
                                beanList.get(position).getErrors().clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        count = 0;
                        total = 0;
                        for (int j = 0; j < beanList.size(); j++) {
                            if (beanList.get(j).getNum() == 10) {
                                count++;
                                total += Double.parseDouble(beanList.get(j).getNew_price()) * Double.parseDouble(beanList.get(j).getQuantity());
                            }
                        }
                        if (total > 0) {
                            meShoppingCartTvTotal.setText(df.format(total));
                        } else {
                            meShoppingCartTvTotal.setText("0.00");
                        }
                        if (count != beanList.size()) {
                            b = false;
                            meShoppingCartIvIcon.setImageResource(R.drawable.select_is_no);
                        } else {
                            b = true;
                            meShoppingCartIvIcon.setImageResource(R.drawable.select_is_yes);
                        }
                        meShoppingCartTvNum.setText("（ " + count + " ）");
                    } else if (t[0].equals("Mall/deleteCart")) {
                        showShortToast(getString(R.string.delete_success));
                        Map<String, Object> params = new TreeMap<>();
                        params.put("user_no", UserUtils.getString("user_no", null));
                        CommonService.query("Mall/cart", params, null, this, handler);
                    }
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
}
