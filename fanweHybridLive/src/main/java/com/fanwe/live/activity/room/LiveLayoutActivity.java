package com.fanwe.live.activity.room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fanwe.games.model.PluginModel;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDViewVisibilityCallback;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDKeyboardListener;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDReplaceableLayout;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomGiftGifView;
import com.fanwe.live.appview.room.RoomGiftPlayView;
import com.fanwe.live.appview.room.RoomHeartView;
import com.fanwe.live.appview.room.RoomInfoView;
import com.fanwe.live.appview.room.RoomInviteFriendsView;
import com.fanwe.live.appview.room.RoomMsgView;
import com.fanwe.live.appview.room.RoomPopMsgView;
import com.fanwe.live.appview.room.RoomRemoveViewerView;
import com.fanwe.live.appview.room.RoomSendMsgView;
import com.fanwe.live.appview.room.RoomViewerJoinRoomView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveAddViewerDialog;
import com.fanwe.live.dialog.LiveChatC2CDialog;
import com.fanwe.live.dialog.LiveRechargeDialog;
import com.fanwe.live.dialog.LiveRedEnvelopeNewDialog;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.dialog.common.AppDialogMenu;
import com.fanwe.live.model.App_AdminModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_plugin_statusActModel;
import com.fanwe.live.model.LiveQualityData;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * ????????????
 */
public class LiveLayoutActivity extends LiveActivity
{
    protected RoomInfoView mRoomInfoView;
    protected RoomGiftPlayView mRoomGiftPlayView;
    protected RoomPopMsgView mRoomPopMsgView;
    protected RoomViewerJoinRoomView mRoomViewerJoinRoomView;
    protected RoomMsgView mRoomMsgView;
    protected RoomSendMsgView mRoomSendMsgView;
    protected RoomHeartView mRoomHeartView;
    protected RoomGiftGifView mRoomGiftGifView;

    protected RoomInviteFriendsView mRoomInviteFriendsView;
    protected RoomRemoveViewerView mRoomRemoveViewerView;

    private RelativeLayout rl_root_layout; // UI??????????????????
    private SDReplaceableLayout fl_bottom_extend; // ????????????

