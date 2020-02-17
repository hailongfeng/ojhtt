package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;

public class OrderToastActivity extends BaseActivity {

    @BindView(R.id.mining_toast_tv_buy_name)
    TextView miningToastTvBuyName;
    @BindView(R.id.mining_toast_tv_sell_num)
    TextView miningToastTvSellNum;
    @BindView(R.id.mining_toast_tv_sell_price)
    TextView miningToastTvSellPrice;
    @BindView(R.id.mining_toast_tv_sell_amount)
    TextView miningToastTvSellAmount;
    @BindView(R.id.mining_toast_tv_sell_security)
    EditText miningToastTvSellSecurity;
    @BindView(R.id.mining_toast_tv_sell_btn)
    Button miningToastTvSellBtn;
    private Intent intent;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_order_toast);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {
        intent = getIntent();


    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {

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
