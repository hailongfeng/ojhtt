package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.UserBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2017/9/1.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_et_zh)
    EditText loginEtZh;
    @BindView(R.id.login_et_psw)
    EditText loginEtPsw;
    @BindView(R.id.login_btn_user)
    Button loginBtnUser;
    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_forget)
    TextView loginForget;


    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        hideDivider();
    }

    @Override
    protected void initData() {

    }

    @Override
    @OnClick({R.id.login_btn_user, R.id.login_register, R.id.login_forget})
    public void onClick(View v) {
        String zh = loginEtZh.getText().toString().trim();
        String psw = loginEtPsw.getText().toString().trim();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_btn_user:
                if (zh.length() > 0) {
                    if (psw.length() > 0) {
                        if (psw.length() >= 6 && psw.length() <= 20) {
                            Map<String, Object> maps = new TreeMap<>();
                            maps.put("password", SHAUtils.getSHA(psw));
                            maps.put("username", zh);
                            CommonService.query("Me/login", maps, null, this, handler);
                        } else
                            showShortToast(getResources().getString(R.string.login_psw_hint));
                    } else
                        showShortToast(getString(R.string.login_input_psw));
                } else
                    showShortToast(getString(R.string.login_input_zh));
                break;
            case R.id.login_register:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forget:
                intent.setClass(this, MeForgetActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (t[0].equals("Me/login")) {
                    if (jsonUtils.getCode().equals("200")) {
                        showShortToast(getString(R.string.login_success));
                        UserBean userBean = jsonUtils.getEntity("data", new UserBean());
                        UserUtils.saveString("user_no", userBean.getUser_no());
                        UserUtils.saveString("realname", userBean.getRealname());
                        UserUtils.saveString("recommend_number", userBean.getRecommend_number());
                        UserUtils.saveString("team_number", userBean.getTeam_number());
                        UserUtils.saveString("invitation_code", userBean.getInvitation_code());
                        UserUtils.saveString("qrcode", userBean.getQrcode());
                        UserUtils.saveString("phone", userBean.getPhone());
                        UserUtils.saveString("sms_code", userBean.getSms_code());
                        UserUtils.saveString("dti_account", userBean.getDti_account());
                        UserUtils.saveString("dti_frozen", userBean.getDti_frozen());
                        UserUtils.saveString("rb_account", userBean.getRb_account());
                      /*  if (b) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }*/
                        Intent broad = new Intent();
                        broad.setAction("android.intent.action.me");
                        sendBroadcast(broad);
                        setResult(20);
                        finish();
                    } else {
                        AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
                    }
                }
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        super.onNetWorkedFail(strings);
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }
}
