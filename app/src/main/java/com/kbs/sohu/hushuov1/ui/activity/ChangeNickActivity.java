package com.kbs.sohu.hushuov1.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.chat.EMClient;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.Model;

/**
 * Created by tarena on 2017/02/21.
 */

public class ChangeNickActivity extends AppCompatActivity {

    private EditText nickName;
    private Button save_nick;
    private String nick;
    private LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstnceState) {
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_change_nick);
        initView();
        initListener();
    }

    public static void actionStart(Context context, String data){
        Intent intent = new Intent(context,ChangeNickActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }

    private void initView() {
        nickName = (EditText) findViewById(R.id.et_nick);
        save_nick = (Button) findViewById(R.id.bt_save_nick);
        nickName.setText(getIntent().getStringExtra("data"));
        nickName.setSelection(nickName.getText().length());
    }

    private void initListener() {
        nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nick = s.toString();
            }
        });
        save_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().getUserAccountDao().updateAccount(EMClient.getInstance().getCurrentUser(),nick,null);
                Intent intent = new Intent("com.kbs.sohu.hushuov1.CHANGE_NICK");
                sendBroadcast(intent);
                finish();
            }
        });
    }
}
