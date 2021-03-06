package com.fanwe.live;

import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.games.model.custommsg.CustomMsgGameBanker;
import com.fanwe.games.model.custommsg.GameMsgModel;
import com.fanwe.library.utils.SDBase64;
import com.fanwe.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgApplyLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgCreaterComeback;
import com.fanwe.live.model.custommsg.CustomMsgCreaterLeave;
import com.fanwe.live.model.custommsg.CustomMsgCreaterQuit;
import com.fanwe.live.model.custommsg.CustomMsgData;
import com.fanwe.live.model.custommsg.CustomMsgEndVideo;
import com.fanwe.live.model.custommsg.CustomMsgForbidSendMsg;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.model.custommsg.CustomMsgLargeGift;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.CustomMsgLiveMsg;
import com.fanwe.live.model.custommsg.CustomMsgLiveStopped;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.fanwe.live.model.custommsg.CustomMsgPrivateImage;
import com.fanwe.live.model.custommsg.CustomMsgPrivateText;
import com.fanwe.live.model.custommsg.CustomMsgPrivateVoice;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.fanwe.live.model.custommsg.CustomMsgRejectLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLive;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.model.custommsg.CustomMsgWarning;
import com.fanwe.live.model.custommsg.ICustomMsg;
import com.fanwe.pay.model.custommsg.CustomMsgStartPayMode;
import com.fanwe.pay.model.custommsg.CustomMsgStartScenePayMode;
import com.fanwe.shop.model.custommsg.CustomMsgShopBuySuc;
import com.fanwe.shop.model.custommsg.CustomMsgShopPush;

import java.util.HashMap;

public class LiveConstant
{
    /**
     * sdk??????????????????
     */
    public static final int MAX_LINK_MIC_COUNT = 3;

    public static final String LIVE_PRIVATE_KEY_TAG = SDBase64.decodeToString("8J+UkQ==");

    public static final String LIVE_HOT_CITY = "??????";

    public static final String LEVEL_SPAN_KEY = "level";

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final class LiveSdkTag
    {
        /**
         * ??????sdk??????tag
         */
        public static final String TAG_SDK_TENCENT = "tag_sdk_tencent";
        /**
         * ??????sdk??????tag
         */
        public static final String TAG_SDK_KSY = "tag_sdk_ksy";
    }

    public static final class LiveSdkType
    {
        /**
         * ??????sdk
         */
        public static final int TENCENT = 0;
        /**
         * ??????sdk
         */
        public static final int KSY = 1;
    }

    /**
     * ??????????????????
     */
    public static final class VideoQualityType
    {
        /**
         * ??????
         */
        public static final int VIDEO_QUALITY_STANDARD = 0;
        /**
         * ??????
         */
        public static final int VIDEO_QUALITY_HIGH = 1;
        /**
         * ??????
         */
        public static final int VIDEO_QUALITY_SUPER = 2;
    }


    /**
     * ???????????????????????????????????????????????????
     */
    public static final class CustomMsgType
    {
        public static final int MSG_NONE = -1;
        /**
         * ????????????????????????
         */
        public static final int MSG_TEXT = 0;
        /**
         * ????????????????????????
         */
        public static final int MSG_GIFT = 1;
        /**
         * ??????????????????
         */
        public static final int MSG_POP_MSG = 2;
        /**
         * ??????????????????
         */
        public static final int MSG_CREATER_QUIT = 3;
        /**
         * ????????????
         */
        public static final int MSG_FORBID_SEND_MSG = 4;
        /**
         * ???????????????????????????
         */
        public static final int MSG_VIEWER_JOIN = 5;
        /**
         * ???????????????????????????
         */
        public static final int MSG_VIEWER_QUIT = 6;
        /**
         * ??????????????????????????????????????????????????????
         */
        public static final int MSG_END_VIDEO = 7;
        /**
         * ????????????
         */
        public static final int MSG_RED_ENVELOPE = 8;
        /**
         * ????????????
         */
        public static final int MSG_LIVE_MSG = 9;
        /**
         * ????????????
         */
        public static final int MSG_CREATER_LEAVE = 10;
        /**
         * ????????????
         */
        public static final int MSG_CREATER_COME_BACK = 11;
        /**
         * ??????
         */
        public static final int MSG_LIGHT = 12;
        /**
         * ????????????
         */
        public static final int MSG_APPLY_LINK_MIC = 13;
        /**
         * ????????????
         */
        public static final int MSG_ACCEPT_LINK_MIC = 14;
        /**
         * ????????????
         */
        public static final int MSG_REJECT_LINK_MIC = 15;
        /**
         * ????????????
         */
        public static final int MSG_STOP_LINK_MIC = 16;
        /**
         * ??????
         */
        public static final int MSG_STOP_LIVE = 17;
        /**
         * ???????????????????????????????????????????????????????????????????????????????????????
         */
        public static final int MSG_LIVE_STOPPED = 18;
        /**
         * ??????????????????
         */
        public static final int MSG_PRIVATE_TEXT = 20;

