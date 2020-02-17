package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.StoreBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by pc on 2018/1/27.
 */

public class StoreDetailsActivity extends BaseActivity {
    @BindView(R.id.shopping_details_iv_pic)
    ImageView shoppingDetailsIvPic;
    @BindView(R.id.shopping_details_tv_title)
    TextView shoppingDetailsTvTitle;
    @BindView(R.id.shopping_details_tv_price)
    TextView shoppingDetailsTvPrice;
    @BindView(R.id.shopping_details_tv_xg)
    TextView shoppingDetailsTvXg;
    @BindView(R.id.shopping_details_tv_kc)
    TextView shoppingDetailsTvKc;
    @BindView(R.id.shopping_details_btn_minus)
    Button shoppingDetailsBtnMinus;
    @BindView(R.id.shopping_details_et_num)
    EditText shoppingDetailsEtNum;
    @BindView(R.id.shopping_details_btn_plus)
    Button shoppingDetailsBtnPlus;
    @BindView(R.id.shopping_details_tv_join)
    Button shoppingDetailsTvJoin;
    @BindView(R.id.shopping_details_wv)
    WebView shoppingDetailsWv;
    @BindView(R.id.shopping_details_tv_ys)
    TextView shoppingDetailsTvYs;
    @BindView(R.id.shopping_details_tv_description)
    TextView shoppingDetailsTvDescription;
    @BindView(R.id.shopping_details_tv_xg_fix)
    TextView shoppingDetailsTvXgFix;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_store_details);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.store_details), android.R.color.black);
    }

    @Override
    protected void initData() {
        Map<String, Object> params = new TreeMap<>();
        params.put("goods_id", getIntent().getStringExtra("id"));
        CommonService.query("Mall/goodsDetail", params, null, this, handler);
        WebSettings settings = shoppingDetailsWv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    private int num = 1;
    private StoreBean storeBean;

    @Override
    @OnClick({R.id.shopping_details_btn_minus, R.id.shopping_details_btn_plus, R.id.shopping_details_tv_join})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_details_btn_plus:
                if (storeBean != null) {
                    if (shoppingDetailsEtNum.getText().toString().trim().length() != 0) {
                        num = Integer.parseInt(shoppingDetailsEtNum.getText().toString());
                    } else {
                        num = 0;
                    }
                    if (storeBean.getLimit_number().equals("0")) {
                        num++;
                    } else if (num < Integer.parseInt(storeBean.getLimit_number())) {
                        num++;
                    } else {
                        num = Integer.parseInt(storeBean.getLimit_number());
                    }
                    shoppingDetailsEtNum.setText("" + num);
                    shoppingDetailsEtNum.setSelection(shoppingDetailsEtNum.getText().length());
                }
                break;
            case R.id.shopping_details_btn_minus:
                if (storeBean != null) {
                    if (shoppingDetailsEtNum.getText().toString().trim().length() != 0) {
                        num = Integer.parseInt(shoppingDetailsEtNum.getText().toString());
                    }
                    if (num > 1) {
                        num--;
                        shoppingDetailsEtNum.setText("" + num);
                        shoppingDetailsEtNum.setSelection(shoppingDetailsEtNum.getText().length());
                    }
                }
                break;
            case R.id.shopping_details_tv_join:
                if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                    Map<String, Object> params = new TreeMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("goods_id", storeBean.getId());
                    params.put("quantity", shoppingDetailsEtNum.getText().toString().trim());
                    CommonService.query("Mall/addCart", params, null, this, handler);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }


    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (t[0].equals("Mall/goodsDetail")) {
                    storeBean = jsonUtils.getEntity("data", new StoreBean());
                    if (storeBean != null) {
                        Picasso.with(this).load(storeBean.getPic_url()).into(shoppingDetailsIvPic);
                        shoppingDetailsTvTitle.setText(storeBean.getTitle());
                        shoppingDetailsTvPrice.setText(storeBean.getPrice());
                        shoppingDetailsTvYs.setText(storeBean.getSell_number());
                        String limit = storeBean.getLimit_number();
                        if (!TextUtils.isEmpty(limit) && limit.equals("0")) {
                            shoppingDetailsTvXgFix.setText(getString(R.string.limit_no));
                            shoppingDetailsTvXg.setVisibility(View.GONE);
                        } else {
                            shoppingDetailsTvXg.setText(storeBean.getLimit_number());
                        }
                        shoppingDetailsTvKc.setText(storeBean.getInventory_number());
                        shoppingDetailsWv.loadDataWithBaseURL(null, storeBean.getDetail(), "text/html", "utf-8", null);
                    }
                } else if (t[0].equals("Mall/addCart")) {
                    if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                        showShortToast(getString(R.string.store_add_success));
                    } else
                        AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
                }
            }
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

}
