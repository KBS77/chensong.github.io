package com.kbs.sohu.hushuov1.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.model.bean.UserInfo;
import com.kbs.sohu.hushuov1.presenter.Impl.SplashPresenter;
import com.kbs.sohu.hushuov1.ui.view.ISplashView;
import com.kbs.sohu.hushuov1.utils.handler.HandlerUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chensong on 2017/02/6.
 */

public class SplashActivity extends AppCompatActivity implements ISplashView{

    @BindView(R.id.spalsh_daojishi) TextView daojishi;
    @BindView(R.id.spalsh_image) ImageView adImg;

    private myCountDownTimer mc;
    private Runnable runnable;
    private SplashPresenter splashPresenter = new SplashPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        splashPresenter.loadAdPic();
        mc = new myCountDownTimer(3200,1000);
        mc.start();
        setCountDown();
    }

    @Override
    public void updateAd(String AdPic) {
        Glide.with(this).load(AdPic).into(adImg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            toMainOrLogin();
            if(runnable != null)
                HandlerUtil.getInstance(this).removeCallbacks(runnable);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HandlerUtil.getInstance(this).removeCallbacks(runnable);
    }

    class myCountDownTimer extends CountDownTimer {

        public myCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            daojishi.setText("跳过"+(millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {

        }
    }
        // 判断进入主页面还是登录页面
    private void toMainOrLogin() {

        // 判断当前账号是否已经登录过
        if(EMClient.getInstance().isLoggedInBefore()) {// 登录过

            // 获取到当前登录用户的信息
            UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());

            if(account == null) {
                    // 跳转到登录页面
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                // 登录成功后的方法
                Model.getInstance().loginSuccess(account);

                // 跳转到主页面
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

        }else {// 没登录过
            // 跳转到登录页面
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // 结束当前页面
        finish();
    }

    private void setCountDown(){
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HandlerUtil.getInstance(SplashActivity.this).postDelayed(runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isFinishing()) {
                            return;
                        }
                        toMainOrLogin();
                    }
                },3000);
                Looper.loop();
            }
        });
    }

}