        /**
         * ??????????????????
         */
        public static final int MSG_PRIVATE_VOICE = 21;
        /**
         * ??????????????????
         */
        public static final int MSG_PRIVATE_IMAGE = 22;
        /**
         * ??????????????????
         */
        public static final int MSG_PRIVATE_GIFT = 23;
        /**
         * ????????????
         */
        public static final int MSG_AUCTION_SUCCESS = 25;
        /**
         * ???????????????????????????????????????????????????????????????????????????
         */
        public static final int MSG_AUCTION_NOTIFY_PAY = 26;
        /**
         * ??????
         */
        public static final int MSG_AUCTION_FAIL = 27;

        /**
         * ??????????????????
         */
        public static final int MSG_AUCTION_OFFER = 28;

        /**
         * ????????????
         */
        public static final int MSG_AUCTION_PAY_SUCCESS = 29;
        /**
         * ????????????????????????
         */
        public static final int MSG_AUCTION_CREATE_SUCCESS = 30;

        /**
         * ????????????????????????
         */
        public static final int MSG_SHOP_GOODS_PUSH = 31;

        /**
         * ????????????????????????
         */
        public static final int MSG_SHOP_GOODS_BUY_SUCCESS = 37;

        /**
         * ????????????????????????
         */
        public static final int MSG_START_PAY_MODE = 32;
        /**
         * ??????????????????
         */
        public static final int MSG_GAMES_STOP = 34;

        /**
         * ????????????
         */
        public static final int MSG_GAME = 39;

        /**
         * ??????????????????????????????
         */
        public static final int MSG_START_SCENE_PAY_MODE = 40;

        /**
         * ?????????????????????(???????????????)
         */
        public static final int MSG_WARNING_BY_MANAGER = 41;

        /**
         * ??????????????????
         */
        public static final int MSG_DATA = 42;

        /**
         * ??????????????????
         */
        public static final int MSG_GAME_BANKER = 43;

        /**
         * ????????????????????????
         */
        public static final int MSG_LARGE_GIFT = 50;
    }

    public static final HashMap<Integer, Class<? extends ICustomMsg>> mapCustomMsgClass = new HashMap<>();

    static
    {
        mapCustomMsgClass.put(CustomMsgType.MSG_TEXT, CustomMsgText.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_GIFT, CustomMsgGift.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_POP_MSG, CustomMsgPopMsg.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_CREATER_QUIT, CustomMsgCreaterQuit.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_FORBID_SEND_MSG, CustomMsgForbidSendMsg.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_VIEWER_JOIN, CustomMsgViewerJoin.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_VIEWER_QUIT, CustomMsgViewerQuit.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_END_VIDEO, CustomMsgEndVideo.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_RED_ENVELOPE, CustomMsgRedEnvelope.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_LIVE_MSG, CustomMsgLiveMsg.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_CREATER_LEAVE, CustomMsgCreaterLeave.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_CREATER_COME_BACK, CustomMsgCreaterComeback.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_LIGHT, CustomMsgLight.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_APPLY_LINK_MIC, CustomMsgApplyLinkMic.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_ACCEPT_LINK_MIC, CustomMsgAcceptLinkMic.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_REJECT_LINK_MIC, CustomMsgRejectLinkMic.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_STOP_LINK_MIC, CustomMsgStopLinkMic.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_STOP_LIVE, CustomMsgStopLive.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_LIVE_STOPPED, CustomMsgLiveStopped.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_PRIVATE_TEXT, CustomMsgPrivateText.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_PRIVATE_VOICE, CustomMsgPrivateVoice.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_PRIVATE_IMAGE, CustomMsgPrivateImage.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_PRIVATE_GIFT, CustomMsgPrivateGift.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_AUCTION_SUCCESS, CustomMsgAuctionSuccess.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_AUCTION_NOTIFY_PAY, CustomMsgAuctionNotifyPay.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_AUCTION_FAIL, CustomMsgAuctionFail.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_AUCTION_OFFER, CustomMsgAuctionOffer.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_AUCTION_PAY_SUCCESS, CustomMsgAuctionPaySuccess.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_AUCTION_CREATE_SUCCESS, CustomMsgAuctionCreateSuccess.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_SHOP_GOODS_PUSH, CustomMsgShopPush.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_SHOP_GOODS_BUY_SUCCESS, CustomMsgShopBuySuc.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_START_PAY_MODE, CustomMsgStartPayMode.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_GAMES_STOP, GameMsgModel.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_GAME, GameMsgModel.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_GAME_BANKER, CustomMsgGameBanker.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_START_SCENE_PAY_MODE, CustomMsgStartScenePayMode.class);

        mapCustomMsgClass.put(CustomMsgType.MSG_WARNING_BY_MANAGER, CustomMsgWarning.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_DATA, CustomMsgData.class);
        mapCustomMsgClass.put(CustomMsgType.MSG_LARGE_GIFT, CustomMsgLargeGift.class);

    }