    private SDKeyboardListener mKeyboardListener = new SDKeyboardListener();

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);

        // ??????????????????
        findViewById(R.id.view_close_room).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickCloseRoom(v);
            }
        });

        mKeyboardListener.setActivity(this).setKeyboardVisibilityCallback(new SDKeyboardListener.SDKeyboardVisibilityCallback()
        {
            @Override
            public void onKeyboardVisibilityChange(boolean visible, int height)
            {
                if (rl_root_layout != null)
                {
                    if (visible)
                    {
                        rl_root_layout.scrollBy(0, height);
                    } else
                    {
                        rl_root_layout.scrollTo(0, 0);
                        rl_root_layout.requestLayout();
                        showSendMsgView(false);
                    }
                }
            }
        });
    }

    protected void initLayout(View view)
    {
        rl_root_layout = (RelativeLayout) view.findViewById(R.id.rl_root_layout);
        initBottomExtend();

        addRoomInfoView();
        addRoomGiftPlayView();
        addRoomGiftGifView();
        addRoomPopMsgView();
        addRoomViewerJoinRoomView();
        addRoomMsgView();
        addRoomSendMsgView();
        addRoomHeartView();
        addRoomBottomView();
    }

    /**
     * ?????????????????????
     */
    private void initBottomExtend()
    {
        fl_bottom_extend = (SDReplaceableLayout) findViewById(R.id.fl_bottom_extend);
        if (fl_bottom_extend != null)
        {
            fl_bottom_extend.addCallback(new SDReplaceableLayout.SDReplaceableLayoutCallback()
            {
                @Override
                public void onContentReplaced(View view)
                {
                    showBottomExtendSwitch(true);
                    onContentVisibilityChanged(view, view.getVisibility());
                }

                @Override
                public void onContentRemoved(View view)
                {
                    showBottomExtendSwitch(false);
                }

                @Override
                public void onContentVisibilityChanged(View view, int visibility)
                {
                    if (View.VISIBLE == visibility)
                    {
                        onShowBottomExtend();
                    } else
                    {
                        onHideBottomExtend();
                    }
                }
            });
        }
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    protected void replaceBottomExtend(View view)
    {
        fl_bottom_extend.replaceContent(view);
    }

    /**
     * ??????????????????????????????
     */
    protected void toggleBottomExtend()
    {
        fl_bottom_extend.toggleContentVisibleOrGone();
    }

    /**
     * ????????????????????????
     */
    protected void onShowBottomExtend()
    {
        LogUtil.i("onShowBottomExtend");
    }

    /**
     * ????????????????????????
     */
    protected void onHideBottomExtend()
    {
        LogUtil.i("onHideBottomExtend");
    }

    /**
     * ??????????????????????????????
     *
     * @param show
     */
    protected void showBottomExtendSwitch(boolean show)
    {
        // ????????????
        LogUtil.i("showBottomExtendSwitch:" + show);
    }

    /**
     * ???????????????view??????
     *
     * @param view
     */
    protected void onShowSendGiftView(View view)
    {
        showBottomView(false);
        showMsgView(false);
    }

    /**
     * ???????????????view??????
     *
     * @param view
     */
    protected void onHideSendGiftView(View view)
    {
        showBottomView(true);
        showMsgView(true);
    }

    /**
     * ???????????????view??????
     *
     * @param view
     */
    protected void onShowSendMsgView(View view)
    {
        showBottomView(false);
    }

    /**
     * ???????????????view??????
     *
     * @param view
     */
    protected void onHideSendMsgView(View view)
    {
        showBottomView(true);
    }

    /**
     * ????????????
     */
    protected void addRoomInfoView()
    {
        if (mRoomInfoView == null)
        {
            mRoomInfoView = new RoomInfoView(this);
            mRoomInfoView.setClickListener(roomInfoClickListener);
            replaceView(R.id.fl_live_room_info, mRoomInfoView);
        }
    }

    /**
     * ????????????
     */
    protected void addRoomGiftPlayView()
    {
        if (mRoomGiftPlayView == null)
        {
            mRoomGiftPlayView = new RoomGiftPlayView(this);
            replaceView(R.id.fl_live_gift_play, mRoomGiftPlayView);
        }
    }

    /**
     * gif??????
     */
    protected void addRoomGiftGifView()
    {
        if (mRoomGiftGifView == null)
        {
            mRoomGiftGifView = new RoomGiftGifView(this);
            replaceView(R.id.fl_live_gift_gif, mRoomGiftGifView);
        }
    }

    /**
     * ??????
     */
    protected void addRoomPopMsgView()
    {
        if (mRoomPopMsgView == null)
        {
            mRoomPopMsgView = new RoomPopMsgView(this);
            replaceView(R.id.fl_live_pop_msg, mRoomPopMsgView);
        }
    }

    /**
     * ????????????
     */
    protected void addRoomViewerJoinRoomView()
    {
        if (mRoomViewerJoinRoomView == null)
        {
            mRoomViewerJoinRoomView = new RoomViewerJoinRoomView(this);
            replaceView(R.id.fl_live_viewer_join_room, mRoomViewerJoinRoomView);
        }
    }

    /**
     * ????????????
     */
    protected void addRoomMsgView()
    {
        if (mRoomMsgView == null)
        {
            mRoomMsgView = new RoomMsgView(this);
            mRoomMsgView.setLayoutParams(new ViewGroup.LayoutParams(
                    SDViewUtil.getScreenWidthPercent(0.784f),
                    SDViewUtil.getScreenHeightPercent(0.254f)));
            replaceView(R.id.fl_live_msg, mRoomMsgView);
        }
    }

    /**
     * ????????????
     */
    protected void addRoomSendMsgView()
    {
        if (mRoomSendMsgView == null)
        {
            mRoomSendMsgView = new RoomSendMsgView(this);
            mRoomSendMsgView.addVisibilityCallback(new SDViewVisibilityCallback()
            {
                @Override
                public void onViewVisibilityChanged(View view, int visibility)
                {
                    if (View.VISIBLE == visibility)
                    {
                        onShowSendMsgView(view);
                    } else
                    {
                        onHideSendMsgView(view);
                    }
                }
            });
            replaceView(R.id.fl_live_send_msg, mRoomSendMsgView);
        }
    }

    /**
     * ??????
     */
    protected void addRoomHeartView()
    {
        if (mRoomHeartView == null)
        {
            mRoomHeartView = new RoomHeartView(this);
            mRoomHeartView.setLayoutParams(new ViewGroup.LayoutParams(
                    SDResourcesUtil.getDimensionPixelSize(R.dimen.width_live_bottom_menu) * 3,
                    SDViewUtil.getScreenWidthPercent(0.5f)));
            replaceView(R.id.fl_live_heart, mRoomHeartView);
        }
    }

    /**
     * ????????????
     */
    protected void addRoomBottomView()
    {
        //????????????
    }

    /**
     * ??????????????????
     */
    protected void addRoomPrivateRemoveViewerView()
    {
        if (mRoomRemoveViewerView == null)
        {
            mRoomRemoveViewerView = new RoomRemoveViewerView(LiveLayoutActivity.this);
            mRoomRemoveViewerView.setRoomId(getRoomId());
            mRoomRemoveViewerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
            {
                @Override
                public void onViewAttachedToWindow(View v)
                {
                    mRoomRemoveViewerView.requestData(false);
                }

                @Override
                public void onViewDetachedFromWindow(View v)
                {
                    mRoomRemoveViewerView = null;
                }
            });
            addView(mRoomRemoveViewerView);
        }
    }

    /**
     * ????????????
     */
    protected void addLiveFinish()
    {
        // ????????????
    }

    /**
     * ????????????view????????????
     */
    private RoomInfoView.ClickListener roomInfoClickListener = new RoomInfoView.ClickListener()
    {
        @Override
        public void onClickAddViewer(View v)
        {
            LiveLayoutActivity.this.onClickAddViewer(v);
        }

        @Override
        public void onClickMinusViewer(View v)
        {
            LiveLayoutActivity.this.onClickMinusViewer(v);
        }
    };

    /**
     * ??????????????????????????????
     *
     * @param v
     */
    protected void onClickAddViewer(View v)
    {
        LiveAddViewerDialog dialog = new LiveAddViewerDialog(this, getRoomInfo().getPrivate_share());
        dialog.setCallback(new LiveAddViewerDialog.Callback()
        {
            @Override
            public void onClickShareFriends(View v)
            {
                if (mRoomInviteFriendsView == null)
                {
                    mRoomInviteFriendsView = new RoomInviteFriendsView(LiveLayoutActivity.this);
                    mRoomInviteFriendsView.setRoomId(getRoomId());
                    mRoomInviteFriendsView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
                    {
                        @Override
                        public void onViewAttachedToWindow(View v)
                        {
                            mRoomInviteFriendsView.requestData(false);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v)
                        {
                            mRoomInviteFriendsView = null;
                        }
                    });
                    addView(mRoomInviteFriendsView);
                }
            }
        });
        dialog.showBottom();
    }

    /**
     * ??????????????????????????????
     *
     * @param v
     */
    protected void onClickMinusViewer(View v)
    {
        addRoomPrivateRemoveViewerView();
    }

    @Override
    public void onBsRefreshViewerList(List<UserModel> listModel)
    {
        super.onBsRefreshViewerList(listModel);
        mRoomInfoView.onLiveRefreshViewerList(listModel);
    }

    @Override
    public void onBsRemoveViewer(UserModel model)
    {
        super.onBsRemoveViewer(model);
        mRoomInfoView.onLiveRemoveViewer(model);
    }

    @Override
    public void onBsInsertViewer(int position, UserModel model)
    {
        super.onBsInsertViewer(position, model);
        mRoomInfoView.onLiveInsertViewer(position, model);
    }

    @Override
    public void onBsTicketChange(long ticket)
    {
        super.onBsTicketChange(ticket);
        mRoomInfoView.updateTicket(ticket);
    }

    @Override
    public void onBsViewerNumberChange(int viewerNumber)
    {
        super.onBsViewerNumberChange(viewerNumber);
        mRoomInfoView.updateViewerNumber(viewerNumber);
    }

    @Override
    public void onBsBindCreaterData(UserModel model)
    {
        super.onBsBindCreaterData(model);
        mRoomInfoView.bindCreaterData(model);
    }

    @Override
    public void onBsShowOperateViewer(boolean show)
    {
        super.onBsShowOperateViewer(show);
        mRoomInfoView.showOperateViewerView(show);
    }

    @Override
    public void onBsUpdateLiveQualityData(LiveQualityData data)
    {
        super.onBsUpdateLiveQualityData(data);
        mRoomInfoView.getSdkInfoView().updateLiveQuality(data);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel)
    {
        super.onBsRequestRoomInfoSuccess(actModel);

        mRoomInfoView.bindData(actModel);
        bindShowShareView();
        getLiveBusiness().startLiveQualityLooper(this);
    }

    /**
     * ??????????????????
     * ??????????????????
     */
    protected void showRechargeDialog()
    {
        LiveRechargeDialog dialog = new LiveRechargeDialog(this);
        dialog.show();
    }

    /**
     * ????????????
     *
     * @param v
     */
    protected void onClickCloseRoom(View v)
    {
        //????????????
    }

    /**
     * ????????????????????????
     *
     * @param v
     */
    protected void onClickMenuSendMsg(View v)
    {
        showSendMsgView(true);
    }

    @Override
    public void openSendMsg(String content)
    {
        super.openSendMsg(content);
        showSendMsgView(true);
        mRoomSendMsgView.setContent(content);
    }

    /**
     * ??????????????????
     *
     * @param v
     */
    protected void onClickMenuPrivateMsg(View v)
    {
        LiveChatC2CDialog dialog = new LiveChatC2CDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {

            @Override
            public void onDismiss(DialogInterface dialog)
            {

            }
        });
        dialog.showBottom();
    }

    /**
     * ??????????????????
     *
     * @param v
     */
    protected void onClickMenuAdminMsg(View v)
    {
        Object[] arrOption = new String[]
                {"??????","????????????","??????"};
        AppDialogMenu dialog = new AppDialogMenu(this);
        dialog.setItems(arrOption);
        dialog.setCallback(new ISDDialogMenu.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {

            }

            @Override
            public void onClickItem(View v, int index, SDDialogBase dialog)
            {
                switch (index)
                {
                    case 0: // ??????
                    {
                        // ???????????????????????????
                        final EditText editText = new EditText(LiveLayoutActivity.this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(LiveLayoutActivity.this);
                        builder.setTitle("?????????????????????");
                        builder.setView(editText);
                        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String content = editText.getText().toString();
                                if(!TextUtils.isEmpty(content)){
                                    showProgressDialog("");
                                    CommonInterface.requestSendWarningMsg(getRoomId(), content, new AppRequestCallback<App_AdminModel>() {
                                        @Override
                                        protected void onSuccess(SDResponse sdResponse) {
                                            SDToast.showToast(actModel.getError());
                                        }

                                        @Override
                                        protected void onFinish(SDResponse resp) {
                                            super.onFinish(resp);
                                            dismissProgressDialog();
                                        }
                                    });
                                }
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                    break;
                    case 1: // ????????????
                    {
                        AppDialogConfirm appDialogConfirm = new AppDialogConfirm(LiveLayoutActivity.this);
                        appDialogConfirm.setTextContent("??????????????????????????????").setTextCancel("??????").setTextConfirm("??????");
                        appDialogConfirm.setCallback(new ISDDialogConfirm.Callback()
                        {
                            @Override
                            public void onClickCancel(View v, SDDialogBase dialog)
                            {

                            }

                            @Override
                            public void onClickConfirm(View v, SDDialogBase dialog)
                            {
                                showProgressDialog("");
                                CommonInterface.requestCloseLive(getRoomId(), new AppRequestCallback<App_AdminModel>() {
                                    @Override
                                    protected void onSuccess(SDResponse sdResponse) {
                                        SDToast.showToast(actModel.getError());
                                    }
                                    @Override
                                    protected void onFinish(SDResponse resp) {
                                        super.onFinish(resp);
                                        dismissProgressDialog();
                                    }
                                });

                            }
                        });
                        appDialogConfirm.show();
                    }
                    break;
                    case 2: // ??????
                    {
                        AppDialogConfirm appDialogConfirm = new AppDialogConfirm(LiveLayoutActivity.this);
                        appDialogConfirm.setTextContent("??????????????????????????????").setTextCancel("??????").setTextConfirm("??????");
                        appDialogConfirm.setCallback(new ISDDialogConfirm.Callback()
                        {
                            @Override
                            public void onClickCancel(View v, SDDialogBase dialog)
                            {

                            }

                            @Override
                            public void onClickConfirm(View v, SDDialogBase dialog)
                            {
                                showProgressDialog("");
                                CommonInterface.requestSetBan(getRoomId(), new AppRequestCallback<App_AdminModel>() {
                                    @Override
                                    protected void onSuccess(SDResponse sdResponse) {
                                        SDToast.showToast(actModel.getError());
                                    }
                                    @Override
                                    protected void onFinish(SDResponse resp) {
                                        super.onFinish(resp);
                                        dismissProgressDialog();
                                    }
                                });
                            }
                        });
                        appDialogConfirm.show();
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        dialog.showBottom();

    }


    /**
     * ????????????????????????view
     */
    protected void bindShowShareView()
    {
    }

    /**
     * ?????????????????????
     */
    protected void onClickCreaterPlugin(final PluginModel model)
    {
        if (model == null)
        {
            return;
        }
        CommonInterface.requestPlugin_status(model.getId(), new AppRequestCallback<App_plugin_statusActModel>()
        {
            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onStart()
            {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    if (actModel.getIs_enable() == 1)
                    {
                        if (model.isNormalPlugin())
                        {
                            //??????????????????
                            onClickCreaterPluginNormal(model);
                        } else if (model.isGamePlugin())
                        {
                            //??????????????????
                            onClickCreaterPluginGame(model);
                        }
                    }
                }
            }
        });

    }

    /**
     * ????????????????????????
     *
     * @param model
     */
    protected void onClickCreaterPluginNormal(PluginModel model)
    {

    }

    /**
     * ????????????????????????
     *
     * @param model
     */
    protected void onClickCreaterPluginGame(PluginModel model)
    {

    }

    protected void replaceBankerView(View view)
    {
        replaceView(R.id.fl_container_banker, view);
    }

    /**
     * ????????????
     *
     * @param v
     */
    protected void onClickMenuShare(View v)
    {
        openShare(new UMShareListener()
        {
            @Override
            public void onStart(SHARE_MEDIA share_media)
            {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media)
            {
                getLiveBusiness().sendShareSuccessMsg();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable)
            {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media)
            {
            }
        });
    }

    /**
     * ??????????????????
     */
    protected void hideSendGiftView()
    {

    }

    /**
     * ????????????????????????view
     */
    protected void showBottomView(boolean show)
    {
        // ????????????
    }

    /**
     * ????????????????????????
     */
    protected void showMsgView(boolean show)
    {
        if (show)
        {
            mRoomMsgView.getVisibilityHandler().setVisible(true);
        } else
        {
            mRoomMsgView.getVisibilityHandler().setInvisible(true);
        }
    }

    /**
     * ????????????????????????view
     */
    protected void showSendMsgView(boolean show)
    {
        if (show)
        {
            SDViewUtil.setVisible(mRoomSendMsgView);
        } else
        {
            SDViewUtil.setInvisible(mRoomSendMsgView);
        }
    }

    /**
     * ????????????view????????????
     *
     * @return
     */
    protected boolean isSendMsgViewVisible()
    {
        if (mRoomSendMsgView == null)
        {
            return false;
        }

        return mRoomSendMsgView.isVisible();
    }

    /**
     * ????????????view????????????
     *
     * @return
     */
    protected boolean isSendGiftViewVisible()
    {
        return false;
    }

    @Override
    public void onMsgRedEnvelope(CustomMsgRedEnvelope msg)
    {
        super.onMsgRedEnvelope(msg);
        LiveRedEnvelopeNewDialog dialog = new LiveRedEnvelopeNewDialog(this, msg);
        dialog.show();
    }

    @Override
    public void onBsViewerShowCreaterLeave(boolean show)
    {
        super.onBsViewerShowCreaterLeave(show);
        if (isAuctioning())
        {
            mRoomInfoView.showCreaterLeave(false);
        } else
        {
            mRoomInfoView.showCreaterLeave(show);
        }
    }
}
