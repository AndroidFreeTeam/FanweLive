package com.fanwe.live.activity.room;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.fanwe.games.model.App_plugin_initActModel;
import com.fanwe.games.model.custommsg.CustomMsgGameBanker;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.event.EUnLogin;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveCreaterIM;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.LiveViewerIM;
import com.fanwe.live.business.LiveBusiness;
import com.fanwe.live.business.LiveCreaterBusiness;
import com.fanwe.live.business.LiveMsgBusiness;
import com.fanwe.live.business.LiveViewerBusiness;
import com.fanwe.live.control.IPushSDK;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.model.App_end_videoActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_monitorActModel;
import com.fanwe.live.model.App_viewerActModel;
import com.fanwe.live.model.LiveQualityData;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgApplyLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgCreaterComeback;
import com.fanwe.live.model.custommsg.CustomMsgCreaterLeave;
import com.fanwe.live.model.custommsg.CustomMsgData;
import com.fanwe.live.model.custommsg.CustomMsgEndVideo;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.model.custommsg.CustomMsgLargeGift;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.fanwe.live.model.custommsg.CustomMsgRejectLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLive;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.model.custommsg.CustomMsgWarning;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.model.custommsg.data.DataLinkMicInfoModel;
import com.umeng.socialize.UMShareListener;

import java.util.List;

/**
 * ???????????????
 * <p/>
 * Created by Administrator on 2016/8/4.
 */
