package com.kbs.sohu.hushuov1.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.utils.BmobUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.et_register_phone) EditText phoneEdit;
    @BindView(R.id.et_register_password) EditText pswEdit;
    @BindView(R.id.et_register_yanzheng) EditText yanzhengEdit;
    private RequestQueue queue;
    String registerPhone,registerPsw,verification_code;
    private int i = 60;
    @BindView(R.id.iv_register_background) ImageView blurRegisterIm;
    @BindView(R.id.btn_getSMS) Button btn_getSMS;
    @BindView(R.id.btn_register) Button btn_register;
    @BindView(R.id.btn_backTolg) Button btn_backToLg;
    @BindView(R.id.ib_registerPhe_clear) ImageButton phoneClear;
    @BindView(R.id.ib_registerPwt_clear) ImageButton passwordClear;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btn_getSMS.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btn_getSMS.setText("获取验证码");
                btn_getSMS.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Toast.makeText(RegisterActivity.this, "验证成功",
                                Toast.LENGTH_SHORT).show();
                        requestzhucexinxi();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(RegisterActivity.this, "验证已发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "验证错误",
                                Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "验证失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        loadBackground();
        setChange();
        queue = Volley.newRequestQueue(RegisterActivity.this);
        btn_getSMS.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void loadBackground(){
        Glide.with(this).load(R.drawable.login_background1)
                .bitmapTransform(new BlurTransformation(this,20),new CenterCrop(this))
                .into(blurRegisterIm);
    }

    private void setChange() {
        phoneEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    phoneClear.setVisibility(View.VISIBLE);
                } else {
                    phoneClear.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                phoneClear.setVisibility(View.INVISIBLE);
            }
        });
        phoneClear.setOnClickListener(this);
        pswEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    passwordClear.setVisibility(View.VISIBLE);
                } else {
                    passwordClear.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                passwordClear.setVisibility(View.INVISIBLE);
            }
        });
        passwordClear.setOnClickListener(this);
        btn_backToLg.setOnClickListener(this);
        SMSSDK.initSDK(RegisterActivity.this, "1b49882f8b9c6", "95de648756bfede6a3d1e5c22a531d39");
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_getSMS:
                getSMS();
                break;
            case R.id.btn_register:
                regiseter();
                break;
            case R.id.ib_registerPhe_clear:
                phoneEdit.getText().clear();
                break;
            case R.id.ib_registerPwt_clear:
                pswEdit.getText().clear();
                break;
            case R.id.btn_backTolg:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    private void requestzhucexinxi() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // 去环信服务器注册账号
                    EMClient.getInstance().createAccount(registerPhone,registerPsw);
                    //将个人信息存储至云端
                    BmobUtil.getInstance().addUser(registerPhone,registerPsw);
                    // 更新页面显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


//
    }

    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(RegisterActivity.this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str 输入的字符串
     * @param length 长度
     * @return 字符串是否匹配
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {

        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    private void getSMS() {
        registerPhone = phoneEdit.getText().toString().trim();
        // 1. 通过规则判断手机号
        if (!judgePhoneNums(registerPhone)) {
            return;
        }
        SMSSDK.getVerificationCode("86", registerPhone);
        btn_getSMS.setClickable(false);
        btn_getSMS.setText("再次输入倒计时" + "(" + i + ")");
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(-9);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-8);
            }
        });
    }

    private void regiseter() {
        verification_code = yanzhengEdit.getText().toString();
        registerPhone = phoneEdit.getText().toString();
        registerPsw = pswEdit.getText().toString();
        if (verification_code == null || verification_code.length() <= 0) {
            Toast.makeText(RegisterActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (registerPhone == null || registerPhone.length() <= 0) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!registerPsw.matches(".{6,20}")) {
            Toast.makeText(RegisterActivity.this, "密码必须是6-20位",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        SMSSDK.submitVerificationCode("86", registerPhone, verification_code);
    }
}
