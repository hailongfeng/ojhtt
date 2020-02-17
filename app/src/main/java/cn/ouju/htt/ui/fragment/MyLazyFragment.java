package cn.ouju.htt.ui.fragment;

import android.support.v4.app.Fragment;
import android.widget.AdapterView;

import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import cn.ouju.htt.http.HttpListener;


/**
 * Created by Administrator on 2017/11/10.
 */

public abstract class MyLazyFragment extends Fragment implements HttpListener,OnRefreshLoadMoreListener,AdapterView.OnItemClickListener {
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected abstract void onInvisible();

    protected abstract void onVisible();

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
