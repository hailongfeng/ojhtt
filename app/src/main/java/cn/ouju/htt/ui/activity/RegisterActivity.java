package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import cn.ouju.htt.bean.UserBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.popupwindow.WorldCodeWindow;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2017/10/30.
 */

public class RegisterActivity extends BaseActivity implements OnItemClickListener {
    @BindView(R.id.register_et_real_name)
    EditText registerEtRealName;
    @BindView(R.id.register_et_psw)
    EditText registerEtPsw;
    @BindView(R.id.register_et_again)
    EditText registerEtAgain;
    @BindView(R.id.register_et_invite)
    EditText registerEtInvite;
    @BindView(R.id.register_et_tel)
    EditText registerEtTel;
    @BindView(R.id.register_tv_get)
    TextView registerTvGet;
    @BindView(R.id.register_et_captcha)
    EditText registerEtCaptcha;
    @BindView(R.id.register_cb)
    CheckBox registerCb;
    @BindView(R.id.register_contract)
    TextView registerContract;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.register_et_area)
    TextView registerEtArea;
    private CountDownTimer timer;
    private boolean b = true;
    private WorldCodeWindow window;
    private List<AreaCodeBean> lists;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.register_dti_zh), android.R.color.black);
        hideDivider();
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                registerTvGet.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                GradientDrawable drawable = (GradientDrawable) registerTvGet.getBackground();
                drawable.setStroke(1, ContextCompat.getColor(RegisterActivity.this, R.color.colorBtn));
                registerTvGet.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorBtn));
                registerTvGet.setText(getString(R.string.register_get_captcha));
                registerTvGet.setEnabled(true);
            }
        };
        registerCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    registerBtn.setAlpha(1.0f);
                    b = true;
                } else {
                    b = false;
                    registerBtn.setAlpha(0.5f);
                }
            }
        });
    }

    @Override
    protected void initData() {
        lists = new ArrayList<>();
        registerContract.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        registerContract.getPaint().setAntiAlias(true);
        window = new WorldCodeWindow(this, this);
    }

    @Override
    @OnClick({R.id.register_btn, R.id.register_tv_get, R.id.register_contract, R.id.register_et_area})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_contract:
                Intent intent = new Intent();
                //   intent.setClass(this, RegisterContractActivity.class);
                intent.putExtra("title", "protocol");
                startActivity(intent);
                break;
            case R.id.register_btn:
                String name = registerEtRealName.getText().toString().trim();
                String psw = registerEtPsw.getText().toString().trim();
                String again = registerEtAgain.getText().toString().trim();
                String invite = registerEtInvite.getText().toString().trim();
                String tel = registerEtTel.getText().toString().trim();
                String captcha = registerEtCaptcha.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(psw)) {
                        if (psw.length() >= 6 && psw.length() <= 20) {
                            if (!TextUtils.isEmpty(again)) {
                                if (again.length() >= 6 && again.length() <= 20) {
                                    if (psw.equals(again)) {
                                        if (!TextUtils.isEmpty(invite)) {
                                            if (!TextUtils.isEmpty(tel)) {
                                                if (tel.length() <= 11) {
                                                    if (!TextUtils.isEmpty(captcha)) {
                                                        Map<String, Object> params = new TreeMap<>();
                                                        params.put("phone", tel);
                                                        params.put("realname", name);
                                                        params.put("code", captcha);
                                                        params.put("password", SHAUtils.getSHA(psw));
                                                        if (bean != null) {
                                                            params.put("sms", bean.getCode());
                                                        } else {
                                                            params.put("sms", "86");
                                                        }
                                                        params.put("recommend_no", invite);
                                                        CommonService.query("Me/register", params, null, this, handler);
                                                    } else {
                                                        showShortToast(getString(R.string.captcha_no));
                                                    }

                                                } else
                                                    showShortToast(getString(R.string.register_phone_hint));
                                            } else {
                                                showShortToast(getString(R.string.register_input_phone));
                                            }
                                        } else {
                                            showShortToast(getString(R.string.register_input_invitation_code_hint));
                                        }
                                    } else
                                        showShortToast(getString(R.string.register_psw_repeat_no));
                                } else {
                                    showShortToast(getString(R.string.register_input_psw_repeat_hint));
                                }
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
                    showShortToast(getString(R.string.register_realname_hint));
                }
                break;
            case R.id.register_tv_get:
                String phone = registerEtTel.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    if (String.valueOf(Long.parseLong(phone)).length() == 11) {
                        Map<String, Object> params = new TreeMap<>();
                        params.put("phone", phone);
                        if (bean != null) {
                            params.put("sms", bean.getCode());
                        } else {
                            params.put("sms", "86");
                        }
                        params.put("type", 0);
                        CommonService.query("Home/getCode", params, null, this, handler);
                    } else
                        showShortToast(getString(R.string.register_phone_hint));
                } else {
                    showShortToast(getString(R.string.register_input_phone));
                }
                break;
            case R.id.register_et_area:
                CommonService.query("Home/smscodeList", null, null, this, handler);
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
                        registerTvGet.setEnabled(false);
                        GradientDrawable drawable = (GradientDrawable) registerTvGet.getBackground();
                        drawable.setStroke(1, ContextCompat.getColor(RegisterActivity.this, R.color.colorGrayText));
                        registerTvGet.setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
                    } else if (t[0].equals("Me/register")) {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(getString(R.string.register_success)).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(RegisterActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("tab", "3");
                                startActivity(intent);
                                finish();
                            }
                        }).show();
                    } else if (t[0].equals("Home/smscodeList")) {
                        List<AreaCodeBean> list = jsonUtils.getEntityList("data", new AreaCodeBean());
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                String is_default = list.get(i).getIs_default();
                                if (is_default.equals("1")) {
                                    registerEtArea.setText("+" + list.get(i).getCode() + " " + list.get(i).getChinese_name() + " " + list.get(i).getName());
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

    private AreaCodeBean bean;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        window.dismiss();
        bean = lists.get(position);
        registerEtArea.setText("+" + bean.getCode() + " " + bean.getChinese_name() + " " + bean.getName());
    }
}
