package cn.ouju.htt.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.TabPageAdapter;
import cn.ouju.htt.ui.fragment.OrderAllFragment;
import cn.ouju.htt.ui.fragment.OrderConfirmFragment;
import cn.ouju.htt.ui.fragment.OrderRejectFragment;
import cn.ouju.htt.ui.fragment.OrderYetFragment;

/**
 * Created by Administrator on 2018/2/3.
 */

public class OrderListActivity extends BaseActivity {
    @BindView(R.id.order_details_stl)
    SlidingTabLayout orderDetailsStl;
    @BindView(R.id.order_details_vp)
    ViewPager orderDetailsVp;
    private String[] tabs;
    private List<Fragment> fragments;
    private OrderAllFragment orderAllFragment;
    private OrderYetFragment orderYetFragment;
    private OrderConfirmFragment orderConfirmFragment;
    private OrderRejectFragment orderRejectFragment;
    private int positions = 0;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.me_order_manager), android.R.color.black);
        tabs = new String[]{getString(R.string.manager_all), getString(R.string.manager_order), getString(R.string.manager_confirm), getString(R.string.manager_reject)};
        orderAllFragment = OrderAllFragment.newInstance(this, handler);
        orderYetFragment = OrderYetFragment.newInstance(this, handler);
        orderConfirmFragment = OrderConfirmFragment.newInstance(this, handler);
        orderRejectFragment = OrderRejectFragment.newInstance(this, handler);
        fragments = new ArrayList<>();
        fragments.add(orderAllFragment);
        fragments.add(orderYetFragment);
        fragments.add(orderConfirmFragment);
        fragments.add(orderRejectFragment);
        TabPageAdapter adapter = new TabPageAdapter(getSupportFragmentManager(), tabs, fragments);
        orderDetailsVp.setOffscreenPageLimit(4);
        orderDetailsVp.setAdapter(adapter);
        orderDetailsStl.setViewPager(orderDetailsVp);
        orderDetailsVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positions = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        switch (positions) {
            case 0:
                orderAllFragment.onNetWorkedFail(strings);
                break;
            case 1:
                orderYetFragment.onNetWorkedFail(strings);
                break;
            case 2:
                orderConfirmFragment.onNetWorkedFail(strings);
                break;
            case 3:
                orderRejectFragment.onNetWorkedFail(strings);
                break;
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        switch (positions) {
            case 0:
                orderAllFragment.onNetWorkedSuccesses(t);
                break;
            case 1:
                orderYetFragment.onNetWorkedSuccesses(t);
                break;
            case 2:
                orderConfirmFragment.onNetWorkedSuccesses(t);
                break;
            case 3:
                orderRejectFragment.onNetWorkedSuccesses(t);
                break;
        }
    }

}
