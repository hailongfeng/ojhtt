package cn.ouju.htt.ui.activity;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;

public class AssetPayToastActivity extends BaseActivity {

    @BindView(R.id.pay_iv)
    ImageView payIv;

    @Override
    protected void initView() {
        setChildContentView(R.layout.alert_pay_iv);
        ButterKnife.bind(this);

    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("path");
        Picasso.with(this).load(url).into(payIv);
    }

    @Override
    public void onClick(View v) {

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