public class LiveActivity extends BaseActivity implements
        ILiveActivity,
        LiveMsgBusiness.LiveMsgBusinessCallback,
        LiveViewerBusiness.LiveViewerBusinessCallback,
        LiveCreaterBusiness.LiveCreaterBusinessCallback
{
    /**
     * ??????id(int)
     */
    public static final String EXTRA_ROOM_ID = "extra_room_id";
    /**
     * ?????????id(String)
     */
    public static final String EXTRA_GROUP_ID = "extra_group_id";
    /**
     * ??????identifier(String)
     */
    public static final String EXTRA_CREATER_ID = "extra_creater_id";

    private LiveViewerIM mViewerIM;
    private LiveCreaterIM mCreaterIM;

    private LiveMsgBusiness mMsgBusiness;

    private LiveViewerBusiness mViewerBusiness;
    private LiveCreaterBusiness mCreaterBusiness;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // ?????????

        int roomId = getIntent().getIntExtra(EXTRA_ROOM_ID, 0);
        String groupId = getIntent().getStringExtra(EXTRA_GROUP_ID);
        String createrId = getIntent().getStringExtra(EXTRA_CREATER_ID);

        LiveInformation.getInstance().setRoomId(roomId);
        LiveInformation.getInstance().setGroupId(groupId);
        LiveInformation.getInstance().setCreaterId(createrId);
    }

    /**
     * ????????????sdk
     *
     * @return
     */
    protected IPushSDK getPushSDK()
    {
        return null;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public LiveMsgBusiness getMsgBusiness()
    {
        if (mMsgBusiness == null)
        {
            mMsgBusiness = new LiveMsgBusiness();
            mMsgBusiness.setBusinessCallback(this);
        }
        return mMsgBusiness;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public LiveViewerBusiness getViewerBusiness()
    {
        if (mViewerBusiness == null)
        {
            mViewerBusiness = new LiveViewerBusiness(this);
            mViewerBusiness.setBusinessCallback(this);
        }
        return mViewerBusiness;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public LiveCreaterBusiness getCreaterBusiness()
    {
        if (mCreaterBusiness == null)
        {
            mCreaterBusiness = new LiveCreaterBusiness(this);
            mCreaterBusiness.setBusinessCallback(this);
        }
        return mCreaterBusiness;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public LiveBusiness getLiveBusiness()
    {
        if (isPlayback())
        {
            return getViewerBusiness();
        } else
        {
            if (isCreater())
            {
                return getCreaterBusiness();
            } else
            {
                return getViewerBusiness();
            }
        }
    }

    /**
     * ???????????????IM?????????
     *
     * @return
     */
    public LiveViewerIM getViewerIM()
    {
        if (mViewerIM == null)
        {
            mViewerIM = new LiveViewerIM();
        }
        return mViewerIM;
    }

    /**
     * ???????????????IM?????????
     *
     * @return
     */
    public LiveCreaterIM getCreaterIM()
    {
        if (mCreaterIM == null)
        {
            mCreaterIM = new LiveCreaterIM();
        }
        return mCreaterIM;
    }

    /**
     * ??????????????????
     */
    public void openShare(UMShareListener listener)
    {
        getLiveBusiness().openShare(this, listener);
    }

    public void doShare(int platform, UMShareListener listener)
    {
        getLiveBusiness().doShare(platform, this, listener);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    protected void requestRoomInfo()
    {
        //????????????
    }

    /**
     * ?????????????????????????????????
     *
     * @param groupId
     */
    protected void onSuccessJoinGroup(String groupId)
    {

    }

    /**
     * ??????im?????????
     *
     * @param event
     */
    public void onEventMainThread(EImOnNewMessages event)
    {
        String groupId = getGroupId();

        getMsgBusiness().parseMsg(event.msg, groupId);
        getLiveBusiness().getMsgBusiness().parseMsg(event.msg, groupId);

        try
        {
            if (LiveConstant.CustomMsgType.MSG_DATA == event.msg.getCustomMsgType())
            {
                String peer = event.msg.getConversationPeer();
                if (!TextUtils.isEmpty(groupId))
                {
                    if (!groupId.equals(peer))
                    {
                        // ?????????????????????
                        IMHelper.quitGroup(peer, null);
                        LogUtil.i("quitGroup other room:" + peer);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onMsgRedEnvelope(CustomMsgRedEnvelope msg)
    {
    }

    @Override
    public void onMsgApplyLinkMic(CustomMsgApplyLinkMic msg)
    {
    }

    @Override
    public void onMsgAcceptLinkMic(CustomMsgAcceptLinkMic msg)
    {
    }

    @Override
    public void onMsgRejectLinkMic(CustomMsgRejectLinkMic msg)
    {
    }

    @Override
    public void onMsgStopLinkMic(CustomMsgStopLinkMic msg)
    {
    }

    @Override
    public void onMsgEndVideo(CustomMsgEndVideo msg)
    {
    }

    @Override
    public void onMsgStopLive(CustomMsgStopLive msg)
    {
    }

    @Override
    public void onMsgPrivate(MsgModel msg)
    {
    }

    @Override
    public void onMsgAuction(MsgModel msg)
    {
    }

    @Override
    public void onMsgShop(MsgModel msg)
    {
    }

    @Override
    public void onMsgGame(MsgModel msg)
    {
    }

    @Override
    public void onMsgGameBanker(CustomMsgGameBanker msg)
    {

    }

    @Override
    public void onMsgGift(CustomMsgGift msg)
    {
    }

    @Override
    public void onMsgPopMsg(CustomMsgPopMsg msg)
    {
    }

    @Override
    public void onMsgViewerJoin(CustomMsgViewerJoin msg)
    {
    }

    @Override
    public void onMsgViewerQuit(CustomMsgViewerQuit msg)
    {
    }

    @Override
    public void onMsgCreaterLeave(CustomMsgCreaterLeave msg)
    {
    }

    @Override
    public void onMsgCreaterComeback(CustomMsgCreaterComeback msg)
    {
    }

    @Override
    public void onMsgLight(CustomMsgLight msg)
    {
    }

    @Override
    public void onMsgLiveChat(MsgModel msg)
    {
    }

    @Override
    public void onMsgWarning(CustomMsgWarning msgWarning)
    {

    }

    @Override
    public void onMsgData(CustomMsgData msg)
    {

    }

    @Override
    public void onMsgDataViewerList(App_viewerActModel model)
    {

    }

    @Override
    public void onMsgDataLinkMicInfo(DataLinkMicInfoModel model)
    {

    }

    @Override
    public void onMsgLargeGift(CustomMsgLargeGift customMsgLargeGift)
    {

    }

    @Override
    public void onMsgPayMode(MsgModel msg)
    {
    }

    /**
     * ????????????
     *
     * @param event
     */
    public void onEventMainThread(EUnLogin event)
    {
        //????????????
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    public void onEventMainThread(EImOnForceOffline event)
    {
        //????????????
    }

    /**
     * ????????????
     *
     * @param event
     */
    public void onEventMainThread(EOnCallStateChanged event)
    {
        //????????????
    }

    /**
     * ?????????IM
     */
    protected void initIM()
    {
        //????????????
    }

    /**
     * ??????IM
     */
    protected void destroyIM()
    {
        //????????????
    }

    @Override
    protected void onDestroy()
    {
        mViewerIM = null;
        mCreaterIM = null;
        if (mViewerBusiness != null)
        {
            mViewerBusiness.onDestroy();
            mViewerBusiness = null;
        }
        if (mCreaterBusiness != null)
        {
            mCreaterBusiness.onDestroy();
            mCreaterBusiness = null;
        }
        LiveInformation.getInstance().exitRoom();
        super.onDestroy();
    }

    //sdk

    /**
     * ????????????
     *
     * @param enable true-?????????false-??????
     */
    protected void sdkEnableAudio(boolean enable)
    {
        //????????????
    }

    /**
     * ??????????????????
     */
    protected void sdkPauseVideo()
    {

    }

    /**
     * ??????????????????
     */
    protected void sdkResumeVideo()
    {

    }

    /**
     * ????????????
     */
    protected void sdkStopLinkMic()
    {
        //????????????
    }

    @Override
    public LiveCreaterBusiness.CreaterMonitorData onBsCreaterGetMonitorData()
    {
        return null;
    }

    @Override
    public void onBsCreaterRequestMonitorSuccess(App_monitorActModel actModel)
    {
    }

    @Override
    public void onBsCreaterRequestInitPluginSuccess(App_plugin_initActModel actModel)
    {
    }

    @Override
    public void onBsCreaterRequestEndVideoSuccess(App_end_videoActModel actModel)
    {
    }

    @Override
    public void onBsCreaterShowReceiveApplyLinkMic(CustomMsgApplyLinkMic msg)
    {
    }

    @Override
    public void onBsCreaterHideReceiveApplyLinkMic()
    {
    }

    @Override
    public boolean onBsCreaterIsReceiveApplyLinkMicShow()
    {
        return false;
    }

    @Override
    public void onBsViewerShowCreaterLeave(boolean show)
    {
    }

    @Override
    public void onBsViewerShowApplyLinkMic(boolean show)
    {
    }

    @Override
    public void onBsViewerApplyLinkMicError(String msg)
    {
    }

    @Override
    public void onBsViewerApplyLinkMicRejected(CustomMsgRejectLinkMic msg)
    {
    }

    @Override
    public void onBsViewerStartJoinRoom()
    {
    }

    @Override
    public void onBsViewerExitRoom(boolean needFinishActivity)
    {

    }

    @Override
    public void onBsViewerShowDailyTask(boolean show)
    {

    }

    @Override
    public void onBsViewerShowTopList(boolean show) {

    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel)
    {
    }

    @Override
    public void onBsRequestRoomInfoError(App_get_videoActModel actModel)
    {
    }

    @Override
    public void onBsRequestRoomInfoException(String msg)
    {
    }

    @Override
    public void onBsRefreshViewerList(List<UserModel> listModel)
    {
    }

    @Override
    public void onBsRemoveViewer(UserModel model)
    {
    }

    @Override
    public void onBsInsertViewer(int position, UserModel model)
    {
    }

    @Override
    public void onBsTicketChange(long ticket)
    {
    }

    @Override
    public void onBsViewerNumberChange(int viewerNumber)
    {
    }

    @Override
    public void onBsBindCreaterData(UserModel model)
    {
    }

    @Override
    public void onBsShowOperateViewer(boolean show)
    {
    }

    @Override
    public LiveQualityData onBsGetLiveQualityData()
    {
        return null;
    }

    @Override
    public void onBsUpdateLiveQualityData(LiveQualityData data)
    {
    }

    @Override
    public void onBsShowProgress(String msg)
    {
        showProgressDialog(msg);
    }

    @Override
    public void onBsHideProgress()
    {
        dismissProgressDialog();
    }

    //----------ILiveActivity implements start----------

    @Override
    public int getRoomId()
    {
        return LiveInformation.getInstance().getRoomId();
    }

    @Override
    public String getGroupId()
    {
        return LiveInformation.getInstance().getGroupId();
    }

    @Override
    public String getCreaterId()
    {
        return LiveInformation.getInstance().getCreaterId();
    }

    @Override
    public App_get_videoActModel getRoomInfo()
    {
        return LiveInformation.getInstance().getRoomInfo();
    }

    @Override
    public boolean isPrivate()
    {
        return LiveInformation.getInstance().isPrivate();
    }

    @Override
    public boolean isPlayback()
    {
        return LiveInformation.getInstance().isPlayback();
    }

    @Override
    public boolean isCreater()
    {
        return LiveInformation.getInstance().isCreater();
    }

    @Override
    public int getSdkType()
    {
        return LiveInformation.getInstance().getSdkType();
    }

    @Override
    public boolean isAuctioning()
    {
        return LiveInformation.getInstance().isAuctioning();
    }

    @Override
    public void openSendMsg(String content)
    {

    }

    //----------ILiveActivity implements start----------

}
