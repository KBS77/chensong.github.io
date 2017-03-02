package com.kbs.sohu.hushuov1.fragment;

import android.content.Intent;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.kbs.sohu.hushuov1.activity.ChatActivity;

import java.util.List;

/**
 * Created by tarena on 2017/02/10.
 */

public class ChatFragment extends EaseConversationListFragment {

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        // 跳转到会话详情页面
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                // 传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());

                // 是否是否群聊
                if(conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                }

                startActivity(intent);
            }
        });

        // 请空集合数据
        conversationList.clear();

        // 监听会话消息
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    @Override
    public void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
        super.onDestroy();
    }

    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            // 设置数据
            for (EMMessage message : list) {
                message.setMsgTime(System.currentTimeMillis());
                //************接收并处理扩展消息***********************
                String userName = message.getStringAttribute("userName", "");
                String hxIdFrom = message.getFrom();
                EaseUser easeUser = new EaseUser(hxIdFrom);
                easeUser.setNick(userName);
            }
            EaseUI.getInstance().getNotifier().onNewMesg(list);
            // 刷新页面
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
}
