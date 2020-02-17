package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.UserInfo;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeInformationActivity extends BaseActivity {
    @BindView(R.id.me_info_et_num)
    EditText meInfoEtNum;
    @BindView(R.id.me_info_et_name)
    EditText meInfoEtName;
    @BindView(R.id.me_info_et_tel)
    EditText meInfoEtTel;
    @BindView(R.id.me_info_et_idcard)
    EditText meInfoEtIdcard;
    @BindView(R.id.me_info_et_alipay)
    EditText meInfoEtAlipay;
    @BindView(R.id.me_info_et_wecha)
    EditText meInfoEtWecha;
    @BindView(R.id.me_info_et_bank_name)
    EditText meInfoEtBankName;
    @BindView(R.id.me_info_et_bank_switch)
    EditText meInfoEtBankSwitch;
    @BindView(R.id.me_info_et_bank_num)
    EditText meInfoEtBankNum;


    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_information);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.me_information), android.R.color.black);

    }

    @Override
    protected void initData() {
        meInfoEtNum.setEnabled(false);
        meInfoEtTel.setEnabled(false);
        /*  String result = UserUtils.getString("useraudit", null);*/
        Map<String, Object> params = new TreeMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Me/getUser", params, null, this, handler);

    }

    @Override
    @OnClick({R.id.toolbar_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_menu:
                final String num = meInfoEtNum.getText().toString().trim();
                final String tel = meInfoEtTel.getText().toString().trim();
                final String card = meInfoEtIdcard.getText().toString().trim();
                final String alipay = meInfoEtAlipay.getText().toString().trim();
                final String wecha = meInfoEtWecha.getText().toString().trim();
                final String bankname = meInfoEtBankName.getText().toString().trim();
                final String bankswitch = meInfoEtBankSwitch.getText().toString().trim();
                final String banknum = meInfoEtBankNum.getText().toString().trim();
                if (!TextUtils.isEmpty(num)) {
                    if (!TextUtils.isEmpty(tel)) {
                        if (!TextUtils.isEmpty(card)) {
                            if (!TextUtils.isEmpty(alipay)) {
                                if (!TextUtils.isEmpty(wecha)) {
                                    if (!TextUtils.isEmpty(bankname)) {
                                        if (!TextUtils.isEmpty(bankswitch)) {
                                            if (!TextUtils.isEmpty(banknum)) {
                                                final EditText et = new EditText(this);
                                                et.setFocusable(true);
                                                et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                                builder.setTitle(getString(R.string.input_safe_psw)).setView(et).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String psw = et.getText().toString().trim();
                                                        if (!TextUtils.isEmpty(psw)) {
                                                            Map<String, Object> params = new HashMap<>();
                                                            params.put("user_no", num);
                                                            params.put("password", SHAUtils.getSHA(et.getText().toString().trim()));
                                                            params.put("id_card", "" + card);
                                                            params.put("alipay", "" + alipay);
                                                            params.put("wechat", "" + wecha);
                                                            params.put("bank", "" + bankname);
                                                            params.put("sub_bank", "" + bankswitch);
                                                            params.put("bank_no", "" + banknum);
                                                            CommonService.query("Me/setUser", params, null, MeInformationActivity.this, handler);
                                                        } else
                                                            showShortToast(getString(R.string.input_safe_psw_hint));
                                                    }
                                                }).show();

                                            } else {
                                                showShortToast(getString(R.string.bank_no_hint));
                                            }
                                        } else {
                                            showShortToast(getString(R.string.bank_switch_hint));
                                        }
                                    } else {
                                        showShortToast(getString(R.string.bank_name_input_hint));
                                    }
                                } else {
                                    showShortToast(getString(R.string.wechat_input_hint));
                                }
                            } else {
                                showShortToast(getString(R.string.alipay_input_hint));
                            }
                        } else {
                            showShortToast(getString(R.string.id_card_hint));
                        }
                    }

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
                    if (t[0].equals("Me/getUser")) {
                        UserInfo userInfo = jsonUtils.getEntity("data", new UserInfo());
                        if (userInfo != null) {
                            if (userInfo.getCont_save().equals("0")) {
                                setRightMenu(getString(R.string.save), R.color.colorBtn, 0, this);
                                meInfoEtNum.setText(userInfo.getUser_no());
                                meInfoEtTel.setText(userInfo.getPhone());
                                meInfoEtIdcard.setText(userInfo.getId_card());
                                meInfoEtAlipay.setText(userInfo.getAlipay());
                                meInfoEtWecha.setText(userInfo.getWechat());
                                meInfoEtBankName.setText(userInfo.getBank());
                                meInfoEtBankSwitch.setText(userInfo.getSub_bank());
                                meInfoEtName.setText(userInfo.getRealname());
                                meInfoEtBankNum.setText(userInfo.getBank_no());
                                if (!TextUtils.isEmpty(userInfo.getId_card())) {
                                    meInfoEtIdcard.setText(userInfo.getId_card());
                                    meInfoEtIdcard.setEnabled(false);
                                } else {
                                    meInfoEtIdcard.setEnabled(true);
                                    meInfoEtIdcard.setHint(getString(R.string.please_input));
                                }
                                if (!TextUtils.isEmpty(userInfo.getAlipay())) {
                                    meInfoEtAlipay.setText(userInfo.getAlipay());
                                    meInfoEtAlipay.setEnabled(false);
                                } else {
                                    meInfoEtAlipay.setEnabled(true);
                                    meInfoEtAlipay.setHint(getString(R.string.please_input));
                                }
                                if (!TextUtils.isEmpty(userInfo.getWechat())) {
                                    meInfoEtWecha.setText(userInfo.getWechat());
                                    meInfoEtWecha.setEnabled(false);
                                } else {
                                    meInfoEtWecha.setEnabled(true);
                                    meInfoEtWecha.setHint(getString(R.string.please_input));
                                }
                                if (!TextUtils.isEmpty(userInfo.getBank())) {
                                    meInfoEtBankName.setText(userInfo.getBank());
                                    meInfoEtBankName.setEnabled(false);
                                } else {
                                    meInfoEtBankName.setEnabled(true);
                                    meInfoEtBankName.setHint(getString(R.string.please_input));
                                }
                                if (!TextUtils.isEmpty(userInfo.getSub_bank())) {
                                    meInfoEtBankSwitch.setText(userInfo.getSub_bank());
                                    meInfoEtBankSwitch.setEnabled(false);
                                } else {
                                    meInfoEtBankSwitch.setEnabled(true);
                                    meInfoEtBankSwitch.setHint(getString(R.string.please_input));
                                }
                                if (!TextUtils.isEmpty(userInfo.getBank_no())) {
                                    meInfoEtBankNum.setText(userInfo.getBank_no());
                                    meInfoEtBankNum.setEnabled(false);
                                } else {
                                    meInfoEtBankNum.setEnabled(true);
                                    meInfoEtBankNum.setHint(getString(R.string.please_input));
                                }
                            } else if (userInfo.getCont_save().equals("1")) {
                                meInfoEtNum.setText(userInfo.getUser_no());
                                meInfoEtName.setText(userInfo.getRealname());
                                meInfoEtTel.setText(userInfo.getPhone());
                                meInfoEtIdcard.setText(userInfo.getId_card());
                                meInfoEtAlipay.setText(userInfo.getAlipay());
                                meInfoEtWecha.setText(userInfo.getWechat());
                                meInfoEtBankName.setText(userInfo.getBank());
                                meInfoEtBankSwitch.setText(userInfo.getSub_bank());
                                meInfoEtBankNum.setText(userInfo.getBank_no());
                                meInfoEtNum.setEnabled(false);
                                meInfoEtName.setEnabled(false);
                                meInfoEtTel.setEnabled(false);
                                meInfoEtIdcard.setEnabled(false);
                                meInfoEtAlipay.setEnabled(false);
                                meInfoEtWecha.setEnabled(false);
                                meInfoEtBankName.setEnabled(false);
                                meInfoEtBankSwitch.setEnabled(false);
                                meInfoEtBankNum.setEnabled(false);
                            }
                        }
                    } else if (t[0].equals("Me/setUser")) {
                        showShortToast(getString(R.string.save_success));
                        finish();
                    }
                } else if (jsonUtils.getCode().equals("405")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
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
}
