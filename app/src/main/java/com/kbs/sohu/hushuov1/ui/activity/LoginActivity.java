package com.kbs.sohu.hushuov1.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.model.bean.UserInfo;
import com.kbs.sohu.hushuov1.ui.widget.ClearWriteEditText;
import com.kbs.sohu.hushuov1.utils.AmUtil;
import com.kbs.sohu.hushuov1.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chensong on 2017/02/7.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_app_logo) ImageView small_logo;
    @BindView(R.id.et_login_account) ClearWriteEditText account;
    @BindView(R.id.et_login_password) ClearWriteEditText password;
    @BindView(R.id.bt_login_in)
    Button bt_login_in;
    @BindView(R.id.bt_login_register)
    Button bt_login_register;
    @BindView(R.id.tv_forget_password)
    TextView tv_forget_password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initListener();
        setChange();
    }

    private void initListener() {
        bt_login_in.setOnClickListener(this);
        bt_login_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    private void setChange() {
        account.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 11){
                    AmUtil.onInactive(LoginActivity.this,account);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_register:
                final Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_login_in:
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            login();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    public void login()throws Exception{
        // 1 获取输入的账号与密码
        final String name = account.getText().toString();
        final String pwd = password.getText().toString();

        // 2 校验输入的用户名和密码
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        // 3 登录逻辑处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //登录自己的服务器
                BmobUser user1 = new BmobUser();
                user1.setUsername(name);
                user1.setPassword(pwd);
                user1.login(new SaveListener<BmobUser>() {

                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            final UserInfo user2 = BmobUser.getCurrentUser(UserInfo.class);
                            EMClient.getInstance().login(name, user2.getPassword2(), new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    closeProgressDialog();
                                    Model.getInstance().loginSuccess(user2);
                                    Model.getInstance().getUserAccountDao().addAccount(user2);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 提示登录成功
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                            // 跳转到主页面
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            onDestroy();
                                        }
                                    });
                                }

                                @Override
                                public void onError(int i, String s) {
                                    // 提示登录失败
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onProgress(int i, String s) {
                                    showProgressDialog();
                                }
                            });
                        } else {
                            ToastUtil.show("密码有误");
                        }
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("正在登录");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
