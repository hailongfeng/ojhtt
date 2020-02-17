package cn.ouju.htt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.ui.activity.BaseActivity;
import cn.ouju.htt.ui.activity.LoginActivity;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2017/10/19.
 */

public abstract class BaseFragment extends Fragment implements HttpListener, View.OnClickListener {
    protected View contentView;
    private LayoutInflater inflater;
    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        initView();
        initData();
        return contentView;
    }


    protected abstract void initView();

    protected abstract void initData();

    protected void setContentView(int layoutId) {
        contentView = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, contentView);
    }

    @Override
    public void showProgress() {
        BaseActivity activity = (BaseActivity) getActivity();
       // activity.showProgress();
    }

    @Override
    public void hideProgress() {

    }

    public void showLongToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public boolean isLogin() {
        if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
            return false;
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

    }
}
