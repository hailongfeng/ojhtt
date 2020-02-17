package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.ui.fragment.BuyFragment;
import cn.ouju.htt.ui.fragment.MeFragment;
import cn.ouju.htt.ui.fragment.SellFragment;
import cn.ouju.htt.ui.fragment.StoresFragment;
import cn.ouju.htt.ui.fragment.TradingFragment;
import cn.ouju.htt.utils.DensityUtil;
import cn.ouju.htt.utils.UserUtils;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.main_buy_iv)
    ImageView mainBuyIv;
    @BindView(R.id.main_buy_tv)
    TextView mainBuyTv;
    @BindView(R.id.main_buy)
    LinearLayout mainBuy;
    @BindView(R.id.main_sell_iv)
    ImageView mainSellIv;
    @BindView(R.id.main_sell_tv)
    TextView mainSellTv;
    @BindView(R.id.main_sell)
    LinearLayout mainSell;
    @BindView(R.id.main_trading_iv)
    ImageView mainTradingIv;
    @BindView(R.id.main_trading_tv)
    TextView mainTradingTv;
    @BindView(R.id.main_trading)
    LinearLayout mainTrading;
    @BindView(R.id.main_me_iv)
    ImageView mainMeIv;
    @BindView(R.id.main_me_tv)
    TextView mainMeTv;
    @BindView(R.id.main_me)
    LinearLayout mainMe;
    @BindView(R.id.main_store_iv)
    ImageView mainStoreIv;
    @BindView(R.id.main_store_tv)
    TextView mainStoreTv;
    @BindView(R.id.main_store)
    LinearLayout mainStore;

    private boolean isShow;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_main);
        //  UpdateApp.update(this, 0, null);
        ButterKnife.bind(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(24), DensityUtil.dp2px(24));
        mainBuyIv.setLayoutParams(params);
        mainSellIv.setLayoutParams(params);
        mainTradingIv.setLayoutParams(params);
        mainMeIv.setLayoutParams(params);
        String tab = getIntent().getStringExtra("tab");
        if (!TextUtils.isEmpty(tab)) {
            if (tab.equals("0")) {
                performMyClick(0);
            } else if (tab.equals("1")) {
                performMyClick(1);
            } else if (tab.equals("2")) {
                performMyClick(2);
            } else if (tab.equals("3")) {
                performMyClick(3);
            } else if (tab.equals("4")) {
                performMyClick(4);
            }
        } else {
            mainSell.performClick();
        }
    }

    private int position;

    public void performMyClick(int tabPosition) {

        switch (tabPosition) {
            case 0:
                mainBuy.performClick();
                break;
            case 1:
                mainSell.performClick();
                break;
            case 2:
                mainTrading.performClick();
                break;
            case 3:
                mainMe.performClick();
                break;
            case 4:
                mainStore.performClick();
                break;
        }
    }

    public void performTrading() {
        String zh = UserUtils.getString("user_no", null);
        if (!TextUtils.isEmpty(zh)) {
            resetTab();
            position = 2;
            setTabState(mainTradingTv, mainTradingIv, R.drawable.trading_fill, R.color.colorBtn);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 10);
        }
        setFragment(position);
    }

    @Override
    protected void initData() {
        isShow = true;
    }

    private BuyFragment buyFragment;
    private SellFragment sellFragment;
    private TradingFragment tradingFragment;
    private MeFragment meFragment;
    private StoresFragment storesFragment;

    @Override
    @OnClick({R.id.main_buy, R.id.main_sell, R.id.main_store, R.id.main_trading, R.id.main_me})
    public void onClick(View view) {
        if (!isShow()) {
            String zh = UserUtils.getString("user_no", null);
            switch (view.getId()) {
                case R.id.main_sell:
                    /*if (!TextUtils.isEmpty(zh)) {*/
                    resetTab();
                    position = 0;
                    setTabState(mainSellTv, mainSellIv, R.drawable.sell_fill, R.color.colorBtn);
                   /* } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }*/

                    break;
                case R.id.main_buy:
                    resetTab();
                    position = 1;
                    setTabState(mainBuyTv, mainBuyIv, R.drawable.buy_fill, R.color.colorBtn);
                    break;

                case R.id.main_trading:
                    if (!TextUtils.isEmpty(zh)) {
                        resetTab();
                        position = 2;
                        setTabState(mainTradingTv, mainTradingIv, R.drawable.trading_fill, R.color.colorBtn);
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent, 10);
                    }
                    break;
                case R.id.main_me:
                    resetTab();
                    position = 3;
                    setTabState(mainMeTv, mainMeIv, R.drawable.me_fill, R.color.colorBtn);
                    break;
                case R.id.main_store:
                    resetTab();
                    position = 4;
                    setTabState(mainStoreTv, mainStoreIv, R.drawable.store_fill, R.color.colorBtn);
                    break;
            }
            setFragment(position);
        }
    }

    private void setFragment(int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                if (sellFragment == null) {
                    sellFragment = SellFragment.getInstance(this, handler);
                }
                if (buyFragment != null) {
                    transaction.hide(buyFragment);
                }
                if (tradingFragment != null) {
                    transaction.hide(tradingFragment);
                }
                if (storesFragment != null) {
                    transaction.hide(storesFragment);
                }
                if (meFragment != null) {
                    transaction.hide(meFragment);
                }
                if (!sellFragment.isAdded()) {
                    transaction.add(R.id.main_content, sellFragment);
                }
                transaction.show(sellFragment);

                break;
            case 1:
                if (buyFragment == null) {
                    buyFragment = BuyFragment.getInstance(this, handler);
                }
                if (sellFragment != null) {
                    transaction.hide(sellFragment);
                }
                if (tradingFragment != null) {
                    transaction.hide(tradingFragment);
                }
                if (meFragment != null) {
                    transaction.hide(meFragment);
                }
                if (storesFragment != null) {
                    transaction.hide(storesFragment);
                }
                if (!buyFragment.isAdded()) {
                    transaction.add(R.id.main_content, buyFragment);
                }
                transaction.show(buyFragment);
                break;
            case 2:
                if (tradingFragment == null) {
                    tradingFragment = TradingFragment.getInstance(this, handler);
                }
                if (buyFragment != null) {
                    transaction.hide(buyFragment);
                }
                if (sellFragment != null) {
                    transaction.hide(sellFragment);
                }
                if (storesFragment != null) {
                    transaction.hide(storesFragment);
                }
                if (meFragment != null) {
                    transaction.hide(meFragment);
                }
                if (!tradingFragment.isAdded()) {
                    transaction.add(R.id.main_content, tradingFragment);
                }
                transaction.show(tradingFragment);
                break;
            case 3:
                if (meFragment == null) {
                    meFragment = MeFragment.getInstance(this, handler, isShow);
                }
                if (buyFragment != null) {
                    transaction.hide(buyFragment);
                }
                if (sellFragment != null) {
                    transaction.hide(sellFragment);
                }
                if (storesFragment != null) {
                    transaction.hide(storesFragment);
                }
                if (tradingFragment != null) {
                    transaction.hide(tradingFragment);
                }
                if (!meFragment.isAdded()) {
                    transaction.add(R.id.main_content, meFragment);
                }
                transaction.show(meFragment);
                break;
            case 4:
                if (storesFragment == null) {
                    storesFragment = StoresFragment.getInstance(this, handler);
                }
                if (buyFragment != null) {
                    transaction.hide(buyFragment);
                }
                if (sellFragment != null) {
                    transaction.hide(sellFragment);
                }
                if (tradingFragment != null) {
                    transaction.hide(tradingFragment);
                }
                if (meFragment != null) {
                    transaction.hide(meFragment);
                }
                if (!storesFragment.isAdded()) {
                    transaction.add(R.id.main_content, storesFragment);
                }
                transaction.show(storesFragment);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        switch (position) {
            case 0:
                sellFragment.onNetWorkedSuccesses(t);

                break;
            case 1:
                buyFragment.onNetWorkedSuccesses(t);
                break;
            case 2:
                tradingFragment.onNetWorkedSuccesses(t);

                break;
            case 3:
                meFragment.onNetWorkedSuccesses(t);
                break;
            case 4:
                storesFragment.onNetWorkedSuccesses(t);
                break;
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        switch (position) {
            case 0:
                sellFragment.onNetWorkedFail(strings);

                break;
            case 1:
                buyFragment.onNetWorkedFail(strings);
                break;
            case 2:
                tradingFragment.onNetWorkedFail(strings);
                break;
            case 3:
                meFragment.onNetWorkedFail(strings);
                break;
            case 4:
                storesFragment.onNetWorkedFail(strings);
                break;
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    private void resetTab() {
        setTabState(mainBuyTv, mainBuyIv, R.drawable.buy, R.color.colorGrayText);
        setTabState(mainSellTv, mainSellIv, R.drawable.sell, R.color.colorGrayText);
        setTabState(mainTradingTv, mainTradingIv, R.drawable.trading, R.color.colorGrayText);
        setTabState(mainMeTv, mainMeIv, R.drawable.me, R.color.colorGrayText);
        setTabState(mainStoreTv, mainStoreIv, R.drawable.store, R.color.colorGrayText);
    }

    private void setTabState(TextView tv, ImageView iv, int imgRes, int color) {
        tv.setTextColor(ContextCompat.getColor(this, color));
        iv.setImageResource(imgRes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 20) {
                mainMe.performClick();
            }
        }

    }

}
