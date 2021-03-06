package com.fanwe.live;

import android.app.Application;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.lib.player.SDMediaPlayer;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.EImOnRefresh;
import com.fanwe.live.event.ESDMediaPlayerStateChanged;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.model.custommsg.TIMMsgModel;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMConversation;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMRefreshListener;
import com.tencent.TIMUserStatusListener;
import com.tencent.TIMValueCallBack;

import java.util.List;

public class LiveIniter
{

    public void init(Application app)
    {
        TIMManager.getInstance().addMessageListener(new TIMMessageListener()
        {

            private void postNewMessage(MsgModel msgModel)
            {
                EImOnNewMessages event = new EImOnNewMessages();
                event.msg = msgModel;
                SDEventManager.post(event);

                if (msgModel.isPrivateMsg())
                {
                    IMHelper.postERefreshMsgUnReaded();
                }
            }

            @Override
            public boolean onNewMessages(final List<TIMMessage> listMessage)
            {
                if (!SDCollectionUtil.isEmpty(listMessage))
                {
                    SDHandlerManager.getBackgroundHandler().post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (TIMMessage msg : listMessage)
                            {
                                if (ApkConstant.DEBUG)
                                {
                                    TIMConversation conversation = msg.getConversation();
                                    LogUtil.i("--------receive msg:" + conversation.getType() + " " + conversation.getPeer());
                                }

                                boolean post = true;
                                final TIMMsgModel msgModel = new TIMMsgModel(msg, true);

                                if (msgModel.getConversationPeer().equals(LiveInformation.getInstance().getCurrentChatPeer()))
                                {
                                    IMHelper.setSingleC2CReadMessage(msgModel.getConversationPeer(), false);
                                }

                                boolean needDownloadSound = msgModel.checkSoundFile(new TIMValueCallBack<String>()
                                {
                                    @Override
                                    public void onError(int i, String s)
                                    {
                                    }

                                    @Override
                                    public void onSuccess(String path)
                                    {
                                        msgModel.getCustomMsgPrivateVoice().setPath(path);
                                        postNewMessage(msgModel);
                                    }
                                });
                                if (needDownloadSound)
                                {
                                    post = false;
                                }


                                if (post)
                                {
                                    postNewMessage(msgModel);
                                }
                            }
                        }
                    });
                }
                return false;
            }
        });
        TIMManager.getInstance().setRefreshListener(new TIMRefreshListener()
        {

            @Override
            public void onRefresh()
            {
                // ????????????????????????????????????????????????????????????????????????????????????ImSDK?????????????????????????????????????????????????????????????????????onRefresh??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                SDEventManager.post(new EImOnRefresh());
            }

            @Override
            public void onRefreshConversation(List<TIMConversation> list)
            {
            }
        });
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener()
        {
            @Override
            public void onForceOffline()
            {
                SDEventManager.post(new EImOnForceOffline());
            }

            @Override
            public void onUserSigExpired()
            {
                CommonInterface.requestUsersig(null);
            }
        });

        TIMManager.getInstance().setLogLevel(TIMLogLevel.OFF);
        TIMManager.getInstance().init(app);
        TIMManager.getInstance().setLogPrintEnable(false);
        LogUtil.i("imsdk version:" + TIMManager.getInstance().getVersion());

        SDMediaPlayer.getInstance().setOnStateChangeCallback(new SDMediaPlayer.OnStateChangeCallback()
        {
            @Override
            public void onStateChanged(SDMediaPlayer.State oldState, SDMediaPlayer.State newState, SDMediaPlayer player)
            {
                LogUtil.i("onStateChanged:" + newState);
                SDEventManager.post(new ESDMediaPlayerStateChanged(newState));
            }
        });
    }

}
