package com.fanwe.auction;

import android.os.CountDownTimer;
import android.view.View;

import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.auction.model.PaiUserGetVideoDataModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestCallbackWrapper;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.MsgModel;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */

public class AuctionBusiness
{

    private CountDownTimer timer;
    private AuctionBusinessListener auctionBusinessListener;
    private boolean isAuctioning;
    private PaiBuyerModel currentBuyer;

    public PaiBuyerModel getCurrentBuyer()
    {
        return currentBuyer;
    }

    public void setAuctionBusinessListener(AuctionBusinessListener auctionBusinessListener)
    {
        this.auctionBusinessListener = auctionBusinessListener;
    }

    private void setAuctioning(boolean auctioning)
    {

    }

    public boolean isAuctioning()
    {
        return isAuctioning;
    }

    public void onAuctionMsg(MsgModel msg)
    {

    }

    protected void onAuctionMsgCreateSuccess(CustomMsgAuctionCreateSuccess customMsg)
    {

    }

    protected void onAuctionMsgOffer(CustomMsgAuctionOffer customMsg)
    {

    }

    protected void onAuctionMsgSuccess(CustomMsgAuctionSuccess customMsg)
    {

    }

    protected void onAuctionMsgNotifyPay(CustomMsgAuctionNotifyPay customMsg)
    {

    }

    protected void onAuctionMsgFail(CustomMsgAuctionFail customMsg)
    {

    }

    protected void onAuctionMsgPaySuccess(CustomMsgAuctionPaySuccess customMsg)
    {

    }

    /**
     * ????????????????????????
     */
    private void needShowPay(List<PaiBuyerModel> list)
    {

    }

    /**
     * ???????????????
     */
    private void startPayRemaining()
    {

    }

    /**
     * ?????????????????????
     */
    private void stopPayRemaining()
    {
        if (timer != null)
        {
            timer.cancel();
        }
    }

    // ??????

    /**
     * ????????????????????????
     */
    public void requestCreateAuctionAuthority(AppRequestCallback<BaseActModel> listener)
    {

    }

    /**
     * ??????????????????
     */
    public void requestPaiInfo(int paiId, AppRequestCallback<App_pai_user_get_videoActModel> listener)
    {

    }

    public void clickAuctionPay(View v)
    {
        auctionBusinessListener.onAuctionPayClick(v);
    }

    public void onDestroy()
    {
        stopPayRemaining();
    }

    public interface AuctionBusinessListener
    {
        /**
         * ??????????????????
         */
        void onAuctionMsgCreateSuccess(CustomMsgAuctionCreateSuccess customMsg);

        /**
         * ??????
         */
        void onAuctionMsgOffer(CustomMsgAuctionOffer customMsg);

        /**
         * ????????????
         */
        void onAuctionMsgSuccess(CustomMsgAuctionSuccess customMsg);

        /**
         * ???????????????????????????????????????????????????????????????????????????
         */
        void onAuctionMsgNotifyPay(CustomMsgAuctionNotifyPay customMsg);

        /**
         * ????????????
         */
        void onAuctionMsgFail(CustomMsgAuctionFail customMsg);

        /**
         * ???????????? ????????????
         */
        void onAuctionMsgPaySuccess(CustomMsgAuctionPaySuccess customMsg);

        /**
         * ?????????????????????
         *
         * @param buyer
         * @param day
         * @param hour
         * @param min
         * @param sec
         */
        void onAuctionPayRemaining(PaiBuyerModel buyer, long day, long hour, long min, long sec);

        /**
         * ????????????????????????????????????????????????
         */
        void onAuctionNeedShowPay(boolean show);

        /**
         * ???????????????????????????
         */
        void onAuctionPayClick(View v);

        /**
         * ????????????????????????
         */
        void onAuctioningChange(boolean isAuctioning);

        /**
         * ????????????????????????
         */
        void onAuctionRequestPaiInfoSuccess(App_pai_user_get_videoActModel actModel);

        /**
         * ??????????????????????????????
         */
        void onAuctionRequestCreateAuthoritySuccess();

        /**
         * ??????????????????????????????
         */
        void onAuctionRequestCreateAuthorityError(String msg);
    }

}
