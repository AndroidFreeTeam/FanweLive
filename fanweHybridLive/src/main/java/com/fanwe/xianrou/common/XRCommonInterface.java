package com.fanwe.xianrou.common;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.xianrou.model.XRAddVideoPlayCountResponseModel;
//import com.fanwe.xianrou.model.XRBuyerListTradeDetailResponseModel;
//import com.fanwe.xianrou.model.XRChatIndexActModel;
//import com.fanwe.xianrou.model.XRChatMyCommentActModel;
import com.fanwe.xianrou.model.XRCommonActionRequestResponseModel;
//import com.fanwe.xianrou.model.XRDistributionIndexActModel;
//import com.fanwe.xianrou.model.XRDoPublishImageTextModel;
import com.fanwe.xianrou.model.XRDynamicCommentResopnseModel;
//import com.fanwe.xianrou.model.XRDynamicDetailFavoriteUsersResponseModel;
//import com.fanwe.xianrou.model.XRIndexIndexActModel;
//import com.fanwe.xianrou.model.XRIndexSelectPhotoActModel;
import com.fanwe.xianrou.model.XRIndexSelectVideoActModel;
//import com.fanwe.xianrou.model.XRNearbyActModel;
//import com.fanwe.xianrou.model.XRPayPayListActModel;
//import com.fanwe.xianrou.model.XRPayPayUserInfoActModel;
import com.fanwe.xianrou.model.XRPublishCheckTypeActModel;
//import com.fanwe.xianrou.model.XRPublishRedPhotoData;
//import com.fanwe.xianrou.model.XRRecommentDynamicResponseModel;
//import com.fanwe.xianrou.model.XRRedPocketPhotoPaySuccessResponseModel;
import com.fanwe.xianrou.model.XRReportTypeResponseModel;
//import com.fanwe.xianrou.model.XRRequestSearchHotTagResponseModel;
import com.fanwe.xianrou.model.XRRequestUserDynamicFavoriteResponseModel;
//import com.fanwe.xianrou.model.XRSearchResponseModel;
//import com.fanwe.xianrou.model.XRSelectPayPhotoModel;
//import com.fanwe.xianrou.model.XRSetUserBlackListResponseModel;
//import com.fanwe.xianrou.model.XRStickTopUserDynamicResponseModel;
//import com.fanwe.xianrou.model.XRUploadShowImageResponseModel;
//import com.fanwe.xianrou.model.XRUserBuyerListResponseModel;
//import com.fanwe.xianrou.model.XRUserCenterResponseModel;
//import com.fanwe.xianrou.model.XRUserDynamicDetailGoodsResponseModel;
import com.fanwe.xianrou.model.XRStickTopUserDynamicResponseModel;
import com.fanwe.xianrou.model.XRUserDynamicDetailResponseModel;
//import com.fanwe.xianrou.model.XRUserDynamicSelfResponseModel;
//import com.fanwe.xianrou.model.XRUserGuardRankingResponseModel;
//import com.fanwe.xianrou.model.XRUserLevelDescriptionResponseModel;
//import com.fanwe.xianrou.model.XRUserProfitResponseModel;
//import com.fanwe.xianrou.model.XRUserPurchaseResponseModel;
//import com.fanwe.xianrou.model.XRUserPurchaseTradeDetailResponseModel;
//import com.fanwe.xianrou.model.XRUserWeixinStatusActModel;
//import com.fanwe.xianrou.model.XRUserWithdrawInfoResponseModel;
//import com.fanwe.xianrou.model.XRUserWithdrawRecordResponseModel;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class XRCommonInterface
{
    public static AppRequestParams getXRRequestParams()
    {
        AppRequestParams params = new AppRequestParams();
//        if(XRAppRuntimeWorker.isXROpen()==1)
//        {
            params.put("itype", "xr");
//        }
        return params;
    }

    /**
     * ??????????????????
     */
//    public static void requestIndexIndex(int page, AppRequestCallback<XRIndexIndexActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("index");
//        params.putAct("index");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }
//
//    /**
//     * ??????????????????
//     */
//    public static void requestIndexSelectPhoto(int page, AppRequestCallback<XRIndexSelectPhotoActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("index");
//        params.putAct("select_photo");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ??????????????????
     */
    public static void requestIndexSelectVideo(int page, AppRequestCallback<XRIndexSelectVideoActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("index");
        params.putAct("select_video");
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * ????????????-??????????????????
     *
     * @param content
     * @param listener
     */
    public static void requestPublishDoPublishVideo(String content, String photo_image, String video_url, String province, String city, String address, AppRequestCallback<XRIndexSelectVideoActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("publish");
        params.putAct("do_publish");
        params.put("publish_type", "video");
        params.put("content", content);
        params.put("photo_image", photo_image);
        params.put("video_url", video_url);
        params.put("province", province);
        params.put("city", city);
        params.put("address", address);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????-????????????
     *
     * @param content
     * @param list
     * @param address
     * @param index_id
     * @param listener
     */
//    public static void requestPublishDoPublish(String content, String index_id, List<XRDoPublishImageTextModel> list, String province, String city, String address, AppRequestCallback<BaseActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("publish");
//        params.putAct("do_publish");
//        params.put("publish_type", "imagetext");
//        params.put("content", content);
//        params.put("address", address);
//        params.put("index_id", index_id);
//        params.put("data", JSON.toJSONString(list));
//        params.put("province", province);
//        params.put("city", city);
//        params.put("address", address);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????-??????????????????
     *
     * @param content  ????????????
     * @param data     ?????????????????????
     * @param listener
     */
//    public static void requestPublishDoPublish(String content, List<XRPublishRedPhotoData> data, String province, String city, String address, AppRequestCallback<BaseActModel> listener)
//    {
//        InitActModel init = InitActModelDao.query();
//        double weibo_red_price = init.getWeibo_red_price();
//
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("publish");
//        params.putAct("do_publish");
//        params.put("publish_type", "red_photo");
//        params.put("content", content);
//        params.put("price", weibo_red_price * data.size());
//        params.put("data", JSON.toJSONString(data));
//        params.put("province", province);
//        params.put("city", city);
//        params.put("address", address);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * @param content  ??????
     * @param price    ??????
     * @param data     ??????
     * @param city     ??????
     * @param province ??????
     * @param address  ??????
     * @param listener
     */
//    public static void requestPublishDoPublishGoods(String content, String price, List<XRPublishRedPhotoData> data, String province, String city, String address, AppRequestCallback<BaseActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("publish");
//        params.putAct("do_publish");
//        params.put("publish_type", "goods");
//        params.put("content", content);
//        params.put("price", price);
//        params.put("data", JSON.toJSONString(data));
//        params.put("province", province);
//        params.put("city", city);
//        params.put("address", address);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????-??????????????????
     *
     * @param price    ????????????
     * @param data     ????????????
     * @param listener
     */
//    public static void requestPublishDoPublishWeixin(String price, String data, int status, AppRequestCallback<BaseActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("publish");
//        params.putAct("do_publish");
//        params.put("publish_type", "weixin");
//        params.put("price", price);
//        params.put("data", data);
//        params.put("status", status);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????-????????????
     *
     * @param price    ????????????
     * @param data     ????????????
     * @param listener
     */
//    public static void requestPublishDoPublishPhoto(String content, String price, String photo_image, List<XRSelectPayPhotoModel> data, String province, String city, String address, AppRequestCallback<BaseActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("publish");
//        params.putAct("do_publish");
//        params.put("publish_type", "photo");
//        params.put("content", content);
//        params.put("price", price);
//        params.put("photo_image", photo_image);
//        params.put("data", JSON.toJSONString(data));
//        params.put("province", province);
//        params.put("city", city);
//        params.put("address", address);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????
     *
     * @param listener
     */
//    public static void requestUserWeixinStatus(AppRequestCallback<XRUserWeixinStatusActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("weixin_status");
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????-??????????????????
     *
     * @param data     ????????????
     * @param price    ????????????
     * @param status   0???????????? 1????????????
     * @param listener
     */
    public static void requestPublishOffWeixin(String data, String price, int status, AppRequestCallback<BaseActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("publish");
        params.putAct("do_publish");
        params.put("publish_type", "weixin");
        params.put("data", data);
        params.put("price", price);
        params.put("status", status);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????-????????????
     *
     * @param listener
     */
    public static void requestPublishOffWeixin(AppRequestCallback<BaseActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("publish");
        params.putAct("off_weixin");
        AppHttpUtil.getInstance().post(params, listener);
    }

//    public static void requestSearchHotTag(AppRequestCallback<XRRequestSearchHotTagResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("index");
//        params.putAct("hot_search");
//        AppHttpUtil.getInstance().post(params, listener);
//    }

//    public static void requestSearch(String keyword, @IntRange(from = 1, to = Integer.MAX_VALUE) int startPage, AppRequestCallback<XRSearchResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("search");
//        params.put("keyword", keyword);
//        params.put("p", startPage);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

//    public static void requestUserCenter(String userId, @IntRange(from = 1, to = Integer.MAX_VALUE) int startPage, AppRequestCallback<XRUserCenterResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("weibo_userinfo");
//        params.put("to_user_id", userId);
//        params.put("page", startPage);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????????????????
     *
     * @param weiboId
     * @param listener
     */
    public static void requestDynamicFavorite(String weiboId, AppRequestCallback<XRRequestUserDynamicFavoriteResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("publish_comment");
        params.put("type", 2);
        params.put("weibo_id", weiboId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????/????????????
     *
     * @param weiboId
     * @param listener
     */
    public static void requestDynamicComment(String weiboId, String content, boolean isReply, @Nullable String toCommentId, AppRequestCallback<XRDynamicCommentResopnseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("publish_comment");
        params.put("type", 1);
        params.put("weibo_id", weiboId);
        params.put("content", content);

        if (isReply)
        {
            params.put("to_comment_id", toCommentId);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????????????????
     *
     * @param weiboId
     * @param listener
     */
    public static void requestDeleteUserDynamic(String weiboId, AppRequestCallback<XRCommonActionRequestResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("del_weibo");
        params.put("weibo_id", weiboId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????????????????
     *
     * @param weiboId
     * @param listener
     */
    public static void requestStickTopUserDynamic(String weiboId, AppRequestCallback<XRStickTopUserDynamicResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("set_top");
        params.put("weibo_id", weiboId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????????????????
     *
     * @param weiboId
     * @param listener
     */
    public static void requestDynamicDetail(String weiboId, int startPage, AppRequestCallback<XRUserDynamicDetailResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("weibo");
        params.putAct("index");
        params.put("weibo_id", weiboId);
        params.put("page", startPage);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????
     *
     * @param page
     * @param listener
     */
//    public static void requestChatIndex(int page, AppRequestCallback<XRChatIndexActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("chat");
//        params.putAct("index");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ??????-????????????
     *
     * @param listener
     */
//    public static void requestChatMyComment(int page, AppRequestCallback<XRChatMyCommentActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("chat");
//        params.putAct("my_comment");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ??????????????????????????????
     *
     * @param listener
     */
//    public static void requestDynamicDetailFavoriteUsers(String weiboId, AppRequestCallback<XRDynamicDetailFavoriteUsersResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("weibo");
//        params.putAct("digg_list");
//        params.put("weibo_id", weiboId);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????????????????
     *
     * @param comment_id
     * @param listener
     */
    public static void requestDeleteDynamicComment(String comment_id, AppRequestCallback<XRCommonActionRequestResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("del_comment");
        params.put("comment_id", comment_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ??????-???????????????
     *
     * @param type     1???????????? 2???????????? 0 ??????
     * @param page
     * @param listener
     */
//    public static void requestDiscoveryNearbyList(int type, int page, AppRequestCallback<XRNearbyActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("discovery");
//        params.putAct("nearby_list");
//        params.put("page", page);
//        params.put("type", type);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????????????????
     *
     * @param listener
     */
//    public static void requestUserDoPublishImg(List<String> list, AppRequestCallback<XRUploadShowImageResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("do_publish_img");
//        params.put("data", JSON.toJSONString(list));
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ?????????????????????????????????
     *
     * @param listener
     */
    public static void requestUploadUserTopBackground(String url, AppRequestCallback<BaseActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("change_user_photo_img");
        params.put("photo_img", url);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ???????????? - ????????????
     *
     * @param page
     * @param listener
     */
//    public static void requestUserPurchase(int page, AppRequestCallback<XRUserPurchaseResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("my_buy_weixin");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ?????? - ??????????????????
     *
     * @param page
     * @param listener
     */
//    public static void requestRecommendDynamic(int page, AppRequestCallback<XRRecommentDynamicResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("discovery");
//        params.putAct("index");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ??????????????????
     *
     * @param listener
     */
    public static void requestReportType(AppRequestCallback<XRReportTypeResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("app");
        params.putAct("tipoff_type");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????
     *
     * @param userId
     * @param type
     * @param listener
     */
    public static void requestReportUser(String userId, String type, AppRequestCallback<XRCommonActionRequestResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("tipoff_weibo");
        params.put("type", type);
        params.put("to_user_id", userId);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ??????????????????
     *
     * @param userId
     * @param dynamicId
     * @param type
     * @param listener
     */
    public static void requestReportUserDynamic(String userId, String dynamicId, String type, AppRequestCallback<XRCommonActionRequestResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("tipoff_weibo");
        params.put("weibo_id", dynamicId);
        params.put("to_user_id", userId);
        params.put("type", type);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ???????????? - ????????????
     *
     * @param page
     * @param listener
     */
//    public static void requestUserBuyerList(int page, AppRequestCallback<XRUserBuyerListResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("my_buyer");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ?????? - ??????????????????
     *
     * @param page
     * @param listener
     */
//    public static void requestUserWithdrawRecord(int page, AppRequestCallback<XRUserWithdrawRecordResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putAct("extract_record");
//        params.putCtl("user_center");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????-????????????
     *
     * @param listener
     */
//    public static void requestUserLevelDescription(AppRequestCallback<XRUserLevelDescriptionResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putAct("user_levels");
//        params.putCtl("user_center");
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????
     *
     * @param page
     * @param listener
     */
//    public static void requestDistributionIndex(int page, AppRequestCallback<XRDistributionIndexActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("distribution");
//        params.putAct("index");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????
     *
     * @param to_user_id ??????ID
     * @param listener
     */
//    public static void requestPayPayUserInfo(String to_user_id, AppRequestCallback<XRPayPayUserInfoActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("pay");
//        params.putAct("pay_user_info");
//        params.put("to_user_id", to_user_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }


    /**
     * ???????????? - ??????????????????
     *
     * @param price
     * @param listener
     */
    public static void requestSetUserChatPrice(String price, AppRequestCallback<XRCommonActionRequestResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("set_chat_price");
        params.put("price", price);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????????????????
     *
     * @param notice_id
     * @param listener
     */
//    public static void requestBuyerListTradeDetail(String notice_id, AppRequestCallback<XRBuyerListTradeDetailResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("my_buyer_user");
//        params.put("notice_id", notice_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????????????????
     *
     * @param notice_id
     * @param listener
     */
//    public static void requestUserPurchaseTradeDetail(String notice_id, AppRequestCallback<XRUserPurchaseTradeDetailResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("my_buy_weixin_user");
//        params.put("notice_id", notice_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ??????????????????
     *
     * @param weibo_id
     * @param listener
     */
//    public static void requestUserUserDynamicDetailGoods(String weibo_id, AppRequestCallback<XRUserDynamicDetailGoodsResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("weibo");
//        params.putAct("goods_info");
//        params.put("weibo_id", weibo_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ??????????????????
     *
     * @param listener
     */
//    public static void requestPayPayList(AppRequestCallback<XRPayPayListActModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("pay");
//        params.putAct("pay_list");
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * @param weibo_id ??????ID
     * @param pay_id   ??????ID
     * @param money    ??????
     * @param listener
     */
    public static void requestPayPay(String weibo_id, String pay_id, String money, AppRequestCallback<App_payActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("pay");
        params.putAct("pay");
        params.put("weibo_id", weibo_id);
        params.put("pay_id", pay_id);
        params.put("money", money);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * @param to_user_id ?????????????????????ID
     * @param pay_id     ??????ID
     * @param money      ??????
     * @param type       ????????? 'weixin','reward','chat'
     * @param account    ?????????????????????????????????????????????
     * @param listener
     */
    public static void requestPayPayUser(String to_user_id, String pay_id, String money, String type, String account, AppRequestCallback<App_payActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("pay");
        params.putAct("pay_user");
        params.put("to_user_id", to_user_id);
        params.put("pay_id", pay_id);
        params.put("money", money);
        params.put("type", type);
        params.put("account", account);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????????????????
     *
     * @param to_user_id
     * @param page
     * @param listener
     */
//    public static void requestUserGuardRanking(String to_user_id, int page, AppRequestCallback<XRUserGuardRankingResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user_center");
//        params.putAct("cont");
//        params.put("to_user_id", to_user_id);
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }
//
//    public static void requestUserDynamicSelf(int page, AppRequestCallback<XRUserDynamicSelfResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("weibo_mine");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }
//
//
//    /**
//     * ?????????????????????
//     *
//     * @param to_user_id
//     * @param listener
//     */
//    public static void requestSetUserBlackList(String to_user_id, AppRequestCallback<XRSetUserBlackListResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user");
//        params.putAct("set_black");
//        params.put("to_user_id", to_user_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * @param weibo_id ??????ID
     * @param listener
     */
    public static void requestAddVideoPlayCount(String weibo_id, AppRequestCallback<XRAddVideoPlayCountResponseModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("weibo");
        params.putAct("add_video_count");
        params.put("weibo_id", weibo_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param weibo_id
     * @param listener
     */
//    public static void requestRedPocketPhotoPaySuccess(String weibo_id, AppRequestCallback<XRRedPocketPhotoPaySuccessResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("weibo");
//        params.putAct("index");
//        params.put("weibo_id", weibo_id);
//        params.put("is_paid", 1);
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????
     *
     * @param page       ????????????
     * @param to_user_id ??????????????????id(???ID?????????????????????)
     * @param listener
     */
    public static void requestMy_focus(int page, String to_user_id, AppRequestCallback<App_focus_follow_ActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("user_focus");
        params.put("p", page);

        if (!TextUtils.isEmpty(to_user_id))
        {
            params.put("to_user_id", to_user_id);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ??????????????????
     *
     * @param listener
     */
    public static void requestPublishCheckType(AppRequestCallback<XRPublishCheckTypeActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("publish");
        params.putAct("check_type");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????????????????
     *
     * @param url
     * @param listener
     */
    public static void requestDeleteShowPhoto(String url, AppRequestCallback<BaseActModel> listener)
    {
        AppRequestParams params = getXRRequestParams();
        params.putCtl("user");
        params.putAct("del_publish_img");
        params.put("url", url);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * ????????????-????????????
     *
     * @param listener
     */
//    public static void requestUserProfit(AppRequestCallback<XRUserProfitResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user_center");
//        params.putAct("profit");
//        AppHttpUtil.getInstance().post(params, listener);
//    }

    /**
     * ????????????-????????????
     *
     * @param listener
     */
//    public static void requestUserWithdrawInfo(AppRequestCallback<XRUserWithdrawInfoResponseModel> listener)
//    {
//        AppRequestParams params = getXRRequestParams();
//        params.putCtl("user_center");
//        params.putAct("money_carry_alipay");
//        AppHttpUtil.getInstance().post(params, listener);
//    }

}