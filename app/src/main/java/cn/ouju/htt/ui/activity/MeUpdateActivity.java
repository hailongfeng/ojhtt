package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeUpdateActivity extends BaseActivity {
    @BindView(R.id.update_et_old)
    EditText updateEtOld;
    @BindView(R.id.update_et_new)
    EditText updateEtNew;
    @BindView(R.id.update_et_again)
    EditText updateEtAgain;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_update);
        String type = getIntent().getStringExtra("type");
        if (type.equals("psw")) {
            setTitle(getString(R.string.update_psw), android.R.color.black);
            updateEtOld.setHint(getString(R.string.current_psw));
            updateEtNew.setHint(getString(R.string.new_psw));
            updateEtAgain.setHint(getString(R.string.register_confirm_psw));
        } else if (type.equals("safe")) {
            setTitle(getString(R.string.update_safe_psw), android.R.color.black);
            updateEtOld.setHint(getString(R.string.current_safe_psw));
            updateEtNew.setHint(getString(R.string.news_safe_psw));
            updateEtAgain.setHint(getString(R.string.confirm_safe_psw));
        }
        setRightMenu(getString(R.string.confirm), R.color.colorBtn, 0, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    @OnClick({R.id.toolbar_menu})
    public void onClick(View v) {
        String old = updateEtOld.getText().toString().trim();
        String news = updateEtNew.getText().toString().trim();
        String again = updateEtAgain.getText().toString().trim();
        if (v.getId() == R.id.toolbar_menu) {
            if (getIntent().getStringExtra("type").equals("psw")) {
                if (!TextUtils.isEmpty(old)) {
                    if (old.length() >= 6 && old.length() <= 20) {
                        if (!TextUtils.isEmpty(news)) {
                            if (news.length() >= 6 && news.length() <= 20) {
                                if (!TextUtils.isEmpty(again)) {
                                    if (news.equals(again)) {
                                        Map<String, Object> params = new HashMap<>();
                                        params.put("user_no", UserUtils.getString("user_no", null));
                                        params.put("old_password", SHAUtils.getSHA(old));
                                        params.put("password", SHAUtils.getSHA(news));
                                        params.put("type", 0);
                                        CommonService.query("Me/changePass", params, null, this, handler);
                                    } else
                                        showShortToast(getString(R.string.register_psw_repeat_no));
                                } else
                                    showShortToast(getString(R.string.register_input_psw_hint));
                            } else
                                showShortToast(getString(R.string.new_psw_must_hint));
                        } else
                            showShortToast(getString(R.string.please_input_current_news_psw));
                    } else
                        showShortToast(getResources().getString(R.string.login_psw_hint));
                } else
                    showShortToast(getString(R.string.please_current_psw));

            } else if (getIntent().getStringExtra("type").equals("safe")) {
                if (!TextUtils.isEmpty(old)) {
                    if (old.length() >= 6 && old.length() <= 20) {
                        if (!TextUtils.isEmpty(news)) {
                            if (news.length() >= 6 && news.length() <= 20) {
                                if (!TextUtils.isEmpty(again)) {
                                    if (news.equals(again)) {
                                        Map<String, Object> params = new HashMap<>();
                                        params.put("user_no", UserUtils.getString("user_no", null));
                                        params.put("old_password", SHAUtils.getSHA(old));
                                        params.put("password", SHAUtils.getSHA(news));
                                        params.put("type", 1);
                                        CommonService.query("Me/changePass", params, null, this, handler);
                                    } else
                                        showShortToast(getString(R.string.new_safe_psw_and_safe_psw_no));
                                } else
                                    showShortToast(getString(R.string.input_confirm_safe_psw));
                            } else
                                showShortToast(getString(R.string.new_safe_psw_must_hint));
                        } else
                            showShortToast(getString(R.string.please_input_current_new_safe_psw));
                    } else
                        showShortToast(getString(R.string.safe_new_psw_must_hint));
                } else
                    showShortToast(getString(R.string.please_current_safe_psw));
            }
        }

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Me/changePass")) {
                        showShortToast(getString(R.string.update_success));
                        finish();
                    }
                } else if (jsonUtils.getCode().equals("405")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
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
