package com.kbs.sohu.hushuov1.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.model.bean.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cs on 2017/02/13.
 */

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.et_query) EditText et_query;
    @BindView(R.id.iv_search_friend) ImageView iv_search_friend;
    @BindView(R.id.rl_add) RelativeLayout rl_add;
    @BindView(R.id.tv_add_name) TextView tv_add_name;
    @BindView(R.id.bt_add_add) Button bt_add_add;
    @BindView(R.id.view_friend_line) View view_line;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        iv_search_friend.setOnClickListener(this);
        bt_add_add.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_search_friend:
                find();
                break;
            case R.id.bt_add_add:
                add();
                break;
            default:
        }
    }

    private void find() {
        // 获取输入的用户名称
        final String name = et_query.getText().toString();

        // 校验输入的名称
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(AddFriendActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 去服务器判断当前用户是否存在
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去服务器判断当前查找的用户是否存在
                userInfo = new UserInfo(name);

                // 更新UI显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view_line.setVisibility(View.VISIBLE);
                        rl_add.setVisibility(View.VISIBLE);
                        tv_add_name.setText(userInfo.getName());
                    }
                });
            }
        });
    }

    private void add() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(), "添加好友");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddFriendActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddFriendActivity.this, "发送邀请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
