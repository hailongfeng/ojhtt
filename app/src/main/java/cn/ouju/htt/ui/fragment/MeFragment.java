package cn.ouju.htt.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.UserBean;
import cn.ouju.htt.broadcast.mBroadCastReceiver;
import cn.ouju.htt.constant.AppConstant;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.popupwindow.EmailWindow;
import cn.ouju.htt.popupwindow.LanguageWindow;
import cn.ouju.htt.ui.activity.AboutActivity;
import cn.ouju.htt.ui.activity.BaseActivity;
import cn.ouju.htt.ui.activity.LoginActivity;
import cn.ouju.htt.ui.activity.MainActivity;
import cn.ouju.htt.ui.activity.MeAddressActivity;
import cn.ouju.htt.ui.activity.MeInformationActivity;
import cn.ouju.htt.ui.activity.MeMiningActivity;
import cn.ouju.htt.ui.activity.MeNewsActivity;
import cn.ouju.htt.ui.activity.MeReCodeActivity;
import cn.ouju.htt.ui.activity.MeReleaseActivity;
import cn.ouju.htt.ui.activity.MeSendEmailActivity;
import cn.ouju.htt.ui.activity.MeSettingActivity;
import cn.ouju.htt.ui.activity.MeTeamActivity;
import cn.ouju.htt.ui.activity.MeTokenActivity;
import cn.ouju.htt.ui.activity.MeUpdateActivity;
import cn.ouju.htt.ui.activity.MeWriteEmailActivity;
import cn.ouju.htt.ui.activity.OrderListActivity;
import cn.ouju.htt.ui.activity.StoreBagActivity;
import cn.ouju.htt.ui.view.MyLinearLayout;
import cn.ouju.htt.utils.UserUtils;

public class MeFragment extends BaseFragment {
    private static HttpListener listeners;
    private static BaseActivity.MyHandler handlers;

    @BindView(R.id.me_email)
    ImageView meEmail;
    @BindView(R.id.me_login)
    TextView meLogin;
    @BindView(R.id.me_login_ll)
    LinearLayout meLoginLl;
    @BindView(R.id.me_tv_exit)
    TextView meTvExit;
    @BindView(R.id.me_tv_num)
    TextView meTvNum;
    @BindView(R.id.me_tv_zc)
    TextView meTvZc;
    @BindView(R.id.me_tv_sf)
    TextView meTvSf;
    @BindView(R.id.me_language)
    ImageView meLanguage;
    @BindView(R.id.me_grid_order)
    MyLinearLayout meGridOrder;
    @BindView(R.id.me_grid_bag)
    MyLinearLayout meGridBag;
    @BindView(R.id.me_grid_address)
    MyLinearLayout meGridAddress;
    @BindView(R.id.me_grid_release)
    MyLinearLayout meGridRelease;
    @BindView(R.id.me_grid_team)
    MyLinearLayout meGridTeam;
    @BindView(R.id.me_grid_recode)
    MyLinearLayout meGridRecode;
    @BindView(R.id.me_grid_token)
    MyLinearLayout meGridToken;
    @BindView(R.id.me_grid_information)
    MyLinearLayout meGridInformation;
    @BindView(R.id.me_grid_news)
    MyLinearLayout meGridNews;
    /*  @BindView(R.id.me_grid_psw)
      MyLinearLayout meGridPsw;
      @BindView(R.id.me_grid_safe)
      MyLinearLayout meGridSafe;*/
    @BindView(R.id.me_grid_about)
    MyLinearLayout meGridAbout;
    private EmailWindow emailWindow;
    private LanguageWindow languageWindow;
    private mBroadCastReceiver receiver;

