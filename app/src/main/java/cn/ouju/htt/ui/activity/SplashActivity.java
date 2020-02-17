package cn.ouju.htt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import butterknife.BindView;
import cn.ouju.htt.R;
import cn.ouju.htt.v2.activity.StartActivity;
import cn.ouju.htt.v2.activity.XszBaseActivity;
import cn.ouju.htt.v2.activity.XszWebViewActivity;

/**
 * Created by Administrator on 2017/11/15.
 */

public class SplashActivity extends XszBaseActivity {

    @BindView(R.id.ll_splash)
    LinearLayout splashLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    public void initView() {
        //splashLl=findViewById(R.id.ll_splash);
    }

    @Override
    public void initData() {

        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(2000);
        splashLl.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent intent=new Intent(context,StartActivity.class);
                //intent.putExtra("index",position);
                toActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {

    }


}
