package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ouju.htt.R;

public class MeSettingActivity extends BaseActivity {
    @BindView(R.id.setting_tv_psw)
    TextView settingTvPsw;
    @BindView(R.id.setting_tv_safe)
    TextView settingTvSafe;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_setting);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.setting), android.R.color.black);
    }

    @Override
    protected void initData() {

    }

    @Override
    @OnClick({R.id.setting_tv_psw, R.id.setting_tv_safe})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.setting_tv_psw:
                intent.setClass(this, MeUpdateActivity.class);
                intent.putExtra("type", "psw");
                startActivity(intent);
                break;
            case R.id.setting_tv_safe:
                intent.setClass(this, MeUpdateActivity.class);
                intent.putExtra("type", "safe");
                startActivity(intent);
                break;
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
