package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.UserInfo;

public class MainAssetToastActivity extends BaseActivity {
    @BindView(R.id.asset_toast_tv_name)
    TextView assetToastTvName;
    @BindView(R.id.asset_toast_tv_number)
    TextView assetToastTvNumber;
    @BindView(R.id.asset_toast_tv_tel)
    TextView assetToastTvTel;
    @BindView(R.id.asset_toast_tv_call)
    TextView assetToastTvCall;
    @BindView(R.id.asset_toast_tv_alipay)
    TextView assetToastTvAlipay;
    @BindView(R.id.asset_toast_tv_wecha)
    TextView assetToastTvWecha;
    @BindView(R.id.asset_toast_tv_bank_name)
    TextView assetToastTvBankName;
    @BindView(R.id.asset_toast_tv_switch)
    TextView assetToastTvSwitch;
    @BindView(R.id.asset_toast_tv_bank_num)
    TextView assetToastTvBankNum;
    private UserInfo info;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_asset_toast);
        ButterKnife.bind(this);
        info = (UserInfo) getIntent().getSerializableExtra("info");
    }

    @Override
    protected void initData() {
        assetToastTvName.setText(info.getRealname());
        assetToastTvNumber.setText(info.getUser_no());
        assetToastTvTel.setText(info.getPhone());
        assetToastTvAlipay.setText(info.getAlipay());
        assetToastTvWecha.setText(info.getWechat());
        assetToastTvBankName.setText(info.getBank());
        assetToastTvSwitch.setText(info.getSub_bank());
        assetToastTvBankNum.setText(info.getBank_no());
    }

    @Override
    @OnClick({R.id.asset_toast_tv_call})
    public void onClick(View v) {
        if (v.getId() == R.id.asset_toast_tv_call) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + info.getPhone()));
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
