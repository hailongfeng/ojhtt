package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

public class TradingToastActivity extends BaseActivity {
    @BindView(R.id.mining_toast_tv_buy_name)
    TextView miningToastTvBuyName;
    @BindView(R.id.mining_toast_tv_sell_num)
    TextView miningToastTvSellNum;
    @BindView(R.id.mining_toast_tv_sell_price)
    TextView miningToastTvSellPrice;
    @BindView(R.id.mining_toast_tv_sell_amount)
    TextView miningToastTvSellAmount;
    @BindView(R.id.mining_toast_tv_sell_name)
    TextView miningToastTvSellName;
    @BindView(R.id.mining_toast_tv_sell_tel)
    TextView miningToastTvSellTel;
    @BindView(R.id.mining_toast_tv_sell_captcha)
    EditText miningToastTvSellCaptcha;
    @BindView(R.id.mining_toast_tv_sell_get)
    TextView miningToastTvSellGet;
    @BindView(R.id.mining_toast_tv_sell_security)
    EditText miningToastTvSellSecurity;
    @BindView(R.id.mining_toast_tv_sell_btn)
    Button miningToastTvSellBtn;
    @BindView(R.id.mining_toast_fix)
    TextView miningToastFix;
    @BindView(R.id.mining_toast_ll)
    LinearLayout miningToastLl;
    @BindView(R.id.mining_toast_tv_sell_fix)
    TextView miningToastTvSellFix;
    private Intent intent;
    private CountDownTimer timer;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_mining_toast);
        ButterKnife.bind(this);
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                miningToastTvSellGet.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                GradientDrawable drawable = (GradientDrawable) miningToastTvSellGet.getBackground();
                drawable.setStroke(1, ContextCompat.getColor(TradingToastActivity.this, R.color.colorBtn));
                miningToastTvSellGet.setTextColor(ContextCompat.getColor(TradingToastActivity.this, R.color.colorBtn));
                miningToastTvSellGet.setText(getString(R.string.register_get_captcha));
                miningToastTvSellGet.setEnabled(true);
            }
        };
    }

    @Override
    protected void initData() {
        intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("type")) && intent.getStringExtra("type").equals("sell")) {
            miningToastLl.setVisibility(View.GONE);
            miningToastFix.setText(getString(R.string.seller)+"：");
            miningToastTvBuyName.setText(intent.getStringExtra("buy_uno"));
            miningToastTvSellName.setText(intent.getStringExtra("sale_uno"));
            miningToastTvSellFix.setText(getString(R.string.sell_num)+"：");

        } else {
            miningToastLl.setVisibility(View.VISIBLE);
            miningToastFix.setText(getString(R.string.buyer)+"：");
            miningToastTvBuyName.setText(intent.getStringExtra("sale_uno"));
            miningToastTvSellName.setText(intent.getStringExtra("buy_uno"));
            miningToastTvSellFix.setText(getString(R.string.buy_num)+"：");
        }
        miningToastTvSellNum.setText(intent.getStringExtra("number"));
        miningToastTvSellPrice.setText(intent.getStringExtra("price") + getString(R.string.dollar));
        miningToastTvSellAmount.setText(intent.getStringExtra("amount") +  getString(R.string.cny));
        miningToastTvSellTel.setText(intent.getStringExtra("buy_phone"));


    }

    @Override
    @OnClick({R.id.mining_toast_tv_sell_get, R.id.mining_toast_tv_sell_btn, R.id.asset_toast_tv_call})
    public void onClick(View v) {
        if (v.getId() == R.id.mining_toast_tv_sell_get) {
            Map<String, Object> params = new TreeMap<>();
            params.put("phone", intent.getStringExtra("sale_phone"));
            String sms = UserUtils.getString("sms_code", null);
            if (!TextUtils.isEmpty(sms)) {
                params.put("sms", sms);
            } else
                params.put("sms", "86");
            params.put("type", 3);
            CommonService.query("Home/getCode", params, null, this, handler);
        }
        if (v.getId() == R.id.mining_toast_tv_sell_btn) {
            String captcha = miningToastTvSellCaptcha.getText().toString().trim();
            String psw = miningToastTvSellSecurity.getText().toString().trim();
            Map<String, Object> params = new TreeMap<>();

            params.put("trade_id", intent.getStringExtra("trade_id"));
            if (intent.getStringExtra("type").equals("buy")) {
                if (TextUtils.isEmpty(captcha)) {
                    showShortToast(getString(R.string.captcha_no));
                } else {
                    params.put("code", captcha);
                }
            }
            if (!TextUtils.isEmpty(psw)) {
                params.put("password", SHAUtils.getSHA(psw));
                if (intent.getStringExtra("type").equals("buy")) {
                    params.put("user_no", intent.getStringExtra("sale_uno"));
                    CommonService.query("Dti/confirmSale", params, null, this, handler);
                } else if (intent.getStringExtra("type").equals("sell")) {
                    params.put("user_no", intent.getStringExtra("buy_uno"));
                    CommonService.query("Dti/confirmBuy", params, null, this, handler);
                }
            } else
                showShortToast(getString(R.string.safe_psw_exist));


        }
        if (v.getId() == R.id.asset_toast_tv_call)

        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + miningToastTvSellTel.getText().toString().trim()));
            startActivity(intent);
        }

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Home/getCode")) {
                        showShortToast(getString(R.string.captcha_send_success));
                        timer.start();
                        miningToastTvSellGet.setEnabled(false);
                        GradientDrawable drawable = (GradientDrawable) miningToastTvSellGet.getBackground();
                        drawable.setStroke(1, ContextCompat.getColor(TradingToastActivity.this, R.color.colorGrayText));
                        miningToastTvSellGet.setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
                    } else if (t[0].equals("Dti/confirmSale")) {
                        showShortToast(getString(R.string.success_sell));
                        setResult(20);
                        finish();
                    } else if (t[0].equals("Dti/confirmBuy")) {
                        showShortToast(getString(R.string.buy_success));
                        setResult(20);
                        finish();
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

}
