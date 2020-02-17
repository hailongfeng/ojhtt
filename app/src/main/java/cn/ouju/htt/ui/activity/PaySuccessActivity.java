package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ouju.htt.R;

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.pay_tv_num)
    TextView payTvNum;
    @BindView(R.id.pay_tv_continue)
    TextView payTvContinue;
    @BindView(R.id.pay_tv_order)
    TextView payTvOrder;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_pay_success);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.pay_success), android.R.color.black);
    }
    private DecimalFormat df = new DecimalFormat("#.00");
    @Override
    protected void initData() {
        String amount = getIntent().getStringExtra("amount");
        payTvNum.setText("-" + df.format(Double.parseDouble(amount)));
    }

    @Override
    @OnClick({R.id.pay_tv_continue, R.id.pay_tv_order})
    public void onClick(View v) {
        if (v.getId() == R.id.pay_tv_continue) {
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("tab","4");
            startActivity(intent);
        }
        if (v.getId() == R.id.pay_tv_order) {
            Intent intent = new Intent();
            intent.setClass(this, OrderListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {

    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }
}
