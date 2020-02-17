package cn.ouju.htt.ui.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.AreaCodeBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.popupwindow.WorldCodeWindow;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;

public class MeForgetActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.forget_et_area)
    TextView forgetEtArea;
    @BindView(R.id.forget_et_tel)
    EditText forgetEtTel;
    @BindView(R.id.forget_tv_get)
    TextView forgetTvGet;
    @BindView(R.id.forget_et_captcha)
    EditText forgetEtCaptcha;
    @BindView(R.id.forget_et_psw)
    EditText forgetEtPsw;
    @BindView(R.id.forget_et_repeat)
    EditText forgetEtRepeat;
    @BindView(R.id.forget_btn)
    Button forgetBtn;
    private WorldCodeWindow window;
    private List<AreaCodeBean> lists;
    private CountDownTimer timer;
    private AreaCodeBean bean;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_forget);
        showToolBar(android.R.color.white);
        setTitle(getResources().getString(R.string.login_forget_psw), android.R.color.black);
    }

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        window = new WorldCodeWindow(this, this);
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                forgetTvGet.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                GradientDrawable drawable = (GradientDrawable) forgetTvGet.getBackground();
                drawable.setStroke(1, ContextCompat.getColor(MeForgetActivity.this, R.color.colorBtn));
                forgetTvGet.setTextColor(ContextCompat.getColor(MeForgetActivity.this, R.color.colorBtn));
                forgetTvGet.setText(getString(R.string.register_get_captcha));
                forgetTvGet.setEnabled(true);
            }
        };
    }

    @Override
    @OnClick({R.id.forget_et_area, R.id.forget_btn, R.id.forget_tv_get})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_et_area:
                CommonService.query("Home/smscodeList", null, null, this, handler);
                break;
            case R.id.forget_btn:
                String psw = forgetEtPsw.getText().toString().trim();
                String again = forgetEtRepeat.getText().toString().trim();
                String tel = forgetEtTel.getText().toString().trim();
                String captcha = forgetEtCaptcha.getText().toString().trim();
                if (!TextUtils.isEmpty(tel)) {
                    if (tel.length() <= 11) {
                        if (!TextUtils.isEmpty(captcha)) {
                            if (!TextUtils.isEmpty(psw)) {
                                if (psw.length() >= 6 && psw.length() <= 20) {
                                    if (!TextUtils.isEmpty(again)) {
                                        if (psw.equals(again)) {
                                            Map<String, Object> params = new TreeMap<>();
                                            params.put("phone", tel);
                                            params.put("code", captcha);
                                            params.put("password", SHAUtils.getSHA(psw));
                                            CommonService.query("Me/forgetPass", params, null, this, handler);
                                        } else
                                            showShortToast(getString(R.string.register_psw_repeat_no));

                                    } else {
                                        showShortToast(getString(R.string.register_input_psw_hint));
                                    }
                                } else {
                                    showShortToast(getResources().getString(R.string.login_psw_hint));
                                }
                            } else {
                                showShortToast(getResources().getString(R.string.register_psw_hint));
                            }
                        } else {
                            showShortToast(getString(R.string.captcha_no));
                        }
                    } else {
                        showShortToast(getString(R.string.register_phone_hint));
                    }
                } else {
                    showShortToast(getString(R.string.register_input_phone));
                }
                break;
            case R.id.forget_tv_get:
                String phone = forgetEtTel.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    if (String.valueOf(Long.parseLong(phone)).length() <= 11) {
                        Map<String, Object> params = new TreeMap<>();
                        params.put("phone", phone);
                        if (bean != null) {
                            params.put("sms", bean.getCode());
                        } else {
                            params.put("sms", "86");
                        }
                        params.put("type", 1);
                        CommonService.query("Home/getCode", params, null, this, handler);
                    } else
                        showShortToast(getString(R.string.register_phone_hint));
                } else {
                    showShortToast(getString(R.string.register_input_phone));
                }
                break;
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
                        forgetTvGet.setEnabled(false);
                        GradientDrawable drawable = (GradientDrawable) forgetTvGet.getBackground();
                        drawable.setStroke(1, ContextCompat.getColor(MeForgetActivity.this, R.color.colorGrayText));
                        forgetTvGet.setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
                    } else if (t[0].equals("Home/smscodeList")) {
                        List<AreaCodeBean> list = jsonUtils.getEntityList("data", new AreaCodeBean());
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                String is_default = list.get(i).getIs_default();
                                if (is_default.equals("1")) {
                                    forgetEtArea.setText("+" + list.get(i).getCode() + " " + list.get(i).getChinese_name() + " " + list.get(i).getName());
                                    break;
                                }
                            }
                            lists.addAll(list);
                            window.setLists(lists);
                            if (window.isShowing()) {
                                window.dismiss();
                            } else {
                                window.showAtLocation(getCurrentFocus(), Gravity.BOTTOM, 0, 0);
                            }
                        }
                    } else if (t[0].equals("Me/forgetPass")) {
                        showShortToast(getString(R.string.set_psw_success));
                        finish();
                    }
                } else {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        window.dismiss();
        bean = lists.get(position);
        forgetEtArea.setText("+" + bean.getCode() + " " + bean.getChinese_name() + " " + bean.getName());
    }
}
