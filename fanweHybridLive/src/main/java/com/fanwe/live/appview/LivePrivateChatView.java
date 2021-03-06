package com.fanwe.live.appview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.lib.player.SDMediaPlayer;
import com.fanwe.lib.recorder.SDMediaRecorder;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.library.utils.ImageFileCompresser;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDDialogUtil;
import com.fanwe.library.utils.SDFileUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewSizeLocker;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.library.view.SDReplaceableLayout;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LivePrivateChatRecyclerAdapter;
import com.fanwe.live.adapter.viewholder.privatechat.PrivateChatViewHolder;
import com.fanwe.live.business.LivePrivateChatBusiness;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveGameExchangeDialog;
import com.fanwe.live.dialog.PrivateChatLongClickMenuDialog;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ESDMediaPlayerStateChanged;
import com.fanwe.live.model.Deal_send_propActModel;
import com.fanwe.live.model.LiveExpressionModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgPrivateVoice;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.span.LiveExpressionSpan;
import com.fanwe.live.view.LiveRecordView;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.io.File;
import java.util.List;

/**
 * ????????????
 */
public class LivePrivateChatView extends BaseAppView
{
    public LivePrivateChatView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LivePrivateChatView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LivePrivateChatView(Context context)
    {
        super(context);
        init();
    }

    /**
     * ????????????????????????????????????
     */
    private static final long SCROLL_DELAY = 100;
    /**
     * ????????????????????????
     */
    private static final int MAX_INPUT_LENGTH = 255;


    private SDTitleSimple title;
    private LivePrivateChatRecordView view_record;
    private LivePrivateChatBarView view_chat_bar;
    private SDRecyclerView lv_content;
    private SDReplaceableLayout fl_bottom_extend;

    private LiveExpressionView view_expression;
    private LivePrivateChatMoreView view_more;
    private LiveSendGiftView view_gift;

    private LivePrivateChatRecyclerAdapter mAdapter;

    private ImageFileCompresser mImageFileCompresser;
    private PhotoHandler mPhotoHandler;

    private boolean mOnUpCancelView;
    private ClickListener mClickListener;

    private boolean mLockHeightEnable;
    private int mLockHeight;
    private SDViewSizeLocker mContentSizeLocker;

    private LivePrivateChatBusiness mChatBusiness;