    public static MeFragment getInstance(HttpListener listener, BaseActivity.MyHandler handler, boolean b) {

        MeFragment meFragment = new MeFragment();
        listeners = listener;
        handlers = handler;
        return meFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                Map<String, Object> params = new HashMap<>();
                params.put("user_no", UserUtils.getString("user_no", null));
                CommonService.query("Me/home", params, null, listeners, handlers);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
            Map<String, Object> params = new HashMap<>();
            params.put("user_no", UserUtils.getString("user_no", null));
            CommonService.query("Me/home", params, null, listeners, handlers);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_me);
        emailWindow = new EmailWindow(getContext(), this);
        languageWindow = new LanguageWindow(getContext(), this);
        receiver = new mBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.me");
        getActivity().registerReceiver(receiver, intentFilter);
        receiver.setCallback(new mBroadCastReceiver.CallBack() {
            @Override
            public void refresh() {
                Map<String, Object> params = new HashMap<>();
                params.put("user_no", UserUtils.getString("user_no", null));
                CommonService.query("Me/home", params, null, listeners, handlers);
            }
        });

    }

    @Override
    protected void initData() {
        updateInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    private void updateInfo() {
        if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
            meTvExit.setVisibility(View.VISIBLE);
            meLoginLl.setVisibility(View.VISIBLE);
            meLogin.setVisibility(View.GONE);
        } else {
            meTvExit.setVisibility(View.GONE);
            meLoginLl.setVisibility(View.GONE);
            meLogin.setVisibility(View.VISIBLE);
        }
        meTvNum.setText(UserUtils.getString("user_no", null));
        meTvZc.setText(UserUtils.getString("dti_account", "0.00000000"));
        meTvSf.setText(UserUtils.getString("rb_account", "0.00000000"));
    }

    @Override
    @OnClick({R.id.me_email, R.id.me_login, R.id.me_grid_setting, R.id.me_grid_mining, R.id.me_tv_exit, R.id.me_language, R.id.me_grid_order, R.id.me_grid_bag, R.id.me_grid_address, R.id.me_grid_release, R.id.me_grid_team, R.id.me_grid_recode, R.id.me_grid_token, R.id.me_grid_information, R.id.me_grid_news, R.id.me_grid_about})
    public void onClick(View view) {
        if (view.getId() == R.id.me_email) {
            if (emailWindow.isShowing()) {
                emailWindow.dismiss();
            } else
                emailWindow.showAsDropDown(meEmail, -meEmail.getWidth() - 30, 0);
        }
        if (view.getId() == R.id.me_tv_exit) {
            new AlertDialog.Builder(getContext()).setMessage(getString(R.string.confirm_exit)).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserUtils.clearAll();
                    updateInfo();
                }
            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
        }
        if (view.getId() == R.id.me_language) {
            if (languageWindow.isShowing()) {
                languageWindow.dismiss();
            } else
                languageWindow.showAsDropDown(meLanguage, -25, 0);
        }
        //中文
        if (view.getId() == R.id.language_cn) {
            languageWindow.dismiss();
            if (getActivity() != null) {
                SharedPreferences sp = getActivity().getSharedPreferences(AppConstant.LANGUAGE_LOCALE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("language", "zh-cn");
                edit.apply();
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Process.killProcess(Process.myPid());
            System.exit(0);

        }
        //英文
        if (view.getId() == R.id.language_en_us) {
            languageWindow.dismiss();
            if (getActivity() != null) {
                SharedPreferences sp = getActivity().getSharedPreferences(AppConstant.LANGUAGE_LOCALE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("language", "en-us");
                edit.apply();
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
        //泰语
        if (view.getId() == R.id.language_th) {
            languageWindow.dismiss();
            if (getActivity() != null) {
                SharedPreferences sp = getActivity().getSharedPreferences(AppConstant.LANGUAGE_LOCALE, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("language", "th-th");
                edit.apply();
            }
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Process.killProcess(Process.myPid());
            System.exit(0);
        }
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.email_send:
                if (isLogin()) {
                    return;
                }
                if (emailWindow.isShowing()) {
                    emailWindow.dismiss();
                }
                if (getActivity() != null) {
                    intent.setClass(getActivity(), MeSendEmailActivity.class);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }
                break;
            case R.id.email_write:
                if (isLogin()) {
                    return;
                }
                if (emailWindow.isShowing()) {
                    emailWindow.dismiss();
                }
                if (getActivity() != null) {
                    intent.setClass(getActivity(), MeWriteEmailActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.email_receive:
                if (isLogin()) {
                    return;
                }
                if (emailWindow.isShowing()) {
                    emailWindow.dismiss();
                }
                if (getActivity() != null) {
                    intent.setClass(getActivity(), MeSendEmailActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
                break;
            case R.id.me_login:
                if (isLogin()) {
                    return;
                }
                if (getActivity() != null) {
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        if (getContext() == null) {
            return;
        }
        if (isLogin()) {
            return;
        }
        switch (view.getId()) {
            case R.id.me_grid_order:
                intent.setClass(getContext(), OrderListActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_bag:
                intent.setClass(getContext(), StoreBagActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_address:
                intent.setClass(getContext(), MeAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_release:
                intent.setClass(getContext(), MeReleaseActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_team:
                intent.setClass(getContext(), MeTeamActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_recode:
                intent.setClass(getContext(), MeReCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_token:
                intent.setClass(getContext(), MeTokenActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_information:
                intent.setClass(getContext(), MeInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_news:
                intent.setClass(getContext(), MeNewsActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_setting:
                intent.setClass(getContext(), MeSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_mining:
                intent.setClass(getContext(), MeMiningActivity.class);
                startActivity(intent);
                break;
            case R.id.me_grid_about:
                intent.setClass(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Me/home")) {
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
                        updateInfo();
                    }
                } else
                    showShortToast(jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        showShortToast(getResources().getString(R.string.network_fail));
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

}
