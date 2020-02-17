package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.constant.AppConstant;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.DensityUtil;
import cn.ouju.htt.utils.LanguageUtils;
import cn.ouju.htt.utils.UpdateApp;

/**
 * Created by Administrator on 2017/8/2.
 */

public abstract class BaseActivity extends AppCompatActivity implements HttpListener, View.OnClickListener {
    private LinearLayout common_title;
    private FrameLayout common_progress;
    public static final String TAG = "TAG";
    public Toolbar toolbar;
    public TextView title;
    private FrameLayout back;
    public Button menu;
    private View divider;
    protected MyHandler handler = new MyHandler(this);

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtils.setLocal(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        common_progress = findViewById(R.id.common_loading);
        common_title = findViewById(R.id.common_view);
        divider = findViewById(R.id.common_divider);
        initToolBar();
        initView();
        initData();

    }

    protected void initToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title = findViewById(R.id.toolbar_title);
        menu = findViewById(R.id.toolbar_menu);
        back = findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setChildContentView(int layout) {
        LinearLayout parent = findViewById(R.id.parent_common_content);
        View content = LayoutInflater.from(this).inflate(layout, null);
        parent.addView(content, getLayoutParams());
        ButterKnife.bind(this);
    }

    private FrameLayout.LayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    protected abstract void initView();

    protected abstract void initData();


    //显示toolbar
    public void showToolBar(int color) {
        common_title.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    //隐藏toolbar
    public void hideToolBar() {
        common_title.setVisibility(View.GONE);
    }

    /**
     * 设置标题，如果没有设置内容的话就隐藏标题
     *
     * @param text  标题内容
     * @param color 标题颜色
     */
    public void setTitle(String text, int color) {
        if (!TextUtils.isEmpty(text)) {
            showToolBar(android.R.color.white);
            title.setVisibility(View.VISIBLE);
            title.setText(text);
            if (color != 0) {
                title.setTextColor(ContextCompat.getColor(this, color));
            }
        } else
            title.setVisibility(View.GONE);
    }

    /**
     * 设置toolbar右边菜单，要么设置文字要么设置图标否则隐藏
     *
     * @param text  文字内容
     * @param color 文字颜色
     * @param resid 图片资源
     */
    public void setRightMenu(String text, int color, int resid, View.OnClickListener btnClick) {
        menu.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            menu.setText(text);
            if (color != 0) {
                menu.setTextColor(ContextCompat.getColor(this, color));
            }
            menu.setOnClickListener(btnClick);
        } else if (resid != 0) {
            menu.setBackgroundResource(resid);
            ViewGroup.LayoutParams params = menu.getLayoutParams();
            params.height = DensityUtil.dp2px(24);
            params.width = DensityUtil.dp2px(24);
            menu.setLayoutParams(params);
            menu.setOnClickListener(btnClick);
        } else
            menu.setVisibility(View.GONE);
    }


    //显示返回按钮
    public void showBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //隐藏返回按钮
    public void hideBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //
    public void showDivider() {
        divider.setVisibility(View.VISIBLE);
    }

    public void hideDivider() {
        divider.setVisibility(View.GONE);
    }

    //显示加载条
    public void showProgress() {
        common_progress.setVisibility(View.VISIBLE);
    }

    //隐藏加载进入条
    public void hideProgress() {
        common_progress.setVisibility(View.GONE);
    }

    public boolean isShow() {
        int i = common_progress.getVisibility();
        if (i == 0 || i == 4) {
            return true;
        }
        return false;
    }

    //无参跳转
    public void startActivity(Class cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    //带参跳转
    public void StartActivity(Class cls, Map<String, Object> params) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (params != null && !params.isEmpty()) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Integer) {
                    intent.putExtra(key, ((Integer) value).intValue());
                } else if (value instanceof Serializable) {
                    intent.putExtra(key, (Serializable) value);
                }
            }
        }
        startActivity(intent);
    }


    public void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        showShortToast(getResources().getString(R.string.network_fail));
    }

    public class MyHandler extends Handler {
        private WeakReference<BaseActivity> activity;

        public MyHandler(BaseActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity mActivity = activity.get();
            mActivity.hideProgress();
            if (msg.what == AppConstant.CONSTANT_NUMBER_ONE) {
                mActivity.onNetWorkedFail();
            } else if (msg.what == AppConstant.CONSTANT_NUMBER_TWO) {
                if (msg.obj != null && msg.obj instanceof List) {
                    List<Object> list = (List<Object>) msg.obj;
                    if (list.get(1) instanceof JsonUtils) {
                        JsonUtils jsonUtils = (JsonUtils) list.get(1);
                        if (jsonUtils.getCode().equals("405")) {
                           /* UserUtils.clearAll();
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setClass(BaseActivity.this, LoginActivity.class);
                            intent.putExtra("exit", true);
                            startActivity(intent);*/
                        } else if (jsonUtils.getCode().equals("501")) {
                            UpdateApp.update(BaseActivity.this, 1, jsonUtils);
                        } else if (jsonUtils.getCode().equals("502")) {

                        } else if (jsonUtils.getCode().equals("632")) {
                            new AlertDialog.Builder(BaseActivity.this).setMessage(jsonUtils.getMsg()).setCancelable(false).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(BaseActivity.this, MeInformationActivity.class);
                                    startActivity(intent);
                                }
                            }).create().show();

                        } else

                            mActivity.onNetWorkedSuccesses(list.get(0), list.get(1));
                    }
                }
            }
        }
    }
}