    private void init()
    {
        setContentView(R.layout.view_live_private_chat);

        title = find(R.id.title);
        view_record = find(R.id.view_record);
        view_chat_bar = find(R.id.view_chat_bar);
        lv_content = find(R.id.lv_content);
        fl_bottom_extend = find(R.id.fl_bottom_extend);

        mChatBusiness = new LivePrivateChatBusiness(mPrivateChatBusinessCallback);

        mContentSizeLocker = new SDViewSizeLocker(findViewById(R.id.view_pull_to_refresh));
        initTitle();

        initPullView();

        view_chat_bar.setClickListener(mChatbarClickListener);
        view_chat_bar.et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_INPUT_LENGTH)});
        view_record.setRecordViewListener(recordViewListener);
        view_record.setRecordView(view_chat_bar.tv_record);

        SDMediaRecorder.getInstance().init(getContext());
        SDMediaRecorder.getInstance().setOnRecorderCallback(mOnRecorderCallback);
        SDMediaRecorder.getInstance().setOnCountDownCallback(mOnCountDownCallback);
        SDMediaRecorder.getInstance().setMaxRecordTime(60 * 1000);

        initImageFileCompresser();
        initPhotoHandler();

        if (LiveInformation.getInstance().getRoomId() > 0)
        {
            setVoiceModeEnable(false);
            setTakePhotoEnable(false);
        } else
        {
            setVoiceModeEnable(true);
            setTakePhotoEnable(true);
        }

        dealHasPrivateChat();
    }

    /**
     * ??????????????????id
     *
     * @param userId
     */
    public void setUserId(String userId)
    {
        mChatBusiness.setUserId(userId);
        mChatBusiness.requestUserInfo();
        mChatBusiness.loadHistoryMessage(20);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param lockHeightEnable
     */
    public void setLockHeightEnable(boolean lockHeightEnable)
    {
        mLockHeightEnable = lockHeightEnable;
    }

    /**
     * ????????????????????????
     *
     * @param callback
     */
    public void addBottomExtendCallback(SDReplaceableLayout.SDReplaceableLayoutCallback callback)
    {
        fl_bottom_extend.addCallback(callback);
    }

    public void setClickListener(ClickListener clickListener)
    {
        mClickListener = clickListener;
    }

    /**
     * ?????????????????????
     *
     * @param visible
     * @param height
     */
    public void onKeyboardVisibilityChange(boolean visible, int height)
    {
        if (visible)
        {
            calculateLockHeight();
        }
    }

    /**
     * ??????????????????????????????
     */
    private void calculateLockHeight()
    {
        if (mLockHeight > 0)
        {
            return;
        }

        Rect rectLv = new Rect();
        lv_content.getGlobalVisibleRect(rectLv);

        mLockHeight = rectLv.height();
    }

    private void initImageFileCompresser()
    {
        mImageFileCompresser = new ImageFileCompresser();
        mImageFileCompresser.setmListener(new ImageFileCompresser.ImageFileCompresserListener()
        {
            @Override
            public void onStart()
            {
                showProgressDialog("??????????????????");
            }

            @Override
            public void onSuccess(File fileCompressed)
            {
                mChatBusiness.sendIMImage(fileCompressed);
            }

            @Override
            public void onFailure(String msg)
            {
                SDToast.showToast(msg);
            }

            @Override
            public void onFinish()
            {
                dismissProgressDialog();
            }
        });
    }

    private void initPullView()
    {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                mChatBusiness.loadHistoryMessage(20);
            }

            @Override
            public void onRefreshingFromFooter()
            {

            }
        });

        mAdapter = new LivePrivateChatRecyclerAdapter(getActivity());
        mAdapter.setClickListener(adapterClickListener);
        lv_content.setAdapter(mAdapter);

    }

    private void initPhotoHandler()
    {
        mPhotoHandler = new PhotoHandler((FragmentActivity) getActivity());
        mPhotoHandler.setListener(new PhotoHandler.PhotoHandlerListener()
        {
            @Override
            public void onResultFromAlbum(File file)
            {
                dealImage(file);
            }

            @Override
            public void onResultFromCamera(File file)
            {
                dealImage(file);
            }

            @Override
            public void onFailure(String msg)
            {

            }
        });
    }

    private void dealImage(File file)
    {
        if (file != null)
        {
            mImageFileCompresser.compressImageFile(file);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param voiceModeEnable
     */
    public void setVoiceModeEnable(boolean voiceModeEnable)
    {
        if (view_chat_bar != null)
        {
            view_chat_bar.setVoiceModeEnable(voiceModeEnable);
        }
    }

    /**
     * ????????????????????????
     *
     * @param takePhotoEnable
     */
    public void setTakePhotoEnable(boolean takePhotoEnable)
    {
        getMoreView();
        if (view_more != null)
        {
            view_more.setTakePhotoEnable(takePhotoEnable);
        }
    }

    /**
     * ???????????????
     */
    public void hideKeyboard()
    {
        view_chat_bar.hideKeyboard();
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private View getExpressionView()
    {
        if (view_expression == null)
        {
            view_expression = new LiveExpressionView(getContext());
            view_expression.setCallback(mExpressionViewCallback);
        }
        return view_expression;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private View getMoreView()
    {
        if (view_more == null)
        {
            view_more = new LivePrivateChatMoreView(getContext());
            view_more.setSendCoinsEnable(InitActModelDao.query().getOpen_send_coins_module() == 1);
            view_more.setSendDiamondsEnable(InitActModelDao.query().getOpen_send_diamonds_module() == 1);
            view_more.setCallback(mPrivateChatMoreViewCallback);
        }
        return view_more;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private View getGiftView()
    {
        if (view_gift == null)
        {
            view_gift = new LiveSendGiftView(getContext());
            view_gift.requestData();
            view_gift.setCallback(mSendGiftViewCallback);
        }
        CommonInterface.requestMyUserInfo(null);
        return view_gift;
    }

    private void initTitle()
    {
        title.initRightItem(1);
        title.getItemRight(0).setImageRight(R.drawable.ic_private_chat_title_bar_user);
        title.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        title.setmListener(new SDTitleSimpleListener()
        {

            @Override
            public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
            {
                Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, mChatBusiness.getUserId());
                getActivity().startActivity(intent);
            }

            @Override
            public void onCLickMiddle_SDTitleSimple(SDTitleItem v)
            {
            }

            @Override
            public void onCLickLeft_SDTitleSimple(SDTitleItem v)
            {
                if (mClickListener != null)
                {
                    mClickListener.onClickBack();
                }
            }
        });
    }

    /**
     * ????????????????????????
     */
    private LiveSendGiftView.SendGiftViewCallback mSendGiftViewCallback = new LiveSendGiftView.SendGiftViewCallback()
    {
        @Override
        public void onClickSend(LiveGiftModel model, int is_plus)
        {
            mChatBusiness.requestSendGiftPrivate(model);
        }
    };

    /**
     * ??????????????????
     */
    private LiveExpressionView.ExpressionViewCallback mExpressionViewCallback = new LiveExpressionView.ExpressionViewCallback()
    {
        @Override
        public void onClickExpression(LiveExpressionModel model)
        {
            String key = model.getKey();

            if (MAX_INPUT_LENGTH > 0)
            {
                if (key != null)
                {
                    int length = view_chat_bar.et_content.getText().toString().length();
                    if ((length + key.length()) > MAX_INPUT_LENGTH)
                    {
                        return;
                    }
                }
            }

            view_chat_bar.et_content.insertSpan(new LiveExpressionSpan(getContext(), model.getResId()), key);
        }

        @Override
        public void onClickDelete()
        {
            view_chat_bar.et_content.removeSpan();
        }
    };

    /**
     * ?????????????????????????????????
     */
    private LivePrivateChatMoreView.PrivateChatMoreViewCallback mPrivateChatMoreViewCallback = new LivePrivateChatMoreView.PrivateChatMoreViewCallback()
    {
        @Override
        public void onClickGift()
        {
            showBottomExtendGift();
        }

        @Override
        public void onClickPhoto()
        {
            mPhotoHandler.getPhotoFromAlbum();
        }

        @Override
        public void onClickCamera()
        {
            mPhotoHandler.getPhotoFromCamera();
        }

        @Override
        public void onClickSendCoin()
        {
            long coins = UserModelDao.query().getCoin();
            LiveGameExchangeDialog dialog = new LiveGameExchangeDialog(getActivity(), LiveGameExchangeDialog.TYPE_COIN_SEND, mListener);
            dialog.setCurrency(coins);
            dialog.setToUserId(mChatBusiness.getUserId());
            dialog.show();
            CommonInterface.requestMyUserInfo(null);
        }

        @Override
        public void onClickSendDialond()
        {
            long diamonds = UserModelDao.query().getDiamonds();
            LiveGameExchangeDialog dialog = new LiveGameExchangeDialog(getActivity(), LiveGameExchangeDialog.TYPE_DIAMOND_SEND, mListener);
            dialog.setCurrency(diamonds);
            dialog.setToUserId(mChatBusiness.getUserId());
            dialog.show();
            CommonInterface.requestMyUserInfo(null);
        }
    };

    /**
     * ????????????
     */
    private LiveRecordView.RecordViewListener recordViewListener = new LiveRecordView.RecordViewListener()
    {
        @Override
        public void onUpCancelView()
        {
            mOnUpCancelView = true;
            view_chat_bar.tv_record.setText("????????????");
            view_chat_bar.tv_record.setBackgroundResource(R.drawable.res_layer_white_corner);
            SDMediaRecorder.getInstance().stop();
        }

        @Override
        public void onUp()
        {
            mOnUpCancelView = false;
            view_chat_bar.tv_record.setText("????????????");
            view_chat_bar.tv_record.setBackgroundResource(R.drawable.res_layer_white_corner);
            SDMediaRecorder.getInstance().stop();
        }

        @Override
        public void onLeaveCancelView()
        {
            view_chat_bar.tv_record.setText("????????????");
        }

        @Override
        public void onEnterCancelView()
        {
            view_chat_bar.tv_record.setText("????????????,????????????");
        }

        @Override
        public boolean onDownRecordView()
        {
            view_chat_bar.tv_record.setText("????????????");
            view_chat_bar.tv_record.setBackgroundResource(R.drawable.res_layer_white_corner);
            SDMediaRecorder.getInstance().start(null);

            return true;
        }

        @Override
        public void onCancel()
        {
            SDMediaRecorder.getInstance().stop();
        }
    };

    /**
     * ??????????????????
     */
    private SDMediaRecorder.OnCountDownCallback mOnCountDownCallback = new SDMediaRecorder.OnCountDownCallback()
    {
        @Override
        public void onTick(long leftTime)
        {
            view_record.setTextRecordTime(SDDateUtil.formatDuring2mmss(leftTime));
        }

        @Override
        public void onFinish()
        {
            view_record.setTextRecordTime(String.valueOf(0));
            view_record.cancelGesture();
        }
    };

    /**
     * ????????????
     */
    private SDMediaRecorder.OnRecorderCallback mOnRecorderCallback = new SDMediaRecorder.OnRecorderCallback()
    {
        @Override
        public void onRecordSuccess(File file, long duration)
        {
            if (file == null)
            {
                return;
            }

            if (mOnUpCancelView)
            {
                SDFileUtil.deleteFileOrDir(file);
            } else
            {
                if (duration < 1000)
                {
                    SDFileUtil.deleteFileOrDir(file);
                    SDToast.showToast("??????????????????");
                } else
                {
                    mChatBusiness.sendIMVoice(file, duration);
                }
            }
        }
    };

    /**
     * ????????????????????????
     */
    public void lockContent()
    {
        if (mLockHeightEnable)
        {
            if (mLockHeight > 0)
            {
                mContentSizeLocker.lockHeight(mLockHeight);
                SDViewUtil.setHeightMatchParent(fl_bottom_extend);
            }
        }
    }

    /**
     * ????????????????????????
     */
    public void unLockContent()
    {
        if (mLockHeightEnable)
        {
            if (mLockHeight > 0)
            {
                mContentSizeLocker.unlockHeight();
                SDViewUtil.setHeightWrapContent(fl_bottom_extend);
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private LivePrivateChatBarView.ClickListener mChatbarClickListener = new LivePrivateChatBarView.ClickListener()
    {
        @Override
        public void onClickKeyboard()
        {
            removeBottomExtend(true);
            lv_content.scrollToEndDelayed(SCROLL_DELAY);
        }

        @Override
        public void onClickVoice()
        {
            removeBottomExtend(true);
        }

        @Override
        public void onClickShowExpression()
        {
            lockContent();
            showBottomExtendExpression();
            lv_content.scrollToEndDelayed(SCROLL_DELAY);
        }

        @Override
        public void onClickHideExpression()
        {
            removeBottomExtend(false);
        }

        @Override
        public void onClickMore()
        {
            lockContent();
            showBottomExtendMore();
            lv_content.scrollToEndDelayed(SCROLL_DELAY);
        }

        @Override
        public void onClickSend(String content)
        {
            if (TextUtils.isEmpty(content))
            {
                SDToast.showToast("???????????????");
                return;
            }
            mChatBusiness.sendIMText(content);
            view_chat_bar.et_content.setText("");
        }

        @Override
        public boolean onTouchEditText()
        {
            removeBottomExtend(false);
            lv_content.scrollToEndDelayed(SCROLL_DELAY);
            return false;
        }
    };

    /**
     * ????????????????????????
     */
    private void showBottomExtendExpression()
    {
        replaceBottomExtend(getExpressionView());
    }

    /**
     * ????????????????????????
     */
    private void showBottomExtendGift()
    {
        replaceBottomExtend(getGiftView());
    }

    /**
     * ????????????????????????
     */
    private void showBottomExtendMore()
    {
        replaceBottomExtend(getMoreView());
    }

    private void replaceBottomExtend(View view)
    {
        if (mLockHeightEnable)
        {
            if (view instanceof ILivePrivateChatMoreView)
            {
                ILivePrivateChatMoreView moreView = (ILivePrivateChatMoreView) view;
                if (mContentSizeLocker.hasLockHeight())
                {
                    moreView.setHeightMatchParent();
                } else
                {
                    moreView.setHeightWrapContent();
                }
            }
        }

        fl_bottom_extend.replaceContent(view);
    }

    /**
     * ????????????????????????
     */
    private void removeBottomExtend(boolean unlockContent)
    {
        if (unlockContent)
        {
            unLockContent();
        }
        fl_bottom_extend.removeContent();
    }

    private void dealHasPrivateChat()
    {
        if (AppRuntimeWorker.hasPrivateChat())
        {
            if (mChatBusiness.canSendPrivateLetter())
            {
                SDViewUtil.setVisible(view_chat_bar);
            } else
            {
                SDViewUtil.setGone(view_chat_bar);
            }
        } else
        {
            SDViewUtil.setGone(view_chat_bar);
        }
    }

    /**
     * ???????????????
     *
     * @param event
     */
    public void onEventMainThread(EImOnNewMessages event)
    {
        mChatBusiness.onEventMainThread(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            boolean isAbove = ev.getRawY() < SDViewUtil.getGlobalVisibleRect(view_chat_bar).top;
            if (isAbove)
            {
                view_chat_bar.showNormalMode();
                removeBottomExtend(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ????????????????????????
     */
    private PrivateChatViewHolder.ClickListener adapterClickListener = new PrivateChatViewHolder.ClickListener()
    {

        @Override
        public void onClickResend(MsgModel model)
        {
            mChatBusiness.sendIMMsg(model);
        }

        @Override
        public void onClickHeadImage(MsgModel model)
        {
            Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
            intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getCustomMsg().getSender().getUser_id());
            getActivity().startActivity(intent);
        }

        @Override
        public void onLongClick(final MsgModel model, View v)
        {
            final PrivateChatLongClickMenuDialog dialog = new PrivateChatLongClickMenuDialog(getActivity());
            switch (model.getCustomMsgType())
            {
                case LiveConstant.CustomMsgType.MSG_PRIVATE_TEXT:
                    dialog.setItems("??????");
                    dialog.setItemClickCallback(new SDItemClickCallback<String>()
                    {
                        @Override
                        public void onItemClick(int position, String item, View view)
                        {
                            SDOtherUtil.copyText(model.getCustomMsgPrivateText().getText());
                            SDToast.showToast("?????????");
                            dialog.dismiss();
                        }
                    });
                    SDDialogUtil.setDialogTopAlignCenter(dialog, v, 10, 0);
                    dialog.show();
                    break;

                default:
                    break;
            }
        }
    };

    public void onEventMainThread(ESDMediaPlayerStateChanged event)
    {
        if (event.state == SDMediaPlayer.State.Completed ||
                event.state == SDMediaPlayer.State.Stopped ||
                event.state == SDMediaPlayer.State.Playing)
        {
            List<MsgModel> listMsg = mAdapter.getData();
            if (!SDCollectionUtil.isEmpty(listMsg))
            {
                int i = 0;
                for (MsgModel msg : listMsg)
                {
                    if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_PRIVATE_VOICE)
                    {
                        CustomMsgPrivateVoice msgVoice = msg.getCustomMsgPrivateVoice();
                        String filePah = msgVoice.getPath();
                        if (!TextUtils.isEmpty(filePah) && filePah.equals(SDMediaPlayer.getInstance().getDataPath()))
                        {
                            mAdapter.updateData(i);
                        }
                    }
                    i++;
                }
            }
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(activity, requestCode, resultCode, data);
        mPhotoHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        mChatBusiness.onDestroy();
        SDMediaRecorder.getInstance().stop();
        SDMediaRecorder.getInstance().setOnRecorderCallback(null);
        SDMediaRecorder.getInstance().setOnCountDownCallback(null);
        SDMediaPlayer.getInstance().reset();
        mImageFileCompresser.deleteCompressedImageFile();

        super.onDetachedFromWindow();
    }

    public interface ClickListener
    {
        void onClickBack();
    }

    private LiveGameExchangeDialog.OnSuccessListener mListener = new LiveGameExchangeDialog.OnSuccessListener()
    {
        @Override
        public void onExchangeSuccess(long diamonds, long coins)
        {

        }

        @Override
        public void onSendCurrencySuccess(Deal_send_propActModel model)
        {
            mChatBusiness.sendIMGift(model);
        }
    };

    /**
     * ????????????
     */
    private LivePrivateChatBusiness.LivePrivateChatBusinessCallback mPrivateChatBusinessCallback = new LivePrivateChatBusiness.LivePrivateChatBusinessCallback()
    {
        @Override
        public void onBsShowProgress(String msg)
        {

        }

        @Override
        public void onBsHideProgress()
        {

        }

        @Override
        public void onRequestUserInfoSuccess(UserModel userModel)
        {
            title.setMiddleTextTop(userModel.getNick_name());
        }

        @Override
        public void onRequestSendGiftPrivateSuccess(Deal_send_propActModel actModel, LiveGiftModel giftModel)
        {
            view_gift.sendGiftSuccess(giftModel);
            mChatBusiness.sendIMGift(actModel);
        }

        @Override
        public void onLoadHistoryMessageSuccess(List<MsgModel> listMsg)
        {
            if (listMsg != null)
            {
                mAdapter.insertData(0, listMsg);
            }
            getPullToRefreshViewWrapper().stopRefreshing();
            lv_content.scrollToPosition(listMsg.size() - 1);
        }

        @Override
        public void onLoadHistoryMessageError()
        {
            getPullToRefreshViewWrapper().stopRefreshing();
        }

        @Override
        public void onAdapterAppendData(MsgModel model)
        {
            mAdapter.appendData(model);
            lv_content.scrollToEndDelayed(SCROLL_DELAY);
        }

        @Override
        public void onAdapterUpdateData(int position, MsgModel model)
        {
            mAdapter.updateData(position, model);
        }

        @Override
        public void onAdapterUpdateData(int position)
        {
            mAdapter.updateData(position);
        }

        @Override
        public int onAdapterIndexOf(MsgModel model)
        {
            return mAdapter.indexOf(model);
        }
    };
}