    /**
     * ????????????????????????
     */
    public static final class CustomMsgDataType
    {
        /**
         * ???????????????????????????
         */
        public static final int DATA_VIEWER_LIST = 0;
        /**
         * ??????????????????????????????????????????????????????
         */
        public static final int DATA_LINK_MIC_INFO = 1;
    }

    /**
     * ????????????????????????????????????
     */
    public static final class LiveMsgType
    {
        /**
         * ????????????????????????
         */
        public static final int MSG_TEXT = 0;
        /**
         * ???????????????
         */
        public static final int MSG_VIEWER_JOIN = MSG_TEXT + 1;
        /**
         * ????????????????????????????????????
         */
        public static final int MSG_GIFT_VIEWER = MSG_VIEWER_JOIN + 1;
        /**
         * ????????????????????????????????????
         */
        public static final int MSG_GIFT_CREATER = MSG_GIFT_VIEWER + 1;
        /**
         * ????????????
         */
        public static final int MSG_FORBID_SEND_MSG = MSG_GIFT_CREATER + 1;
        /**
         * ???????????????
         */
        public static final int MSG_LIVE_MSG = MSG_FORBID_SEND_MSG + 1;
        /**
         * ????????????
         */
        public static final int MSG_RED_ENVELOPE = MSG_LIVE_MSG + 1;
        /**
         * ????????????
         */
        public static final int MSG_CREATER_LEAVE = MSG_RED_ENVELOPE + 1;
        /**
         * ????????????
         */
        public static final int MSG_CREATER_COME_BACK = MSG_CREATER_LEAVE + 1;
        /**
         * ??????
         */
        public static final int MSG_LIGHT = MSG_CREATER_COME_BACK + 1;
        /**
         * ??????
         */
        public static final int MSG_POP_MSG = MSG_LIGHT + 1;
        /**
         * ??????????????????
         */
        public static final int MSG_PRO_VIEWER_JOIN = MSG_POP_MSG + 1;
        /**
         * ??????????????????
         */
        public static final int MSG_AUCTION_OFFER = MSG_PRO_VIEWER_JOIN + 1;
        /**
         * ????????????
         */
        public static final int MSG_AUCTION_SUCCESS = MSG_AUCTION_OFFER + 1;
        /**
         * ????????????
         */
        public static final int MSG_AUCTION_PAY_SUCCESS = MSG_AUCTION_SUCCESS + 1;
        /**
         * ??????
         */
        public static final int MSG_AUCTION_FAIL = MSG_AUCTION_PAY_SUCCESS + 1;
        /**
         * ???????????????????????????????????????????????????????????????????????????
         */
        public static final int MSG_AUCTION_NOTIFY_PAY = MSG_AUCTION_FAIL + 1;
        /**
         * ????????????????????????
         */
        public static final int MSG_AUCTION_CREATE_SUCCESS = MSG_AUCTION_NOTIFY_PAY + 1;

        /**
         * ????????????????????????
         */
        public static final int MSG_SHOP_GOODS_PUSH = MSG_AUCTION_CREATE_SUCCESS + 1;
        /**
         * ????????????????????????
         */
        public static final int MSG_SHOP_GOODS_BUY_SUCCESS = MSG_SHOP_GOODS_PUSH + 1;
    }

    /**
     * ??????????????????
     */
    public static final class PrivateMsgType
    {
        /**
         * ????????????
         */
        public static final int MSG_TEXT_LEFT = 0;
        public static final int MSG_TEXT_RIGHT = 1;

        //??????
        public static final int MSG_VOICE_LEFT = 2;
        public static final int MSG_VOICE_RIGHT = 3;

        //??????
        public static final int MSG_IMAGE_LEFT = 4;
        public static final int MSG_IMAGE_RIGHT = 5;

        //??????
        public static final int MSG_GIFT_LEFT = 6;
        public static final int MSG_GIFT_RIGHT = 7;

    }

    public static final class GiftType
    {
        /**
         * ????????????
         */
        public static final int NORMAL = 0;

        /**
         * gif??????
         */
        public static final int GIF = 1;

        /**
         * ????????????
         */
        public static final int ANIMATOR = 2;
    }

    public static final class GiftAnimatorType
    {
        public static final String PLANE1 = "plane1";

        public static final String PLANE2 = "plane2";
        /**
         * ??????
         */
        public static final String ROCKET1 = "rocket1";
        /**
         * ???????????????
         */
        public static final String FERRARI = "ferrari";
        /**
         * ??????????????????
         */
        public static final String LAMBORGHINI = "lamborghini";
    }

    public static final class PluginClassName
    {
        public static final String P_LIVE_PAY = "live_pay";

        public static final String P_LIVE_PAY_SCENE = "live_pay_scene";

        public static final String P_PAI = "pai";

        public static final String P_SHOP = "shop";

        public static final String P_GAME = "game";
        /**
         * ????????????
         */
        public static final String P_PODCAST_GOODS = "podcast_goods";
    }
}
